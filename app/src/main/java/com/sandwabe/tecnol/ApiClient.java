package com.sandwabe.tecnol;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * High-performance REST API Client for MongoDB Backend
 * Communicates with Next.js multi-database backend
 */
public class ApiClient {
    private static final String TAG = "ApiClient";
    private static final String PREF_API_URL = "api_server_url";
    public static final String PRODUCTION_URL = "https://errrorrrr.com";
    private static final String DEFAULT_SERVER_URL = PRODUCTION_URL;
    
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static User cachedUser = null;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(4, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS)
            .writeTimeout(4, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build();

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public static List<String> getCandidateBaseUrls(Context context) {
        List<String> list = new ArrayList<>();
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
            String custom = prefs.getString(PREF_API_URL, null);
            if (custom != null && !custom.trim().isEmpty()) {
                list.add(custom.trim());
            }
        }
        if (!list.contains(PRODUCTION_URL)) list.add(PRODUCTION_URL);
        if (!list.contains("http://127.0.0.1:3000")) list.add("http://127.0.0.1:3000");
        if (!list.contains("http://192.168.0.117:3000")) list.add("http://192.168.0.117:3000");
        if (!list.contains("http://10.0.2.2:3000")) list.add("http://10.0.2.2:3000");
        return list;
    }

    public static String getBaseUrl(Context context) {
        if (context == null) return PRODUCTION_URL;
        SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        return prefs.getString(PREF_API_URL, PRODUCTION_URL);
    }

    public static void setBaseUrl(Context context, String newUrl) {
        if (context == null || newUrl == null) return;
        context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE)
                .edit()
                .putString(PREF_API_URL, newUrl.trim())
                .apply();
    }

    private static final String PREFS_NAME = "UserAppPrefs";
    private static final String KEY_CACHED_USER_JSON = "cached_user_json";

    public static User getCachedUser(Context context) {
        if (cachedUser != null) return cachedUser;
        if (context != null) {
            try {
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                String jsonStr = prefs.getString(KEY_CACHED_USER_JSON, null);
                if (jsonStr != null && !jsonStr.trim().isEmpty()) {
                    cachedUser = User.fromJson(jsonStr);
                    return cachedUser;
                }
            } catch (Exception e) {
                Log.w(TAG, "Failed to restore cached user from prefs", e);
            }
        }
        return cachedUser;
    }

    public static User getCachedUser() {
        return cachedUser;
    }

    public static void setCachedUser(Context context, User user) {
        cachedUser = user;
        if (context != null) {
            try {
                SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
                if (user != null && user.toJson() != null) {
                    editor.putString(KEY_CACHED_USER_JSON, user.toJson().toString());
                } else {
                    editor.remove(KEY_CACHED_USER_JSON);
                }
                editor.apply();
            } catch (Exception e) {
                Log.w(TAG, "Failed to persist cached user to prefs", e);
            }
        }
    }

    public static void setCachedUser(User user) {
        cachedUser = user;
    }

    public static void clearCachedUser(Context context) {
        cachedUser = null;
        if (context != null) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .remove(KEY_CACHED_USER_JSON)
                    .apply();
        }
    }

    /**
     * Authenticate resident with username (Iqama ID) and password against MongoDB multi-database backend
     */
    public static void login(Context context, String username, String password, ApiCallback<User> callback) {
        List<String> candidates = getCandidateBaseUrls(context);
        attemptLoginWithCandidate(context, username, password, candidates, 0, callback);
    }

    private static void attemptLoginWithCandidate(Context context, String username, String password,
                                                   List<String> candidates, int index, ApiCallback<User> callback) {
        if (index >= candidates.size()) {
            mainHandler.post(() -> callback.onError("Cannot connect to server. Check your connection."));
            return;
        }

        String baseUrl = candidates.get(index);
        String url = baseUrl + "/api/v1/mobile/login";

        JSONObject json = new JSONObject();
        try {
            json.put("username", username.trim());
            json.put("password", password != null ? password.trim() : "");
        } catch (Exception e) {
            callback.onError("JSON packaging error");
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "AbsherAndroid/1.2.0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG, "Endpoint " + baseUrl + " failed: " + e.getMessage() + ", trying fallback...");
                attemptLoginWithCandidate(context, username, password, candidates, index + 1, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null) {
                        attemptLoginWithCandidate(context, username, password, candidates, index + 1, callback);
                        return;
                    }
                    String resStr = responseBody.string();
                    JSONObject resJson = new JSONObject(resStr);

                    if (response.isSuccessful() && resJson.optBoolean("success", false)) {
                        setBaseUrl(context, baseUrl);
                        JSONObject userJson = resJson.optJSONObject("user");
                        if (userJson != null) {
                            User user = parseUserFromJson(userJson);
                            setCachedUser(context, user);
                            mainHandler.post(() -> callback.onSuccess(user));
                            return;
                        }
                    }

                    String errMsg = resJson.optString("error", "Invalid user ID or password");
                    mainHandler.post(() -> callback.onError(errMsg));
                } catch (Exception e) {
                    Log.e(TAG, "Login parsing failed", e);
                    attemptLoginWithCandidate(context, username, password, candidates, index + 1, callback);
                }
            }
        });
    }


    /**
     * Fetch user profile from MongoDB backend with candidate fallback and persistent cache
     */
    public static void fetchUser(Context context, String username, ApiCallback<User> callback) {
        if (username == null || username.trim().isEmpty()) {
            callback.onError("Username is empty");
            return;
        }

        List<String> candidates = getCandidateBaseUrls(context);
        attemptFetchUserWithCandidate(context, username, candidates, 0, callback);
    }

    private static void attemptFetchUserWithCandidate(Context context, String username,
                                                      List<String> candidates, int index, ApiCallback<User> callback) {
        if (index >= candidates.size()) {
            User fallback = getCachedUser(context);
            if (fallback != null) {
                mainHandler.post(() -> callback.onSuccess(fallback));
                return;
            }
            mainHandler.post(() -> callback.onError("Cannot connect to server. Check your connection."));
            return;
        }

        String baseUrl = candidates.get(index);
        String url = baseUrl + "/api/v1/mobile/user?username=" + username.trim();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "AbsherAndroid/1.2.0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG, "fetchUser endpoint " + baseUrl + " failed: " + e.getMessage() + ", trying fallback...");
                attemptFetchUserWithCandidate(context, username, candidates, index + 1, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null) {
                        attemptFetchUserWithCandidate(context, username, candidates, index + 1, callback);
                        return;
                    }
                    String resStr = responseBody.string();
                    if (response.isSuccessful()) {
                        JSONObject userJson = new JSONObject(resStr);
                        User user = parseUserFromJson(userJson);
                        setBaseUrl(context, baseUrl);
                        setCachedUser(context, user);
                        mainHandler.post(() -> callback.onSuccess(user));
                    } else {
                        Log.w(TAG, "fetchUser endpoint " + baseUrl + " returned " + response.code() + ", trying fallback...");
                        attemptFetchUserWithCandidate(context, username, candidates, index + 1, callback);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "fetchUser parse error for " + baseUrl, e);
                    attemptFetchUserWithCandidate(context, username, candidates, index + 1, callback);
                }
            }
        });
    }

    /**
     * Fetch dynamic QR verification code payload with candidate fallback
     */
    public static void fetchDynamicQr(Context context, String username, ApiCallback<String> callback) {
        List<String> candidates = getCandidateBaseUrls(context);
        attemptFetchQrWithCandidate(context, username, candidates, 0, callback);
    }

    private static void attemptFetchQrWithCandidate(Context context, String username,
                                                    List<String> candidates, int index, ApiCallback<String> callback) {
        if (index >= candidates.size()) {
            mainHandler.post(() -> callback.onError("Failed to fetch dynamic QR"));
            return;
        }

        String baseUrl = candidates.get(index);
        String url = baseUrl + "/api/v1/mobile/qr?username=" + (username != null ? username.trim() : "");

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "AbsherAndroid/1.2.0")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                attemptFetchQrWithCandidate(context, username, candidates, index + 1, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody != null && response.isSuccessful()) {
                        JSONObject json = new JSONObject(responseBody.string());
                        String qr = json.optString("verificationData", "");
                        setBaseUrl(context, baseUrl);
                        mainHandler.post(() -> callback.onSuccess(qr));
                    } else {
                        attemptFetchQrWithCandidate(context, username, candidates, index + 1, callback);
                    }
                } catch (Exception e) {
                    attemptFetchQrWithCandidate(context, username, candidates, index + 1, callback);
                }
            }
        });
    }

    public static User parseUserFromJson(JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, json.opt(key));
        }
        return User.fromMap(map);
    }
}

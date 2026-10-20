package com.sandwabe.tecnol;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyServicesFragment extends Fragment {
    private String currentUsername;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUsername = LoginActivity.getCurrentUsername(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_services, container, false);
        
        // Notifications & Settings
        View notificationsIcon = view.findViewById(R.id.notificationsIcon);
        if (notificationsIcon != null) {
            notificationsIcon.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), InboxActivity.class))
            );
        }
        
        View settingsIcon = view.findViewById(R.id.settingsIcon);
        if (settingsIcon != null) {
            settingsIcon.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), Settingsvact.class);
                intent.putExtra("USERNAME", currentUsername);
                startActivity(intent);
            });
        }

        // Search Bar opens SearchActivity
        View searchBarContainer = view.findViewById(R.id.searchBarContainer);
        if (searchBarContainer != null) {
            searchBarContainer.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), SearchActivity.class))
            );
        }

        // 1. Absher Travel
        View cardAbsherTravel = view.findViewById(R.id.cardAbsherTravel);
        if (cardAbsherTravel != null) {
            cardAbsherTravel.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), TravelRequestsActivity.class))
            );
        }

        // 2. Register Newborn
        View cardRegisterNewborn = view.findViewById(R.id.cardRegisterNewborn);
        if (cardRegisterNewborn != null) {
            cardRegisterNewborn.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), RegisterNewbornActivity.class))
            );
        }

        // 3. Renew Driving License
        View cardRenewDrivingLicense = view.findViewById(R.id.cardRenewDrivingLicense);
        if (cardRenewDrivingLicense != null) {
            cardRenewDrivingLicense.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), RenewDrivingLicenseActivity.class);
                startActivity(intent);
            });
        }

        // 4. Renew Resident ID
        View cardRenewResidentID = view.findViewById(R.id.cardRenewResidentID);
        if (cardRenewResidentID != null) {
            cardRenewResidentID.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), RenewResidentIDActivity.class))
            );
        }

        // 5. Authentication Services
        View cardAuthenticationServices = view.findViewById(R.id.cardAuthenticationServices);
        if (cardAuthenticationServices != null) {
            cardAuthenticationServices.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), AuthenticationServicesActivity.class))
            );
        }

        // 6. Update Resident Photo
        View cardUpdateResidentPhoto = view.findViewById(R.id.cardUpdateResidentPhoto);
        if (cardUpdateResidentPhoto != null) {
            cardUpdateResidentPhoto.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), PhotoRequestsActivity.class))
            );
        }

        // 7. Report Minor Accident
        View cardReportMinorAccident = view.findViewById(R.id.cardReportMinorAccident);
        if (cardReportMinorAccident != null) {
            cardReportMinorAccident.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ReportMinorAccidentActivity.class);
                startActivity(intent);
            });
        }

        // 8. Update Passport Information
        View cardUpdatePassportInfo = view.findViewById(R.id.cardUpdatePassportInfo);
        if (cardUpdatePassportInfo != null) {
            cardUpdatePassportInfo.setOnClickListener(v -> {
                CannotProceedActivity.startPassportUpdateError(getContext());
            });
        }

        return view;
    }
}
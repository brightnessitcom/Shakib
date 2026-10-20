# Project Architecture & System Theme Synchronization Guide

## 1. Theme System Specification (সিস্টেম থিম নীতি)
- **Automatic Device System Theme**:
  - The app MUST ALWAYS automatically follow the Android device's system appearance (Light / Dark mode).
  - No manual in-app theme selection cards or radio buttons exist in `Settingsvact` or `PreLoginSettingsActivity`.
  - When the user toggles Dark Mode on their device, the app instantly renders in Dark Mode (`values-night/` colors).
  - When the user toggles Light Mode on their device, the app instantly renders in Light Mode (`values/` colors).

## 2. Technical Implementation Architecture (টেকনিক্যাল আর্কিটেকচার)
### A. The Root Cause of MIUI / Android Custom Skin Context Conflicts:
- On Android, when an Activity or Application overrides context via `LanguageHelper.wrapContext()` (`createConfigurationContext(config)`), the localized configuration context can inadvertently isolate the Activity's resources if `config.uiMode` is unset or not synchronized with the physical device OS configuration.
- To permanently prevent this conflict:
  1. `LanguageHelper.wrapContext()` queries `Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK` and directly sets `config.uiMode`.
  2. `ThemeHelper.applySystemTheme()` inspects the device system state directly and configures `AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES / MODE_NIGHT_NO)`.
  3. `ThemeHelper.isDarkMode()` checks `Resources.getSystem().getConfiguration().uiMode` directly so all dynamic color operations (status bar, icons, text tinting) are guaranteed to match the device system 100%.
  4. `MainActivity` and `MyApp` use native `recreate()` while guarding initial fragment loading with `if (savedInstanceState == null)`. This ensures when the user toggles dark mode from any sub-screen (like Personal Profile), the fragment backstack and active screen are fully preserved without sending the user back to Home.

## 3. Language & Localization Implementation:
- App supports Arabic (`ar`) and English (`en`).
- RTL (Right-to-Left) layout direction is preserved alongside locale in `LanguageHelper.wrapContext()`.
- System night mode is preserved during language wrapping so language changes never affect theme mode.

## 4. Manage Screen Icons Specification:
- All Manage screen icons (`ic_person_plus.xml`, `ic_phone_sync.xml`, `ic_person_check.xml`, `ic_person_pause.xml`) are set to stroke width `2.0` (reduced boldness matching official Absher).
- Package name `tech.error` must remain intact.

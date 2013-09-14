package com.revolt.control.fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import com.revolt.control.ReVoltPreferenceFragment;
import com.revolt.control.R;
import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class StatusBarBattery extends ReVoltPreferenceFragment implements
        OnPreferenceChangeListener {

    private static final String PREF_BATT_ICON = "battery_icon_list";
    private static final String PREF_BATT_BAR = "battery_bar_list";
    private static final String PREF_BATT_BAR_STYLE = "battery_bar_style";
    private static final String PREF_BATT_BAR_COLOR = "battery_bar_color";
    private static final String PREF_BATT_BAR_WIDTH = "battery_bar_thickness";
    private static final String PREF_BATT_ANIMATE = "battery_bar_animate";
    private static final String PREF_LOW_BATTERY_WARNING_POLICY = "pref_low_battery_warning_policy";

    ListPreference mBatteryIcon;
    ListPreference mBatteryBar;
    ListPreference mBatteryBarStyle;
    ListPreference mBatteryBarThickness;
    CheckBoxPreference mBatteryBarChargingAnimation;
    ColorPickerPreference mBatteryBarColor;

    private ListPreference mLowBatteryWarning;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_statusbar_battery);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_statusbar_battery);

        mBatteryIcon = (ListPreference) findPreference(PREF_BATT_ICON);
        mBatteryIcon.setOnPreferenceChangeListener(this);
        mBatteryIcon.setValue((Settings.System.getInt(mContentRes,
                Settings.System.STATUSBAR_BATTERY_ICON, 0)) + "");

        mBatteryBar = (ListPreference) findPreference(PREF_BATT_BAR);
        mBatteryBar.setOnPreferenceChangeListener(this);
        mBatteryBar.setValue((Settings.System.getInt(mContentRes,
                Settings.System.STATUSBAR_BATTERY_BAR, 0)) + "");

        mBatteryBarStyle = (ListPreference) findPreference(PREF_BATT_BAR_STYLE);
        mBatteryBarStyle.setOnPreferenceChangeListener(this);
        mBatteryBarStyle.setValue((Settings.System.getInt(mContentRes,
                Settings.System.STATUSBAR_BATTERY_BAR_STYLE, 0)) + "");

        mBatteryBarColor = (ColorPickerPreference) findPreference(PREF_BATT_BAR_COLOR);
        mBatteryBarColor.setOnPreferenceChangeListener(this);

        mBatteryBarChargingAnimation = (CheckBoxPreference) findPreference(PREF_BATT_ANIMATE);
        mBatteryBarChargingAnimation.setChecked(Settings.System.getBoolean(mContentRes,
                Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE, false));

        mBatteryBarThickness = (ListPreference) findPreference(PREF_BATT_BAR_WIDTH);
        mBatteryBarThickness.setOnPreferenceChangeListener(this);
        mBatteryBarThickness.setValue((Settings.System.getInt(mContentRes,
                Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, 1)) + "");

        mLowBatteryWarning = (ListPreference) findPreference(PREF_LOW_BATTERY_WARNING_POLICY);
        int lowBatteryWarning = Settings.System.getInt(getActivity().getContentResolver(),
                                    Settings.System.POWER_UI_LOW_BATTERY_WARNING_POLICY, 3);
        mLowBatteryWarning.setValue(String.valueOf(lowBatteryWarning));
        mLowBatteryWarning.setSummary(mLowBatteryWarning.getEntry());
        mLowBatteryWarning.setOnPreferenceChangeListener(this);

        if (Integer.parseInt(mBatteryBar.getValue()) == 0) {
            mBatteryBarStyle.setEnabled(false);
            mBatteryBarColor.setEnabled(false);
            mBatteryBarChargingAnimation.setEnabled(false);
            mBatteryBarThickness.setEnabled(false);
            mBatteryBarStyle.setSummary(R.string.enable_battery_bar);
            mBatteryBarColor.setSummary(R.string.enable_battery_bar);
            mBatteryBarChargingAnimation.setSummary(R.string.enable_battery_bar);
            mBatteryBarThickness.setSummary(R.string.enable_battery_bar); 
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference) {
        if (preference == mBatteryBarChargingAnimation) {

            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE,
                    ((CheckBoxPreference) preference).isChecked() ? 1 : 0);
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBatteryIcon) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_BATTERY_ICON, val);
        } else if (preference == mBatteryBarColor) {
            String hex = ColorPickerPreference.convertToARGB(Integer
                    .valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);

            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_BATTERY_BAR_COLOR, intHex);
            return true;

        } else if (preference == mLowBatteryWarning) {
            int lowBatteryWarning = Integer.valueOf((String) newValue);
            int index = mLowBatteryWarning.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWER_UI_LOW_BATTERY_WARNING_POLICY, lowBatteryWarning);
            mLowBatteryWarning.setSummary(mLowBatteryWarning.getEntries()[index]);
            return true;

        } else if (preference == mBatteryBar) {

            int val = Integer.parseInt((String) newValue);
            Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_BATTERY_BAR, val);
            if (val == 0) {
                mBatteryBarStyle.setEnabled(false);
                mBatteryBarColor.setEnabled(false);
                mBatteryBarChargingAnimation.setEnabled(false);
                mBatteryBarThickness.setEnabled(false);
                mBatteryBarStyle.setSummary(R.string.enable_battery_bar);
                mBatteryBarColor.setSummary(R.string.enable_battery_bar);
                mBatteryBarChargingAnimation.setSummary(R.string.enable_battery_bar);
                mBatteryBarThickness.setSummary(R.string.enable_battery_bar);
            } else {
                mBatteryBarStyle.setEnabled(true);
                mBatteryBarColor.setEnabled(true);
                mBatteryBarChargingAnimation.setEnabled(true);
                mBatteryBarThickness.setEnabled(true);
                mBatteryBarStyle.setSummary(null);
                mBatteryBarColor.setSummary(null);
                mBatteryBarChargingAnimation.setSummary(R.string.battery_bar_animate_summary);
                mBatteryBarThickness.setSummary(null);
            }
            return true;

        } else if (preference == mBatteryBarStyle) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_BATTERY_BAR_STYLE, val);

        } else if (preference == mBatteryBarThickness) {

            int val = Integer.parseInt((String) newValue);
            return Settings.System.putInt(mContentRes,
                    Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS, val);

        }
        return false;
    }

}

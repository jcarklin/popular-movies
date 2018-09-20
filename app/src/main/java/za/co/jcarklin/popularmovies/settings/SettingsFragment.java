package za.co.jcarklin.popularmovies.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import za.co.jcarklin.popularmovies.R;

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_movies_browser);
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Preference preference;
        for (int i=0; i<getPreferenceScreen().getPreferenceCount(); i++) {
            preference = getPreferenceScreen().getPreference(i);
            setSummaries(preference);
        }
    }

    private void setSummaries(Preference preference) {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        boolean updateDoubleUpChecboxSummary = false;
        if (preference instanceof CheckBoxPreference) {
            if (preference.getKey()
                    .equals(getString(R.string.double_cols_for_landscape_key))) {
                updateDoubleUpChecboxSummary = true;
            }
        } else {
            String value = sharedPreferences.getString(preference.getKey(),"");
            if (preference instanceof ListPreference) {
                int index = ((ListPreference) preference).findIndexOfValue(value);
                if (index > 0) {
                    preference.setSummary(((ListPreference) preference).getEntries()[index]);
                }
                if (preference.getKey().equals(getString(R.string.pref_num_cols_key))) {
                    updateDoubleUpChecboxSummary = true;
                }
            } else {
                preference.setSummary(value);
            }
        }
        if (updateDoubleUpChecboxSummary) {
            int numCols = Integer.valueOf(sharedPreferences.getString(getString(R.string.pref_num_cols_key),"2"));
            CheckBoxPreference doubleUp = ((CheckBoxPreference)findPreference(getString(R.string.double_cols_for_landscape_key)));
            if (doubleUp.isChecked()) {
                numCols=numCols*2;
            }
            doubleUp.setSummary(getString(R.string.number_of_cols_in_landscape_is)+" "+numCols);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        setSummaries(preference);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

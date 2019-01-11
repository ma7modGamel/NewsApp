package com.example.xxpc.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import static com.example.xxpc.newsapp.SettingsActivity.NewsAppsPreferenceFragment.numItem;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zsettings);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("returnNumItem", numItem);
        setResult(RESULT_OK, returnIntent);

    }


    public static class NewsAppsPreferenceFragment extends PreferenceFragment implements Preference. OnPreferenceChangeListener{

        public static int numItem = 10;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference minMagnitude = findPreference(getString(R.string.settings_min_num_key));
            bindPreferenceSummaryToValue(minMagnitude);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();
            preference.setSummary(stringValue);


            return true;
        }
    }

}

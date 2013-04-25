package info.a7mady911.myCourses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

/**
 * User: A7madY911
 */
public class SettingsUI extends PreferenceActivity {
    private static final String TAG = SettingsUI.class.getSimpleName();
    private Context mCtx = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set pref fragment
        PrefFragment prefFragment = new PrefFragment();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, prefFragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class PrefFragment extends PreferenceFragment implements
            OnPreferenceClickListener, OnPreferenceChangeListener {

        ListPreference mapTypePref;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            // Get the custom preferences with listeners
            Preference feedbackPref = (Preference) findPreference("pref_feedback");
            feedbackPref.setOnPreferenceClickListener(this);
            Preference changelogPref = (Preference) findPreference("pref_changelog");
            changelogPref.setOnPreferenceClickListener(this);
            Preference aboutPref = (Preference) findPreference("pref_about");
            aboutPref.setOnPreferenceClickListener(this);
        }

        //when preference clicked
        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();
            if (key.equalsIgnoreCase("pref_feedback")) {
                SendFeedback();
            } else if (key.equalsIgnoreCase("pref_changelog")) {
                showChangelog();
            } else if (key.equalsIgnoreCase("pref_about")) {
                openAboutUI();
            }
            return false;
        }

        // Open about dialog
        private void openAboutUI() {
            Intent aboutIntent = new Intent(getActivity(), AboutUI.class);
            startActivity(aboutIntent);
        }

        // send feedback via email
        private void SendFeedback() {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL,
                    new String[]{getString(R.string.feedback_email)});
            email.putExtra(Intent.EXTRA_SUBJECT,
                    getString(R.string.feedback_subject));
            email.setType("message/rfc822");
            startActivity(Intent
                    .createChooser(email, "Choose an Email client:"));
        }

        // show Changelog
        public void showChangelog() {
            ChangelogUI ChangelogDialog = new ChangelogUI(getActivity());
            ChangelogDialog.show();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return false;
        }

    }
}
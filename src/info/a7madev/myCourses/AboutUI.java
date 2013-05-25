package info.a7madev.myCourses;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Author: A7maDev
 * Class: About UI
 * Description: Show information about developer, application version and logo
 */
public class AboutUI extends Activity {

    private static final String TAG = AboutUI.class.getSimpleName();
    private TextView appVersionTextView;
    private String appVersionNumber;

    /**
     * Name: onCreate
     * Description: during the activity creation
     *
     * @param savedInstanceState:Bundle
     * @throws PackageManager.NameNotFoundException
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Show About UI layout
        setContentView(R.layout.about_ui);

        // Get and display app version
        try {
            appVersionNumber = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
            appVersionTextView = (TextView) findViewById(R.id.about_versionTextView);
            appVersionTextView.setText("Version " + appVersionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Name: onStop
     * Description: when the activity stops
     */
    @Override
    protected void onStop() {
        super.onStop();
    }
}
package info.a7mady911.myCourses;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.google.analytics.tracking.android.EasyTracker;

/**
 * User: A7madY911
 * Date: 4/14/13
 */
public class AboutUI extends Activity {
    private static final String TAG = AboutUI.class.getSimpleName();
    private Context mCtx = this;
    private TextView appVersion;
    private String appVersionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Google Analytics starts tracking this activity
        try {
            EasyTracker.getInstance().setContext(mCtx);
            EasyTracker.getInstance().activityStart(this);
        } catch (Exception e) {
            Log.d(TAG, "Google Analytics starts: " + e.getMessage());
        }

        // Show About UI layout
        setContentView(R.layout.home_ui);

        // Get App Version
        try {
            appVersionNumber = this.getPackageManager().getPackageInfo(
                    this.getPackageName(), 0).versionName;
            //appVersion = (TextView) findViewById(R.id.About_AppVersion);
            appVersion.setText("Version: " + appVersionNumber);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Google Analytics stops tracking this activity
        try {
            EasyTracker.getInstance().activityStop(this);
        } catch (Exception e) {
            Log.d(TAG, "Google Analytics stops: " + e.getMessage());
        }
    }
}
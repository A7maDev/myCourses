package info.a7madev.myCourses;

import android.app.Application;
import android.util.Log;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * User: A7maDev
 */
@ReportsCrashes(formKey = "a546f55b18f0e1922757dd871a55991a",
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text)
public class MyCoursesApp extends Application {

    private static final String TAG = MyCoursesApp.class.getSimpleName();

    @Override
    public void onCreate() {

        // initial ACRA
        ACRA.init(this);
        ACRA.getErrorReporter().setReportSender(new HockeySender());

        super.onCreate();

        Log.i(TAG, "Application onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "Application terminated");
    }
}

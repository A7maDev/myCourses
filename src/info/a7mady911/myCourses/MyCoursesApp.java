package info.a7mady911.myCourses;

import android.app.Application;
import android.util.Log;

/**
 * User: A7madY911
 * Date: 2/25/13
 */
public class MyCoursesApp extends Application {

    private static final String TAG = MyCoursesApp.class.getSimpleName();

    @Override
    public void onCreate() {
        // initial ACRA
        //ACRA.init(this);
        super.onCreate();
        Log.i(TAG, "Application onCreate");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "Application terminated");
    }
}

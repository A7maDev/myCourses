package info.a7madev.myCourses;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private String[] enrollmentDetails = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set Actionbar title
        getActivity().getActionBar().setTitle("myCourses");

        //declare all text views
        TextView studentTextView = (TextView) getActivity().findViewById(R.id.homefragmentui_student);
        TextView timetableTextView = (TextView) getActivity().findViewById(R.id.homefragmentui_timetable);
        TextView coursesTextView = (TextView) getActivity().findViewById(R.id.homefragmentui_courses);
        TextView acadcalendarTextView = (TextView) getActivity().findViewById(R.id.homefragmentui_acadcalendar);
        TextView usefullinksTextView = (TextView) getActivity().findViewById(R.id.homefragmentui_usefullinks);
        TextView enrollmentTextView = (TextView) getActivity().findViewById(R.id.homefragmentui_enrollmentinfo);

        //set all onClick listeners
        studentTextView.setOnClickListener(this);
        timetableTextView.setOnClickListener(this);
        coursesTextView.setOnClickListener(this);
        acadcalendarTextView.setOnClickListener(this);
        usefullinksTextView.setOnClickListener(this);
        enrollmentTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homefragmentui_student:
                changeFragment(new StudentUI());
                break;
            case R.id.homefragmentui_timetable:
                //changeFragment(new TimetableUI(), "Timetable");
                break;
            case R.id.homefragmentui_courses:
                changeFragment(new CoursesUI());
                break;
            case R.id.homefragmentui_acadcalendar:
                changeFragment(new AcadCalendarUI());
                break;
            case R.id.homefragmentui_usefullinks:
                changeFragment(new UsefulLinksUI());
                break;
            case R.id.homefragmentui_enrollmentinfo:
                changeFragment(new EnrollmentUI());
                break;
        }
    }

    private void changeFragment(Fragment fragment) {
        try {
            getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}

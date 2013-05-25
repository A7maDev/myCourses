package info.a7madev.myCourses;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class EnrollmentUI extends Fragment {

    private static final String TAG = EnrollmentUI.class.getSimpleName();
    private String[] enrollmentDetails = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enroll_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set Actionbar title
        getActivity().getActionBar().setTitle("Enrollment Information");

        enrollmentDetails = getResources().getStringArray(R.array.Enrollment_Details);

        if (enrollmentDetails != null) {
            EditText uniYearTextView = (EditText) getActivity().findViewById(R.id.enrollui_uniYearEditText);
            uniYearTextView.setText(enrollmentDetails[0]);
            EditText uniDateTextView = (EditText) getActivity().findViewById(R.id.enrollui_uniDateEditText);
            uniDateTextView.setText(enrollmentDetails[1]);
            EditText uniRequireTextView = (EditText) getActivity().findViewById(R.id.enrollui_uniRequireEditText);
            uniRequireTextView.setText(enrollmentDetails[2]);
            EditText courseYearTextView = (EditText) getActivity().findViewById(R.id.enrollui_courseYearEditText);
            courseYearTextView.setText(enrollmentDetails[3]);
            EditText courseDateTextView = (EditText) getActivity().findViewById(R.id.enrollui_courseDateEditText);
            courseDateTextView.setText(enrollmentDetails[4]);
        } else {
            sendACRAReport(null);
            getActivity().setContentView(R.layout.empty_ui);
            TextView noDataTextView = (TextView) getActivity().findViewById(R.id.emptyui_noData_textView);
            noDataTextView.setText("no relevant enrollment information!");
        }
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }

}

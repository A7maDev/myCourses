package info.a7madev.myCourses;

import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class CourseDetailsUI extends Fragment implements View.OnClickListener {

    private static final String TAG = CourseDetailsUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String courseCode = null, staffID = null, courseName = null, progCode = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coursedetails_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        //set Actionbar title
        getActivity().getActionBar().setTitle("Course Details");

        //Get data from Bundle
        Bundle extras = this.getArguments();
        if (extras != null) {
            courseCode = extras.getString("Course Code");

            try {
                // Create the database adapter object
                if (HomeUI.dbHelper != null) {
                    dbHelper = HomeUI.dbHelper;
                } else {
                    dbHelper = new DataAdapter(getActivity());
                }
                if (!dbHelper.isDBHelperOpen()) {
                    dbHelper.open();
                }

                cursor = dbHelper.fetchCourseDetailsDataByCode(courseCode);

                //populate data to view
                EditText courseCodeTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_codeEditText);
                EditText courseNameTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_nameEditText);
                EditText courseCreditTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_creditEditText);
                EditText courseLevelTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_levelEditText);
                EditText courseProgTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_progEditText);
                EditText classCRNTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_crnEditText);
                EditText classTutorTextView = (EditText) getActivity().findViewById(R.id.coursedetailsui_tutorEditText);
                ImageView progAction = (ImageView) getActivity().findViewById(R.id.coursedetailsui_progAction);
                ImageView tutorAction = (ImageView) getActivity().findViewById(R.id.coursedetailsui_tutorAction);

                if (cursor.moveToFirst()) {
                    courseCodeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_CODE)));
                    courseName = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_NAME));
                    courseNameTextView.setText(courseName);
                    courseCreditTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_CREDIT)));
                    courseLevelTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_COURSE_Level)));
                    progCode = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_PROG_CODE));
                    courseProgTextView.setText(progCode);
                    classCRNTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_CLASS_CRN)));
                    classTutorTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_NAME)));
                    staffID = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_ID));

                    //set all clickable listeners
                    progAction.setOnClickListener(this);
                    tutorAction.setOnClickListener(this);

                } else {
                    sendACRAReport(null);
                    showEmptyUI("There are no relevant course details");
                }

                //close cursor
                dbHelper.close();

            } catch (SQLException e) {
                sendACRAReport(e);
                showEmptyUI("There are no relevant course details");
            } catch (IllegalArgumentException e) {
                sendACRAReport(e);
                showEmptyUI("There are no relevant course details");
            }

        } else {
            sendACRAReport(null);
            showEmptyUI("There are no relevant course details");
        }
    }

    private void showEmptyUI(String msg) {
        try {
            Fragment fragment = new EmptyFragment();
            Bundle extras = new Bundle();
            extras.putString("NoData Message", msg);
            fragment.setArguments(extras);
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.coursedetailsui_tutorAction:
                showTutor();
                break;
            case R.id.coursedetailsui_progAction:
                showProgramme();
                break;
        }
    }

    private void showProgramme() {
        Fragment fragment = new ProgrammeStaffUI();
        Bundle extras = new Bundle();
        extras.putString("Programme Code", progCode);
        fragment.setArguments(extras);
        getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    private void showTutor() {
        Fragment fragment = new StaffUI();
        Bundle extras = new Bundle();
        extras.putString("Staff ID", staffID);
        fragment.setArguments(extras);
        getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.options_menu_coursedetailsui, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_assessments:
                Fragment fragment = new AssessmentsUI();
                Bundle extras = new Bundle();
                extras.putString("Course Code", courseCode);
                extras.putString("Course Name", courseName);
                fragment.setArguments(extras);
                getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                        .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }

}

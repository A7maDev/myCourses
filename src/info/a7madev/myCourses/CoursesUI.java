package info.a7madev.myCourses;

import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class CoursesUI extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = CoursesUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String[] listOfCourses;
    private ListView listView;
    private SimpleCursorAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.courses_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set Actionbar title
        getActivity().getActionBar().setTitle("Courses");

        try {

            listView = (ListView) getActivity().findViewById(R.id.coursesui_listView);

            // Create the database adapter object
            if (HomeUI.dbHelper != null) {
                dbHelper = HomeUI.dbHelper;
            } else {
                dbHelper = new DataAdapter(getActivity());
            }
            if (!dbHelper.isDBHelperOpen()) {
                dbHelper.open();
            }

            cursor = dbHelper.fetchAllCoursesData();

            if (cursor.getCount() != 0) {

                listOfCourses = new String[]{DataAdapter.KEY_COURSE_CODE, DataAdapter.KEY_COURSE_NAME};
                int[] to = new int[]{android.R.id.text1, android.R.id.text2};

                mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_2,
                        cursor, listOfCourses, to, 0);
                listView.setOnItemClickListener(this);
                listView.setAdapter(mAdapter);
            } else {
                sendACRAReport(null);
                showEmptyUI("no relevant courses found!");
            }

            //close the cursor
            dbHelper.close();

        } catch (SQLException e) {
            sendACRAReport(e);
            showEmptyUI("no relevant courses found!");
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
    public void onStop() {
        super.onStop();

        // Close cursor
        try {
            dbHelper.close();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor selectedItemCursor = (Cursor) mAdapter.getItem(position);
        if (selectedItemCursor != null) {
            String selectedString = selectedItemCursor.getString(selectedItemCursor.getColumnIndex(DataAdapter.KEY_COURSE_CODE));
            Fragment fragment = new CourseDetailsUI();
            Bundle extras = new Bundle();
            extras.putString("Course Code", selectedString);
            fragment.setArguments(extras);
            getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } else {
            sendACRAReport(null);
            Toast.makeText(view.getContext(), "Not able to get the selected item", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}
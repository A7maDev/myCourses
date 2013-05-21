package info.a7mady911.myCourses;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * User: A7madY911
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
        View rootView = inflater.inflate(R.layout.courses_ui, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getActivity().findViewById(R.id.coursesui_listView);

        // Create the adapter object
        if (((HomeUI) getActivity()).getDataAdapter() != null) {
            dbHelper = ((HomeUI) getActivity()).getDataAdapter();
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
            getActivity().setContentView(R.layout.empty_ui);
        }

        //close the cursor
        dbHelper.close();
    }


    @Override
    public void onStop() {
        super.onStop();

        // Close cursor
        try {
            dbHelper.close();
        } catch (Exception e) {
            Log.e(TAG, "Close cursor: " + e.toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor selectedItemCursor = (Cursor) mAdapter.getItem(position);
        String selectedString = null;
        if (selectedItemCursor != null) {
            selectedString = selectedItemCursor.getString(selectedItemCursor.getColumnIndex(DataAdapter.KEY_COURSE_CODE));
            Intent courseDetailsUIIntent = new Intent(getActivity(), CourseDetailsUI.class);
            courseDetailsUIIntent.putExtra("Course Code", selectedString);
            startActivity(courseDetailsUIIntent);
        } else {
            Toast.makeText(view.getContext(), "Not able to get the selected item", Toast.LENGTH_SHORT).show();
        }

    }

}
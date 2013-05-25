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
public class ProgrammeStaffUI extends Fragment implements AdapterView.OnItemClickListener {

    private static final String TAG = ProgrammeStaffUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private SimpleCursorAdapter mAdapter;
    private String progCode = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.programmestaff_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get data from Bundle
        Bundle extras = this.getArguments();
        if (extras != null) {
            progCode = extras.getString("Programme Code");

            //set Actionbar title
            getActivity().getActionBar().setTitle(progCode + " Tutors List");

            try {

                ListView listView = (ListView) getActivity().findViewById(R.id.programmeui_listView);

                // Create the database adapter object
                if (HomeUI.dbHelper != null) {
                    dbHelper = HomeUI.dbHelper;
                } else {
                    dbHelper = new DataAdapter(getActivity());
                }
                if (!dbHelper.isDBHelperOpen()) {
                    dbHelper.open();
                }

                Cursor cursor = dbHelper.fetchStaffListDataByProgCode(progCode);

                if (cursor.getCount() != 0) {

                    String[] listOfAssessments = new String[]{DataAdapter.KEY_STAFF_NAME};
                    int[] to = new int[]{android.R.id.text1};

                    mAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor, listOfAssessments, to, 0);
                    listView.setOnItemClickListener(this);
                    listView.setAdapter(mAdapter);
                } else {
                    sendACRAReport(null);
                    showEmptyUI("No relevant tutors list for this programme");
                }
            } catch (SQLException e) {
                sendACRAReport(e);
                showEmptyUI("No relevant tutors list for this programme");
            }

        } else {
            sendACRAReport(null);
            showEmptyUI("No relevant tutors list for this programme");
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Cursor selectedItemCursor = (Cursor) mAdapter.getItem(position);
        if (selectedItemCursor != null) {
            String selectedString = selectedItemCursor.getString(selectedItemCursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_ID));
            Fragment fragment = new StaffUI();
            Bundle extras = new Bundle();
            extras.putString("Staff ID", selectedString);
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
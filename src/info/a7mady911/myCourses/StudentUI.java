package info.a7mady911.myCourses;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * User: A7madY911
 */
public class StudentUI extends Fragment {

    private static final String TAG = StudentUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String staffID = null, progCode = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.student_ui, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create the adapter object
        if (((HomeUI) getActivity()).getDataAdapter() != null) {
            dbHelper = ((HomeUI) getActivity()).getDataAdapter();
        } else {
            dbHelper = new DataAdapter(getActivity());
        }
        if (!dbHelper.isDBHelperOpen()) {
            dbHelper.open();
        }
        cursor = dbHelper.fetchStudentData();

        //populate data to view
        TextView studentIDTextView = (TextView) getActivity().findViewById(R.id.studentui_idTextView);
        TextView studentNameTextView = (TextView) getActivity().findViewById(R.id.studentui_nameTextView);
        TextView studentEmailTextView = (TextView) getActivity().findViewById(R.id.studentui_emailTextView);
        TextView studentMentorTextView = (TextView) getActivity().findViewById(R.id.studentui_mentorTextView);
        TextView studentProgrammeTextView = (TextView) getActivity().findViewById(R.id.studentui_progTextView);
        if (cursor.moveToFirst()) {
            studentIDTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STUDENT_ID)));
            studentNameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STUDENT_NAME)));
            studentEmailTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STUDENT_EMAIL)));
            studentMentorTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_NAME)));
            progCode = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_PROG_CODE));
            studentProgrammeTextView.setText(progCode);
            staffID = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_ID));

            //Mentor text view clickable
            studentMentorTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mentorUIIntent = new Intent(getActivity(), StaffUI.class);
                    mentorUIIntent.putExtra("Staff ID", staffID);
                    startActivity(mentorUIIntent);
                }
            });

            //Programme text view clickable
            studentProgrammeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent programmeUIIntent = new Intent(getActivity(), ProgrammeUI.class);
                    programmeUIIntent.putExtra("Programme Code", progCode);
                    startActivity(programmeUIIntent);
                }
            });


        } else {
            Log.d(TAG, "Error populating data from database");
            getActivity().setContentView(R.layout.empty_ui);
        }

        //close cursor
        dbHelper.close();
    }
}

package info.a7madev.myCourses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class StaffUI extends Fragment implements View.OnClickListener {

    private static final String TAG = StaffUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String staffID = null, staffPhone = null, staffName = null, staffEmail = null, staffProgramme = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.staff_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set Actionbar title
        getActivity().getActionBar().setTitle("Staff Details");

        //Get data from Bundle
        Bundle extras = this.getArguments();
        if (extras != null) {
            staffID = extras.getString("Staff ID");

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

                //fetch data
                cursor = dbHelper.fetchStaffDetailsDataByID(staffID);

                //populate data to view
                EditText staffNameTextView = (EditText) getActivity().findViewById(R.id.staffui_nameEditText);
                EditText staffEmailTextView = (EditText) getActivity().findViewById(R.id.staffui_emailEditText);
                EditText staffOfficeTextView = (EditText) getActivity().findViewById(R.id.staffui_officeEditText);
                EditText staffPhoneTextView = (EditText) getActivity().findViewById(R.id.staffui_phoneEditText);
                EditText staffProgTextView = (EditText) getActivity().findViewById(R.id.staffui_progEditText);
                ImageView nameAction = (ImageView) getActivity().findViewById(R.id.staffui_nameAction);
                ImageView emailAction = (ImageView) getActivity().findViewById(R.id.staffui_emailAction);
                ImageView phoneAction = (ImageView) getActivity().findViewById(R.id.staffui_phoneAction);
                ImageView progAction = (ImageView) getActivity().findViewById(R.id.staffui_progAction);

                if (cursor != null) {

                    cursor.moveToFirst();

                    staffName = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_NAME));
                    staffEmail = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_EMAIL));
                    staffPhone = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_PHONE));
                    staffProgramme = cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_PROG_CODE));

                    staffNameTextView.setText(staffName);
                    staffEmailTextView.setText(staffEmail);
                    staffOfficeTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DataAdapter.KEY_STAFF_OFFICE)));
                    staffPhoneTextView.setText(staffPhone);
                    staffProgTextView.setText(staffProgramme);

                    //set all click listeners
                    nameAction.setOnClickListener(this);
                    emailAction.setOnClickListener(this);
                    phoneAction.setOnClickListener(this);
                    progAction.setOnClickListener(this);

                } else {
                    sendACRAReport(null);
                    showEmptyUI("no relevant staff details found!");
                }

                //close cursor
                dbHelper.close();

            } catch (SQLException e) {
                sendACRAReport(e);
                showEmptyUI("no relevant staff details found!");
            } catch (IllegalArgumentException e) {
                sendACRAReport(e);
                showEmptyUI("no relevant staff details found!");
            }

        } else {
            sendACRAReport(null);
            showEmptyUI("no relevant staff details found!");
        }
    }

    private void showEmptyUI(String msg) {
        try {
            getActivity().setContentView(R.layout.empty_ui);
            TextView noDataTextView = (TextView) getActivity().findViewById(R.id.emptyui_noData_textView);
            noDataTextView.setText(msg);
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.staffui_nameAction:
                copyNameToClipboard();
                break;
            case R.id.staffui_emailAction:
                emailStaff();
                break;
            case R.id.staffui_phoneAction:
                callStaff();
                break;
            case R.id.staffui_progAction:
                showProgramme();
                break;
        }
    }

    private void copyNameToClipboard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("myCourses Tutor Name", staffName);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Copy to Clipboard", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void showProgramme() {
        try {
            Fragment fragment = new ProgrammeStaffUI();
            Bundle extras = new Bundle();
            extras.putString("Programme Code", staffProgramme);
            fragment.setArguments(extras);
            getActivity().getFragmentManager().beginTransaction().setCustomAnimations(
                    R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                    R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                    .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void callStaff() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Call Confirmation")
                .setMessage("Would you like to call " + staffName + "?")
                .setPositiveButton(R.string.text_call,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                try {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" + staffPhone));
                                    startActivity(callIntent);
                                } catch (ActivityNotFoundException e) {
                                    sendACRAReport(e);
                                }
                            }
                        })
                .setNegativeButton(R.string.text_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.dismiss();
                            }
                        });
        builder.create().show();
    }

    private void emailStaff() {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL,
                new String[]{staffEmail});
        email.setType("message/rfc822");
        try {
            startActivity(Intent
                    .createChooser(email, "Choose an Email client:"));
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}

package info.a7mady911.myCourses;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * User: A7madY911
 */
public class StaffUI extends Activity {

    private static final String TAG = StaffUI.class.getSimpleName();
    public static DataAdapter dbHelper;
    private Cursor cursor = null;
    private String staffID = null, staffPhone = null, staffName = null, staffEmail = null, staffProgramme = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_ui);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setTitle("Staff Details");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Get data from Bundle
        Bundle extras = getIntent().getExtras();
        if (!extras.isEmpty()) {
            staffID = extras.getString("Staff ID");

            // Create the adapter object
            if (HomeUI.getDataAdapter() != null) {
                dbHelper = HomeUI.getDataAdapter();
            } else {
                dbHelper = new DataAdapter(this);
            }
            if (!dbHelper.isDBHelperOpen()) {
                dbHelper.open();
            }
            cursor = dbHelper.fetchStaffDetailsDataByID(staffID);

            //populate data to view
            TextView staffNameTextView = (TextView) findViewById(R.id.staffui_nameTextView);
            TextView staffEmailTextView = (TextView) findViewById(R.id.staffui_emailTextView);
            TextView staffOfficeTextView = (TextView) findViewById(R.id.staffui_officeTextView);
            TextView staffPhoneTextView = (TextView) findViewById(R.id.staffui_phoneTextView);
            TextView staffProgTextView = (TextView) findViewById(R.id.staffui_progTextView);
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
                staffPhoneTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callStaff();
                    }
                });
                staffEmailTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        emailStaff();
                    }
                });
                staffProgTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent programmeUIIntent = new Intent(StaffUI.this, ProgrammeUI.class);
                        programmeUIIntent.putExtra("Programme Code", staffProgramme);
                        startActivity(programmeUIIntent);
                        StaffUI.this.finish();
                    }
                });
            } else {
                Log.d(TAG, "Error populating data from database");
                setContentView(R.layout.empty_ui);
            }

            //close cursor
            dbHelper.close();

        } else {
            Log.d(TAG, "Error populating data from database");
            setContentView(R.layout.empty_ui);
        }
    }

    private void callStaff() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
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
                                    Log.e(TAG, "Call failed", e);
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
        startActivity(Intent
                .createChooser(email, "Choose an Email client:"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

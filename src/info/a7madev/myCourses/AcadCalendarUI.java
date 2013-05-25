package info.a7madev.myCourses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.acra.ACRA;

import java.io.*;

/**
 * User: A7maDev
 */
public class AcadCalendarUI extends Fragment implements View.OnClickListener {

    private static final String TAG = AcadCalendarUI.class.getSimpleName();
    private File pdfFile, downloadsDirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    private String pdfURL = "http://www.polytechnic.bh/media/Reg/wall-planner-12-13-oct2012.pdf",
            pdfFileWithPath = downloadsDirPath + "/acadcal1213.pdf";
    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.acadcalendar_ui, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set Actionbar title
        getActivity().getActionBar().setTitle("Academic Calendar");

        pdfFile = new File(pdfFileWithPath);

        if (!pdfFile.exists()) {
            new getPDFFileAsyncTask().execute();
        }

        //declare all text views
        TextView viewReaderTextView = (TextView) getActivity().findViewById(R.id.acadcalendarui_viewreader);
        TextView viewBrowserTextView = (TextView) getActivity().findViewById(R.id.acadcalendarui_viewbrowser);
        TextView sharePDFTextView = (TextView) getActivity().findViewById(R.id.acadcalendarui_sharepdf);
        TextView shareLinkTextView = (TextView) getActivity().findViewById(R.id.acadcalendarui_sharelink);

        //set all onClick listeners
        viewReaderTextView.setOnClickListener(this);
        viewBrowserTextView.setOnClickListener(this);
        sharePDFTextView.setOnClickListener(this);
        shareLinkTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acadcalendarui_viewreader:
                viewInPDFReader();
                break;
            case R.id.acadcalendarui_viewbrowser:
                viewInBrowser();
                break;
            case R.id.acadcalendarui_sharepdf:
                sharePDF();
                break;
            case R.id.acadcalendarui_sharelink:
                shareURL();
                break;
        }
    }

    private class getPDFFileAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                copyPDF();
            } catch (IOException e) {
                sendACRAReport(e);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
            progressDialog.setCancelable(false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
    }

    private void shareURL() {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Bahrain Polytechnic Academic Calendar 2012 - 2013 " + pdfURL);
        try {
            startActivity(Intent.createChooser(shareIntent, "Share Link via"));
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void sharePDF() {
        if (pdfFile.exists()) {
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfFile));
            try {
                startActivity(Intent.createChooser(shareIntent, "Share PDF File via"));
            } catch (Exception e) {
                sendACRAReport(e);
            }
        } else {
            showToast("Sorry, I can't find the PDF file!");
        }
    }

    private void viewInBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL));
        try {
            startActivity(browserIntent);
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    private void viewInPDFReader() {
        if (pdfFile.exists()) {
            Uri path = Uri.fromFile(pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                sendACRAReport(e);
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to download Adobe PDF Reader from Google Play?")
                        .setPositiveButton(R.string.text_yes,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                        marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
                                        try {
                                            startActivity(marketIntent);
                                        } catch (Exception e1) {
                                            sendACRAReport(e1);
                                        }

                                    }
                                })
                        .setNegativeButton(R.string.text_no,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                    }
                                });
                builder.create().show();
            }
        } else {
            sendACRAReport(null);
            showToast("Sorry, I can't find the PDF file!");
        }
    }

    private void showToast(String toastMsg) {
        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_SHORT).show();
    }

    private void copyPDF() throws IOException {

        InputStream myInput = getResources().openRawResource(R.raw.acadcal1213);

        if (!downloadsDirPath.exists()) {
            pdfFileWithPath = Environment.getExternalStorageDirectory() + "/acadcal1213.pdf";
        }

        OutputStream myOutput = new FileOutputStream(pdfFileWithPath);

        // transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}

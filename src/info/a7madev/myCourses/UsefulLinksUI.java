package info.a7madev.myCourses;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.acra.ACRA;

/**
 * User: A7maDev
 */
public class UsefulLinksUI extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = UsefulLinksUI.class.getSimpleName();
    private String[] linksTitlesList, linksURLsList;
    private ProgressDialog progressDialog;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        //Change Actionbar title
        getActivity().getActionBar().setTitle("Useful Links");

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...", true);
        progressDialog.setCancelable(false);

        new getListAsyncTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.options_menu_usefullinksui, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                progressDialog.show();
                new getListAsyncTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linksURLsList[position]));
        try {
            startActivity(browserIntent);
        } catch (Exception e) {
            sendACRAReport(e);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, linksTitlesList[position]);
        shareIntent.putExtra(Intent.EXTRA_TEXT, linksURLsList[position]);
        try {
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception e) {
            sendACRAReport(e);
        }
        return false;
    }


    private class getListAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            listView = (ListView) getActivity().findViewById(R.id.listView);

            if (linksTitlesList == null && linksURLsList == null) {
                linksTitlesList = getResources().getStringArray(R.array.UsefulLinks_Title);
                linksURLsList = getResources().getStringArray(R.array.UsefulLinks_URL);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showList();
            progressDialog.dismiss();
        }
    }

    private void showList() {
        //validate links list
        if (linksTitlesList.length != 0) {
            //set adapter list layout
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.usefullinks_row, R.id.usefullink_linkTextView, linksTitlesList);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
            listView.setAdapter(adapter);
        } else {
            sendACRAReport(null);
            showEmptyUI("no relevant Useful Links list found!");
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

    private void sendACRAReport(Exception caughtException) {
        ACRA.getErrorReporter().handleSilentException(caughtException);
    }
}
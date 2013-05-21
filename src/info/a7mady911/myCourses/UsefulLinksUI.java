package info.a7mady911.myCourses;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * User: A7madY911
 */
public class UsefulLinksUI extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = UsefulLinksUI.class.getSimpleName();
    private String[] linksTitlesList, linksURLsList;
    private ProgressDialog progressDialog;
    private ListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_view);

        //Actionbar
        ActionBar actionBar = getActionBar();
        actionBar.show();
        actionBar.setTitle("Useful Links");
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog = ProgressDialog.show(this, "", "Loading...", true);
        progressDialog.setCancelable(false);

        new getListAsyncTask().execute();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_refresh:
                progressDialog.show();
                new getListAsyncTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linksURLsList[position]));
        startActivity(browserIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, linksTitlesList[position]);
        shareIntent.putExtra(Intent.EXTRA_TEXT, linksURLsList[position]);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
        return false;
    }


    private class getListAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            listView = (ListView) findViewById(R.id.listView);

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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.usefullinks_row, R.id.usefullink_linkTextView, linksTitlesList);
            listView.setOnItemClickListener(this);
            listView.setOnItemLongClickListener(this);
            listView.setAdapter(adapter);
        } else {
            setContentView(R.layout.empty_ui);
        }
    }
}
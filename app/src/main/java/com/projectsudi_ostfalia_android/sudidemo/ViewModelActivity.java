package com.projectsudi_ostfalia_android.sudidemo;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewModelActivity extends AppCompatActivity {

    // Variables for the search input field, and results TextViews.
    private EditText mBookInput;
    private TextView mErrorText;
    private ListView lv;
    private List<Book> listbook = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);

        // Initialize all the view variables.
        mBookInput = (EditText)findViewById(R.id.bookInput);
        mErrorText = (TextView)findViewById(R.id.errorText);
        lv = (ListView) findViewById(R.id._listBook);

        ViewModelViewModel usersViewModel =  ViewModelProviders.of(this).get(ViewModelViewModel.class);
        mErrorText.setText(usersViewModel.Error());

        listbook = usersViewModel.getBook();
        if(listbook != null)
        {
            String[] list = new String[listbook.size()];
            for(int i = 0; i< listbook.size(); i++)
            {
                list[i] = listbook.get(i).title;
            }


            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
            lv.setAdapter(arrayAdapter);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    /**
     * Gets called when the user pushes the "Search Books" button
     *
     * @param view The view (Button) that was clicked.
     */
    public void searchBooks(View view) throws ExecutionException, InterruptedException {
        // Get the search string from the input field.
        String queryString = mBookInput.getText().toString();

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If the network is active and the search field is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected() && queryString.length()!=0) {

            ViewModelViewModel usersViewModel =  ViewModelProviders.of(this).get(ViewModelViewModel.class);
            usersViewModel.searchBook(mBookInput.getText().toString());
            mErrorText.setText(usersViewModel.Error());

            listbook = usersViewModel.getBook();

            String[] list = new String[listbook.size()];
            for(int i = 0; i< listbook.size(); i++)
            {
                list[i] = listbook.get(i).title;
            }

            ListView lv = (ListView) findViewById(R.id._listBook);
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
            lv.setAdapter(arrayAdapter);

        }
        // Otherwise update the TextView to tell the user there is no connection or no search term.
        else {
            if (queryString.length() == 0) {
                mErrorText.setText(R.string.no_search_term);
            } else {
                mErrorText.setText(R.string.no_network);
            }
        }
    }


}

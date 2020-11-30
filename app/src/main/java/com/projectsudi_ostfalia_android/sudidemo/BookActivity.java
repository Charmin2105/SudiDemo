package com.projectsudi_ostfalia_android.sudidemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class BookActivity extends AppCompatActivity{



        // Variables for the search input field, and results TextViews.
        private EditText mBookInput;
        private TextView mTitleText;
        private TextView mAuthorText;


        /**
         * Initializes the activity.
         *
         * @param savedInstanceState The current state data
         */
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_book);

            // Initialize all the view variables.
            mBookInput = (EditText)findViewById(R.id.bookInput);
            mTitleText = (TextView)findViewById(R.id.titleText);
            mAuthorText = (TextView)findViewById(R.id.authorText);
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
                String test = new FetchBookActivity().execute(queryString).get();
                TEST(test);
            }
            // Otherwise update the TextView to tell the user there is no connection or no search term.
            else {
                if (queryString.length() == 0) {
                    mAuthorText.setText("");
                    mTitleText.setText(R.string.no_search_term);
                } else {
                    mAuthorText.setText("");
                    mTitleText.setText(R.string.no_network);
                }
            }
        }

        public void TEST(String s)
        {
            try {
                // Convert the response into a JSON object.
                JSONObject jsonObject = new JSONObject(s);
                // Get the JSONArray of book items.
                JSONArray itemsArray = jsonObject.getJSONArray("items");

                // Initialize iterator and results fields.
                int i = 0;
                String title = null;
                String authors = null;

                // Look for results in the items array, exiting when both the title and author
                // are found or when all items have been checked.
                while (i < itemsArray.length() || (authors == null && title == null)) {
                    // Get the current item information.
                    JSONObject book = itemsArray.getJSONObject(i);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    // Try to get the author and title from the current item,
                    // catch if either field is empty and move on.
                    try {
                        title = volumeInfo.getString("title");
                        authors = volumeInfo.getString("authors");
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    // Move to the next item.
                    i++;
                }

                // If both are found, display the result.
                if (title != null && authors != null){
                    mTitleText.setText(title);
                    mAuthorText.setText(authors);
                    mBookInput.setText("");
                } else {
                    // If none are found, update the UI to show failed results.
                    mTitleText.setText(R.string.no_results);
                    mAuthorText.setText("");
                }

            } catch (Exception e){
                // If onPostExecute does not receive a proper JSON string,
                // update the UI to show failed results.
                mTitleText.setText(R.string.no_results);
                mAuthorText.setText("");
                e.printStackTrace();
            }
        }

}

package com.projectsudi_ostfalia_android.sudidemo;


import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
     * AsyncTask implementation that opens a network connection and
     * query's the Book Service API.
     */
    public class FetchBookActivity extends AsyncTask<String,Void,String> {

        // Class name for Log tag
        private static final String LOG_TAG = FetchBookActivity.class.getSimpleName();

        // Constructor providing a reference to the views in MainActivity
        public FetchBookActivity() {

        }


        /**
         * Makes the Books API call off of the UI thread.
         *
         * @param params String array containing the search data.
         * @return Returns the JSON string from the Books API or
         *         null if the connection failed.
         */
        @Override
        protected String doInBackground(String... params) {

            // Get the search string
            String queryString = params[0];


            // Set up variables for the try block that need to be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String bookJSONString = null;

            // Attempt to query the Books API.
            try {
                // Base URI for the Books API.
                final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

                final String QUERY_PARAM = "q"; // Parameter for the search string.
                final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
                final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

                // Build up your query URI, limiting results to 10 items and printed books.
                Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, queryString)
                        .appendQueryParameter(MAX_RESULTS, "10")
                        .appendQueryParameter(PRINT_TYPE, "books")
                        .build();

                URL requestURL = new URL(builtURI.toString());

                // Open the network connection.
                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Get the InputStream.
                InputStream inputStream = urlConnection.getInputStream();

                // Read the response string into a StringBuilder.
                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                    builder.append(line + "\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // return null;
                    return null;
                }
                bookJSONString = builder.toString();

                // Catch errors.
            } catch (IOException e) {
                e.printStackTrace();

                // Close the connections.
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            // Return the raw response.
            return bookJSONString;
        }

        /**
         * Handles the results on the UI thread. Gets the information from
         * the JSON and updates the Views.
         *
         * @param s Result from the doInBackground method containing the raw JSON response,
         *          or null if it failed.
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


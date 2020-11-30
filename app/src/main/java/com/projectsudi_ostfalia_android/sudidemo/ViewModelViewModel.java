package com.projectsudi_ostfalia_android.sudidemo;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ViewModelViewModel extends ViewModel {

    private String mError = "";
    private String mJsonString = "";

    private List<Book> listBook;

    public List<Book> getBook()
    {
        return listBook;
    }

    public String Error() {
        return mError;
    }


    public void searchBook(String query) {

        try {
            mJsonString = new FetchBookActivity().execute(query).get();
            jsonRead(mJsonString);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public void jsonRead(String s) {
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;
            listBook = new ArrayList<Book>();

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            while (true) {
                // Get the current item information.

                JSONObject book;
                JSONObject volumeInfo;

                try
                {
                    book = itemsArray.getJSONObject(i);
                    volumeInfo = book.getJSONObject("volumeInfo");
                }
                catch (Exception e)
                {
                    break;
                }

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {

                    Book books = new Book();
                    books.title = volumeInfo.getString("title");
                    books.authors = volumeInfo.getString("authors");
                    listBook.add(books);

                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (title != null && authors != null) {
            } else {
                // If none are found, update the UI to show failed results.
                //mErrorText.setText(R.string.no_results);
                mError = "Buch nicht gefunden";
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.

            mError = "Buch nicht gefunden";

            //mErrorText.setText(R.string.no_results);
            //mAuthorText.setText("");
            e.printStackTrace();
        }
    }


}

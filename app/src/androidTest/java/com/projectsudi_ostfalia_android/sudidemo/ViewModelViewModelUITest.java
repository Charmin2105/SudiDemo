package com.projectsudi_ostfalia_android.sudidemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.ListView;
import android.app.Activity;


import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.*;


@RunWith(AndroidJUnit4.class)
public class ViewModelViewModelUITest {
    final String titel = "Kurze Antworten auf große Fragen";
    final String TAG = "TAG";


    @Rule
    //Festlegung der Regeln zum Starten der Activity
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void searchBook() {


        // Starten der ViewModelViewModel Activity
        //onView(withId(R.id.demo10)).perform(click());
        Log.d(TAG, "1");
        //Suchtitel eingabe
        onView(withId(R.id.bookInput)).perform(typeText(titel))
                .check(matches(withText(containsString(titel))));
        Log.d(TAG, "2");
        closeSoftKeyboard();
        onView(withId(R.id.searchButton)).perform(click());
    }
    @After
    public void ItemCheckTest(){

        //final ListView listBook = new ListView()
       // int position = 1;
        //listBook.performItemClick(listBook.getChildAt(position), position, listBook.getAdapter().getItemId(position));

    }

}
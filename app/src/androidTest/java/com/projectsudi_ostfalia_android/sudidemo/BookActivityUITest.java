package com.projectsudi_ostfalia_android.sudidemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

@RunWith(AndroidJUnit4.class)
public class BookActivityUITest {
    //Testvariabeln
    final String titel = "Kurze Antworten auf große Fragen";
    final String author = "Stephen Hawking";
    final String falsAuthor = "Dennis Welzer";

    @Rule
    //Festlegung der Regeln zum Starten der Activity
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new  ActivityTestRule<>(MainActivity.class);

    @Test
    public void BookSearch(){
        //Öffnen der Books Activity
        onView(withId(R.id.demo9)).perform(click());

        //Titeleingabe
        onView(withId(R.id.bookInput)).perform(typeText(titel))
                .check(matches(withText(containsString(titel))));
        closeSoftKeyboard();

        //Suche starten
        onView(withId(R.id.searchButton)).perform(click());

        // Falsches Ergebis Check
        onView(withId(R.id.authorText)).check(matches(not(withText(containsString(falsAuthor)))));

        // Erwartetes Ergebnis Check
        onView(withId(R.id.titleText)).check(matches(withText(containsString(titel))));
        onView(withId(R.id.authorText)).check(matches(withText(containsString(author))));

    }
}
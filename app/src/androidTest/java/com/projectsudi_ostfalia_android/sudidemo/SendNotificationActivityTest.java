package com.projectsudi_ostfalia_android.sudidemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.containsString;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

@RunWith(AndroidJUnit4.class)
public class SendNotificationActivityTest {
    //Variablen
    final String name = "SudiDemo";
    final String titel = "Dies ist ein toller Text";
    final String nachricht = "Ein Test der SudiDemo App ";

    @Rule
    //Festlegung der Regeln zum Starten der Activity
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new  ActivityTestRule<>(MainActivity.class);

    @Test
    public void SendNotification(){
        //Notification Demo starten
        onView(withId(R.id.demo4)).perform(click());

        //Texteingaben
        onView(withId(R.id.editText_name)).perform(typeText(name ))
                .check(matches(withText(containsString(name))));
        onView(withId(R.id.editText_titel)).perform(typeText(titel))
                .check(matches(withText(containsString(titel))));
        onView(withId(R.id.editText_nachricht)).perform(typeText(nachricht))
                .check(matches(withText(containsString(nachricht))));
        closeSoftKeyboard();

        onView(withId(R.id.button_senden)).perform(click());

    }

}
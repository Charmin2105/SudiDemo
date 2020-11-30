package com.projectsudi_ostfalia_android.sudidemo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.*;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {


    @Rule
    //Festlegung der Regeln zum Starten der Activity
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new  ActivityTestRule<>(MainActivity.class);

    @Test
    public void MainUITest(){

        //Starten aller Demos in der Main Activity
        onView(withId(R.id.demo1)).perform(click());
        pressBack();
        onView(withId(R.id.demo2)).perform(click());
        pressBack();
        onView(withId(R.id.demo3)).perform(click());
        pressBack();
        onView(withId(R.id.demo4)).perform(click());
        pressBack();
        onView(withId(R.id.demo5)).perform(click());
        pressBack();
        onView(withId(R.id.demo6)).perform(click());
        pressBack();
        onView(withId(R.id.demo7)).perform(click());
        pressBack();
        onView(withId(R.id.demo8)).perform(click());
        pressBack();
        onView(withId(R.id.demo9)).perform(click());
        pressBack();
        onView(withId(R.id.demo10)).perform(click());
        pressBack();

    }
}
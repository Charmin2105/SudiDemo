package com.projectsudi_ostfalia_android.sudidemo;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.ViewAction.*;
import  static android.support.test.espresso.ViewInteraction.*;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;

@RunWith(AndroidJUnit4.class)
public class FirebaseAccountCreationTest {

    //Mustereingaben für einen Account
    final String mail ="TestMail@test.com";
    final String password = "Password";

    @Rule
    //Festlegung der Regeln zum Starten der Activity
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new  ActivityTestRule<>(MainActivity.class);

    @Test
    public void CreateNewAccount(){
        //Button 5 (Firebase) wird gedrückt
        onView(withId(R.id.demo5)).perform(click());

        //In der EinloggenActivity wird der Button zum Registrieren gedrückt
        onView(withId(R.id.buttonLoginRegistrieren)).perform(click());

        //Eingabe einer Mailadresse sowie dem Überprüfen ob die Eingabe mit dem Wert übereinstimmt
        onView(withId(R.id.editTextRegistryMail)).perform(typeText(mail ))
                .check(matches(withText(containsString(mail))));

        //Eingabe eines Passworts und dem schließen der Tastatur
        onView(withId(R.id.editTextRegistryPassword)).perform(typeText(password),
                closeSoftKeyboard());

        //Der RegistreirenButton wird gedrückt
        onView(withId(R.id.buttonRegistry)).perform(click());

        //Um zurück in den Login zu gelangen
        pressBack();

    }
    @After
    public void LoginAccount()
    {
        //Eingabe der Mailadresse in der LoginActivity
        onView(withId(R.id.editTextLoginMail)).perform(typeText(mail))
                .check(matches(withText(containsString(mail))));

        //Eingabe des Passwortes in der LoginActivity
        onView(withId(R.id.editTextLoginPassword)).perform(typeText(password),closeSoftKeyboard());

        //Button für den Login wird gedrückt
        onView(withId(R.id.buttonLoginEinloggen)).perform(click());


    }

}
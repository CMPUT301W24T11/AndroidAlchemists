package com.example.qrcheckin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateAndUpdateProfile {

    @Rule
    public ActivityScenarioRule<ProfileActivity> activityScenarioRule = new ActivityScenarioRule<ProfileActivity>(ProfileActivity.class);

    @Test
    public void testEditButton() {

        String name = "Rupam";
        String contact = "5877789979";
        String homepage = "SkilledRupam.com";


        // Check if the show_profile is displayed
        onView(withId(R.id.showProfile)).check(matches(isDisplayed()));

        // Click the edit button to pop up a fragment
        onView(allOf(withId(R.id.edit_profile), isDescendantOfA(withId(R.id.profileToolbar)))).perform(click());

        // Test the text boxes
        onView(withId(R.id.edit_name)).perform(ViewActions.typeText(name));
        onView(withId(R.id.edit_contact)).perform(ViewActions.typeText(contact));
        onView(withId(R.id.edit_homepage)).perform(ViewActions.typeText(homepage));

        // Perform a click action on the "Edit" button
        onView(withText("EDIT")).perform(click());

        // matching if the changes appear correctly
        onView(withId(R.id.profileName1)).check(matches(withText(name)));
        onView(withId(R.id.contact1)).check(matches(withText(contact)));
        onView(withId(R.id.homepage1)).check(matches(withText(homepage)));

        // Perform a click action to enable the switch
//        onView(withId(R.id.geoswitch)).perform(click());

        // Close the app
        activityScenarioRule.getScenario().close();

        // Reopen the app
        ActivityScenario<ProfileActivity> scenario = ActivityScenario.launch(ProfileActivity.class);

        // matching if the changes still appear correctly
        onView(withId(R.id.profileName1)).check(matches(withText(name)));
        onView(withId(R.id.contact1)).check(matches(withText(contact)));
        onView(withId(R.id.homepage1)).check(matches(withText(homepage)));

        // Perform a click action on the 'Update Picture' button
        onView(withId(R.id.btnUpdatePicture)).perform(click());

        // Check if the dialog is displayed
        onView(withId(R.id.updatePictureFragment)).check(matches(isDisplayed()));

        // Click on the "Choose from Gallery" option
        onView(withId(R.id.gallery)).perform(click());

        // Check if the intent to pick an image from the gallery is sent
//        intended(allOf(
//                hasAction(Intent.ACTION_GET_CONTENT),
//                hasType("image/*")
//        ));
    }
}
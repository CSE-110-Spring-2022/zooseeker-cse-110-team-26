package com.example.zooapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.anything;
import static java.lang.Thread.sleep;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DetailedSwitchTest {

    @Rule
    public ActivityTestRule<ExhibitsActivity> mActivityTestRule = new ActivityTestRule<>(ExhibitsActivity.class);

    @Test
    /**
     * Test if the go back button works correctly
     * */
    public void detailed_switch_test() throws InterruptedException {
        // clear the recyclerView
        sleep(500);
        onView(withId(R.id.clear_all)).perform(click());
        sleep(500);
        // Add exhibits to the list
        onView(withId(R.id.search_bar)).perform(typeText("go"));
        sleep(500);
        onView(withText(containsString("gorilla"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("fl"));
        sleep(500);
        onView(withText(containsString("flamingo"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("ko"));
        sleep(500);
        onView(withText(containsString("koi"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        Espresso.closeSoftKeyboard();

        // Click the "PLAN" button
        sleep(500);
        onView(withId(R.id.button11)).perform(click());


        // Click "Direction" button
        sleep(500);
        onView(withId(R.id.direction)).perform(click());

        // Click the "Next" button
        sleep(500);
        onView(withId(R.id.getNextDirection)).perform(click());

        // Click the detailed switch
        sleep(500);
        onView(withId(R.id.setting)).perform(click());

        //Check if directions are now detailed
        onView(withId(R.id.currentDirection)).check(matches(withText(containsString("Continue"))));

        // Click the detailed switch
        sleep(500);
        onView(withId(R.id.setting)).perform(click());

        //Check if directions are back to brief
        onView(withId(R.id.currentDirection)).check(matches(not(withText(containsString("Continue")))));

        // Click the detailed switch
        sleep(500);
        onView(withId(R.id.setting)).perform(click());

        // Click the "Next" button
        sleep(500);
        onView(withId(R.id.getNextDirection)).perform(click());

        //Click the "Step Back" button
        sleep(500);
        onView(withId(R.id.step_back)).perform(click());

        //Check if directions are now detailed
        onView(withId(R.id.currentDirection)).check(matches(withText(containsString("Continue"))));

        // Click the detailed switch
        sleep(500);
        onView(withId(R.id.setting)).perform(click());

        //Check if directions are back to brief
        onView(withId(R.id.currentDirection)).check(matches(not(withText(containsString("Continue")))));

    }
}
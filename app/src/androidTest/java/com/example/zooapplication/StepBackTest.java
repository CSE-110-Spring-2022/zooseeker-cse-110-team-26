package com.example.zooapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.anything;
import static java.lang.Thread.sleep;

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
public class StepBackTest {

    @Rule
    public ActivityTestRule<ExhibitsActivity> mActivityTestRule = new ActivityTestRule<>(ExhibitsActivity.class);

    @Test
    /**
     * Test if the step back button works correctly
     * */
    public void stepBackButtonTest() throws InterruptedException {
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
        // Click the "Skip" button
        sleep(500);
        onView(withId(R.id.getNextDirection)).perform(click());
        sleep(500);
        onView(withId(R.id.getNextDirection)).perform(click());
        sleep(500);
        onView(withId(R.id.getNextDirection)).perform(click());
        sleep(500);
        onView(withId(R.id.step_back)).perform(click());
        sleep(500);
        onView(withId(R.id.step_back)).perform(click());
        sleep(500);
        onView(withId(R.id.step_back)).perform(click());
        sleep(500);
        onView(withId(R.id.step_back)).perform(click());
        sleep(500);
        onView(withId(R.id.step_back)).perform(click());
        sleep(500);




        // An error message should be displayed after skipping all route
        onView(withText("No previous item!")).check(matches(isDisplayed()));
    }
}
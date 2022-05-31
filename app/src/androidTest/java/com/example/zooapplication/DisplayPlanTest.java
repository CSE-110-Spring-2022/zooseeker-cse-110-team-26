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
public class DisplayPlanTest {

    @Rule
    public ActivityTestRule<ExhibitsActivity> mActivityTestRule = new ActivityTestRule<>(ExhibitsActivity.class);

    @Test
    /**
     * Test if the Display Plan has the right distances
     * */
    public void checkCorrectDistanceNumbers() throws InterruptedException {
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

        //Check if the text view has the correct distance numbers
        sleep(500);
        onData(anything()).inAdapterView(withId(R.id.planlist)).atPosition(0).check(matches(withText(containsString("5300"))));
        onData(anything()).inAdapterView(withId(R.id.planlist)).atPosition(1).check(matches(withText(containsString("13100"))));
        onData(anything()).inAdapterView(withId(R.id.planlist)).atPosition(2).check(matches(withText(containsString("29800"))));

    }
}
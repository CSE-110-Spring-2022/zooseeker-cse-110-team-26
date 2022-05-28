package com.example.zooapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBackUnconditionally;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static java.lang.Thread.sleep;

import android.content.Intent;

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
public class RetainItemsTest {

    @Rule
    public ActivityTestRule<ExhibitsActivity> mActivityTestRule = new ActivityTestRule<>(ExhibitsActivity.class);

    @Test
    public void RetainTest() throws InterruptedException {
        // clear the recyclerView
        sleep(500);
        onView(withId(R.id.clear_all)).perform(click());
        sleep(500);
        // Add exhibits to the list
        onView(withId(R.id.search_bar)).perform(typeText("el"));
        sleep(500);
        onView(withText(containsString("elephant"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("li"));
        sleep(500);
        onView(withText(containsString("lions"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("f"));
        sleep(500);
        onView(withText(containsString("fox"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        Espresso.closeSoftKeyboard();

        // Check if the context of the list is correct
        String[] context = new String[]{"Elephant Odyssey", "Lions", "Arctic Foxes"}; // a string list contains the excepted output value
        for (int idx = 0; idx <context.length; idx++) {
            // try ti scroll to an item that contains the exhibit
            onView(ViewMatchers.withId(R.id.dis))
                    // scroll with fail if item is not on the recyclerView
                    .perform(RecyclerViewActions.scrollTo(
                            hasDescendant(withText(context[idx])))
                            );
        }

        // close and re-open the app
        sleep(500);
        pressBackUnconditionally();
        mActivityTestRule.finishActivity();
        mActivityTestRule.launchActivity(new Intent());

        // check items again when re-open the app
        for (int idx = 0; idx <context.length; idx++) {
            // try ti scroll to an item that contains the exhibit
            onView(ViewMatchers.withId(R.id.dis))
                    // scroll with fail if item is not on the recyclerView
                    .perform(RecyclerViewActions.scrollTo(
                            hasDescendant(withText(context[idx])))
                    );
        }

        // clear the recyclerView when the test is finished
        sleep(500);
        onView(withId(R.id.clear_all)).perform(click());

    }
}

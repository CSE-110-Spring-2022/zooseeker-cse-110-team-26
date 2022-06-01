package com.example.zooapplication;


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
/**
 * Class name: AddExhibitsTest
 * Description: Test if exhibits are added to the list correctly
 * public function:
 *      AddTest() - Test the list by adding three animal
 *      DuplicatedTest - Test if the app would prevent the user from adding duplicated exhibits
 *
 * */
public class AddExhibitsTest {

    @Rule
    public ActivityTestRule<ExhibitsActivity> mActivityTestRule = new ActivityTestRule<>(ExhibitsActivity.class);

    @Test
    /*
    *  Test the list by adding three animal
    *
    * */
    public void AddTest() throws InterruptedException {
        // clear the recyclerView
        sleep(500);
        onView(withId(R.id.clear_all)).perform(click());
        sleep(500);
        // Add exhibits to the list
        onView(withId(R.id.search_bar)).perform(typeText("fl"));
        sleep(500);
        onView(withText(containsString("flamingo"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("go"));
        sleep(500);
        onView(withText(containsString("gorilla"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("ko"));
        sleep(500);
        onView(withText(containsString("koi"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        Espresso.closeSoftKeyboard();

        // Check if the context of the list is correct
        String[] context = new String[]{"Flamingos", "Gorillas", "Koi Fish"}; // a string list contains the excepted output value
        for (int idx = 0; idx <context.length; idx++) {
            // try ti scroll to an item that contains the exhibit
            onView(ViewMatchers.withId(R.id.dis))
                    // scroll with fail if item is not on the recyclerView
                    .perform(RecyclerViewActions.scrollTo(
                            hasDescendant(withText(context[idx])))
                           );
            }
        }

    @Test
    /*
    *   Test if the app would prevent the user from adding duplicated exhibits
    * */
    public void DuplicatedTest() throws InterruptedException {
        sleep(500);
        onView(withId(R.id.clear_all)).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("fl"));
        sleep(500);
        onView(withText(containsString("flamingo"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("fl"));
        sleep(500);
        onView(withText(containsString("flamingo"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withText("Item already in the exhibits list")).check(matches(isDisplayed()));
    }
}

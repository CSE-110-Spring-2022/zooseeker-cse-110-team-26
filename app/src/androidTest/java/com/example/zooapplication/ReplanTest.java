package com.example.zooapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static java.lang.Thread.sleep;

import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ReplanTest {
    @Rule
    public ActivityTestRule<ExhibitsActivity> mActivityTestRule = new ActivityTestRule<>(ExhibitsActivity.class);
    UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    String beforeReplan = "Proceed on Gate Path 1100.0 ft towards Front Street / Treetops Way\n" +
            "Proceed on Front Street 2700.0 ft towards Front Street / Monkey Trail\n" +
            "Proceed on Monkey Trail 1500.0 ft towards Flamingos";

    String afterReplan = "Proceed on Monkey Trail 1200.0 ft towards Scripps Aviary\n" +
            "Continue on Monkey Trail 1200.0 ft towards Monkey Trail / Hippo Trail\n" +
            "Continue on Monkey Trail 2300.0 ft towards Capuchin Monkeys\n";
    String s1= DetailedtoBrief.toBrief(afterReplan); // check the brief direction
    /**
     * test if the app prompts and performs re-plan when the set location is off-route,
     * and the user choose to replan
    */
    @Test
    public void clickReplan() throws InterruptedException, UiObjectNotFoundException {
        onView(withId(R.id.currentDirection)).check(matches(withText(containsString(beforeReplan))));

        // mock the location at Gorilla by typing its lat nad lng
        onView(withId(R.id.lat)).perform(typeText("32.74711745394194"));
        sleep(500);
        onView(withId(R.id.lng)).perform(typeText("-117.18047982358976"));
        sleep(500);
        Espresso.closeSoftKeyboard();
        //click the set button to mock location
        onView(withId(R.id.set)).perform(click());
        sleep(500);

//        textView.check(matches(withText(containsString("Test"))));
        sleep(500);
        // check if the dialog pops and asks if the user want to replan
        onView(withText("Off-route! Want to re-plan?")).check(matches(isDisplayed()));
        sleep(500);

        // mock that the user click the replan button
        UiObject clickReplan = new UiObject(new UiSelector().text("RE-PLAN"));
        clickReplan.click();
        sleep(500);

        onView(withId(R.id.currentDirection)).check(matches(withText(containsString(s1))));


    }

    /***
     * Test if the plan remain the same if the user chooses not to replan
     */
    @Test
    public void unclickReplan() throws InterruptedException, UiObjectNotFoundException {
        onView(withId(R.id.currentDirection)).check(matches(withText(containsString(beforeReplan))));

        // mock the location at Gorilla by typing its lat nad lng
        onView(withId(R.id.lat)).perform(typeText("32.74711745394194"));
        sleep(500);
        onView(withId(R.id.lng)).perform(typeText("-117.18047982358976"));
        sleep(500);
        Espresso.closeSoftKeyboard();
        //click the set button to mock location
        onView(withId(R.id.set)).perform(click());
        sleep(500);

//        textView.check(matches(withText(containsString("Test"))));
        sleep(500);
        // check if the dialog pops and asks if the user want to replan
        onView(withText("Off-route! Want to re-plan?")).check(matches(isDisplayed()));
        sleep(500);

        // mock that the user click the cancel button
        UiObject clickReplan = new UiObject(new UiSelector().text("CANCEL"));
        clickReplan.click();
        sleep(500);

        onView(withId(R.id.currentDirection)).check(matches(withText(containsString(beforeReplan))));


    }

    // add exhibits to the list before testing
    @Before
    public void initialize() throws  InterruptedException{
        // clear the recyclerView
        sleep(500);
        onView(withId(R.id.clear_all)).perform(click());
        sleep(500);
        // Add exhibits to the list
        onView(withId(R.id.search_bar)).perform(typeText("fl"));
        sleep(500);
        onView(withText(containsString("flamingo"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        onView(withId(R.id.search_bar)).perform(typeText("mo"));
        sleep(500);
        onView(withText(containsString("monkey"))).inRoot(isPlatformPopup()).perform(click());
        sleep(500);
        Espresso.closeSoftKeyboard();

        // Click the "PLAN" button
        sleep(500);
        onView(withId(R.id.button11)).perform(click());

        // Click "Direction" button
        sleep(500);
        onView(withId(R.id.direction)).perform(click());

    }




}

package com.example.zooapplication;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.zooapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static java.lang.Thread.sleep;

@LargeTest
@RunWith(AndroidJUnit4.class)
/**
 * Class name: DirectionTest
 * Description: Test if the direction can be displayed correctly
 * public function:
 *      DirectionTest - Check if the plan and route information can be displayed
 *
 * */
public class DirectionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    /**
     * Test the if the direction display correctly
     * */
    public void PlanTest() {

        // Add exhibits to the list
        onView(withId(R.id.search_bar)).perform(typeText("el"));
        onView(withText(containsString("elephant"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.search_bar)).perform(typeText("li"));
        onView(withText(containsString("lions"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(R.id.search_bar)).perform(typeText("f"));
        onView(withText(containsString("fox"))).inRoot(isPlatformPopup()).perform(click());
        Espresso.closeSoftKeyboard();

        // Click the "PLAN" button
        onView(withId(R.id.button11)).perform(click());

        // Check if the context of the list is correct
        String[] context = new String[]{"lions", "elephant_odyssey", "arctic_foxes"}; // a string list contains the excepted output value
        for (int idx = 0; idx <context.length; idx++) {
            onData(anything())
                    .inAdapterView(withId(R.id.planlist))
                    .atPosition(idx)
                    .check(matches(withText(context[idx])));
        }

        // Click "Direction" button
        onView(withId(R.id.direction)).perform(click());
        // Click the "NEXT" button
        onView(withId(R.id.getNextDirection)).perform(click());
        onView(withId(R.id.getNextDirection)).perform(click());
        onView(withId(R.id.getNextDirection)).perform(click());

        // Check if Error message display when the trip is finished
        onView(withText("The route is done!")).check(matches(isDisplayed()));

    }
}

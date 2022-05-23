//package com.example.zooapplication;
//
//
//import androidx.test.espresso.DataInteraction;
//import androidx.test.espresso.ViewInteraction;
//import androidx.test.filters.LargeTest;
//import androidx.test.rule.ActivityTestRule;
//import androidx.test.runner.AndroidJUnit4;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewParent;
//
//import static androidx.test.InstrumentationRegistry.getInstrumentation;
//import static androidx.test.espresso.Espresso.onData;
//import static androidx.test.espresso.Espresso.onView;
//import static androidx.test.espresso.Espresso.pressBack;
//import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
//import static androidx.test.espresso.action.ViewActions.*;
//import static androidx.test.espresso.assertion.ViewAssertions.*;
//import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
//import static androidx.test.espresso.matcher.ViewMatchers.*;
//
//import com.example.zooapplication.R;
//
//import org.hamcrest.Description;
//import org.hamcrest.Matcher;
//import org.hamcrest.TypeSafeMatcher;
//import org.hamcrest.core.IsInstanceOf;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.hamcrest.CoreMatchers.instanceOf;
//import static org.hamcrest.Matchers.allOf;
//import static org.hamcrest.Matchers.anything;
//import static org.hamcrest.Matchers.is;
//import static java.lang.Thread.sleep;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
///**
// * Class name: AddExhibitsTest
// * Description: Test if exhibits are added to the list correctly
// * public function:
// *      AddTest() - Test the list by adding three animal
// *      DuplicatedTest - Test if the app would prevent the user from adding duplicated exhibits
// *
// * */
//public class AddExhibitsTest {
//
//    @Rule
//    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
//
//    @Test
//    /*
//    *  Test the list by adding three animal
//    *
//    * */
//    public void AddTest() throws InterruptedException {
//        // adding three exhibits to the list
//        onView(withId(R.id.search_bar)).perform(typeText("el"));
//        sleep(500);
//        onView(withText(containsString("elephant odyssey"))).inRoot(isPlatformPopup()).perform(click());
//        sleep(500);
//        onView(withId(R.id.search_bar)).perform(typeText("li"));
//        sleep(500);
//        onView(withText(containsString("lions"))).inRoot(isPlatformPopup()).perform(click());
//        sleep(500);
//        onView(withId(R.id.search_bar)).perform(typeText("f"));
//        sleep(500);
//        onView(withText(containsString("fox"))).inRoot(isPlatformPopup()).perform(click());
//        sleep(500);
//
//        // Check if the context of the list is correct
//        String[] context = new String[]{"Elephant Odyssey", "Lions", "Arctic Foxes"}; // a string list contains the excepted output value
//        for (int idx = 0; idx <context.length; idx++) {
//            onData(anything())
//                    .inAdapterView(withId(R.id.dis))
//                    .atPosition(idx)
//                    .check(matches(withText(context[idx])));
//            }
//        }
//
//    @Test
//    /*
//    *   Test if the app would prevent the user from adding duplicated exhibits
//    * */
//    public void DuplicatedTest() throws InterruptedException {
//        onView(withId(R.id.search_bar)).perform(typeText("el"));
//        sleep(500);
//        onView(withText(containsString("elephant"))).inRoot(isPlatformPopup()).perform(click());
//        sleep(500);
//        onView(withId(R.id.search_bar)).perform(typeText("el"));
//        sleep(500);
//        onView(withText(containsString("elephant"))).inRoot(isPlatformPopup()).perform(click());
//        sleep(500);
//        onView(withText("Item already in the exhibits list")).check(matches(isDisplayed()));
//    }
//}

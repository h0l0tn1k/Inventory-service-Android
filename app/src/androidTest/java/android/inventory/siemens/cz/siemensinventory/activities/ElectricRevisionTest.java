package android.inventory.siemens.cz.siemensinventory.activities;


import android.inventory.siemens.cz.siemensinventory.R;
import android.inventory.siemens.cz.siemensinventory.helpers.When;
import android.inventory.siemens.cz.siemensinventory.scenarios.Login;
import android.inventory.siemens.cz.siemensinventory.scenarios.SetConnection;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ElectricRevisionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void electricRevisionTest() {

        SetConnection.setConnectionIfIncorrect();

        When.iLogin();

        When.iOpenNavigationDrawer(mActivityTestRule);

        When.iClickOnElectricRevision();

        When.iWriteIntoSearchField(withId(R.id.el_revision_search_box), "8");

        onData(anything()).inAdapterView(withId(R.id.el_revision_search_results)).atPosition(1).perform(click());

        onView(withId(R.id.electricRevisionBtn)).perform(click());

        onView(allOf(withId(R.id.rfab__content_label_list_icon_iv),
                        childAtPosition(
                                allOf(withId(R.id.rfab__content_label_list_root_view),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                0)),
                                1),
                        isDisplayed())).perform(click());

        When.iClickOnElement(allOf(withId(android.R.id.button1), withText("Save")));

        When.iPressBack();

        onData(anything()).inAdapterView(withId(R.id.el_revision_search_results)).atPosition(2).perform(scrollTo(), click());

        onView(withId(R.id.electricRevisionBtn)).perform(click());

        onView(allOf(withId(R.id.rfab__content_label_list_icon_iv),
                        childAtPosition(
                                allOf(withId(R.id.rfab__content_label_list_root_view),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                1)),
                                1),
                        isDisplayed())).perform(click());

        When.iClickOnElement(allOf(withId(android.R.id.button1), withText("Yes")));

        When.iPressBack();

        onView(withId(R.id.el_revision_scanBtn)).perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

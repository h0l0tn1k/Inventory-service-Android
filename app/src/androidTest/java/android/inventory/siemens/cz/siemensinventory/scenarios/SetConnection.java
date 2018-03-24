package android.inventory.siemens.cz.siemensinventory.scenarios;

import android.inventory.siemens.cz.siemensinventory.R;
import android.inventory.siemens.cz.siemensinventory.helpers.Then;
import android.inventory.siemens.cz.siemensinventory.helpers.When;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

/**
 * Created by I333206 on 24.03.2018.
 */

public class SetConnection {

    private static String ipAddress = "10.182.37.17", path = "/rest", port = "8080";


    public static void setConnectionIfIncorrect() {
        if(Then.isSnackbarDisplayed()) {
            setConnection();
        }
    }

    public static void setConnection() {
        When.iPressBack();

        When.iClickOnElement(withId(R.id.btn_setting));

        //set HTTP method
        onData(PreferenceMatchers.withTitle(R.string.httpMethod)).perform(click());

        onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(withClassName(is("android.widget.FrameLayout")))))
                .atPosition(0).perform(click());

        //set ip address
        onData(PreferenceMatchers.withTitle(R.string.ipAddress)).perform(click());
        When.iEnterTextIntoElement(withId(android.R.id.edit), ipAddress);
        When.iClickOnElement(allOf(withId(android.R.id.button1), withText("OK")));

        //set port
        onData(PreferenceMatchers.withTitle(R.string.port)).perform(click());
        When.iEnterTextIntoElement(withId(android.R.id.edit), port);
        When.iClickOnElement(allOf(withId(android.R.id.button1), withText("OK")));

        //set path
        onData(PreferenceMatchers.withTitle(R.string.path)).perform(click());
        When.iEnterTextIntoElement(withId(android.R.id.edit), path);
        When.iClickOnElement(allOf(withId(android.R.id.button1), withText("OK")));

        //test connection & assert
        onData(PreferenceMatchers.withTitle(R.string.testConnectionToService)).perform(click());

        Then.iShouldSeeSnackbarWithMessage("Connection is successful!");

        pressBack();
    }

    private static Matcher<View> childAtPosition(
    final Matcher<View> parentMatcher) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + 0 + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(0));
            }
        };
    }
}

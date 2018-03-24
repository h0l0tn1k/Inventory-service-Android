package android.inventory.siemens.cz.siemensinventory.helpers;

import android.inventory.siemens.cz.siemensinventory.R;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by I333206 on 23.03.2018.
 */

public class When {

    public static void iPressBack() {
        pressBack();
    }

    public static void iEnterTextIntoElement(Matcher<View> element, String text) {
        onView(element).perform(scrollTo(), replaceText(text), closeSoftKeyboard());
    }

    public static void iClickOnElement(Matcher<View> element) {
        onView(element).perform(scrollTo(), click());
    }

    public static void iSleepFor(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void iClickOnElectricRevision() {
        onView(childAtPosition(withId(R.id.design_navigation_view), 6)).perform(click());
    }

    public static void iWriteIntoSearchField(Matcher<View> view, String query) {
        onView(view).perform(click(), typeText(query), closeSoftKeyboard());
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

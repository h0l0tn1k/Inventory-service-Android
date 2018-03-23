package android.inventory.siemens.cz.siemensinventory.helpers;

import android.inventory.siemens.cz.siemensinventory.R;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by I333206 on 23.03.2018.
 */

public class Then {

    public static void iShouldSeeEditTextWithError(Matcher<View> editText, String errorMessage) {
        onView(editText).check(matches(isDisplayed()));
        onView(editText).check(matches(hasErrorText(errorMessage)));
    }

    public static void iShouldSeeEditTextWithNoError(Matcher<View> editText) {
        onView(editText).check(matches(isDisplayed()));
        onView(editText).check(matches(hasNoErrorText()));
    }

    public static void iShouldSeeSnackbarWithMessage(String message) {
        onView(allOf(withId(R.id.snackbar_text), withText(message))).check(matches(isDisplayed()));
    }

    public static void iShouldSeeElement(Matcher<View> element) {
        onView(element).check(matches(isDisplayed()));
    }

    private static Matcher<View> hasNoErrorText() {
        return new BoundedMatcher<View, EditText>(EditText.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("Has no error text: ");
            }

            @Override
            protected boolean matchesSafely(EditText view) {
                return view.getError() == null;
            }
        };
    }
}

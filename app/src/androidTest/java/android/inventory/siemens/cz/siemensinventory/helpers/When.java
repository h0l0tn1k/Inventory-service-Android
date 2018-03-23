package android.inventory.siemens.cz.siemensinventory.helpers;

import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;

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
}

package android.inventory.siemens.cz.siemensinventory.activities;


import android.app.Activity;
import android.inventory.siemens.cz.siemensinventory.R;
import android.inventory.siemens.cz.siemensinventory.helpers.Then;
import android.inventory.siemens.cz.siemensinventory.helpers.When;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void loginWithEmptyCredentials() {
        When.iClickOnElement(withId(R.id.btn_login));

        //assert
        Then.iShouldSeeEditTextWithError(withId(R.id.login_email), getString(R.string.email_empty));
        Then.iShouldSeeEditTextWithError(withId(R.id.login_password), getString(R.string.password_empty));
    }

    @Test
    public void loginWithInvalidEmail() {
        When.iClickOnElement(withId(R.id.login_email));
        When.iEnterTextIntoElement(withId(R.id.login_email), "invalidEmail");

        When.iClickOnElement(withId(R.id.btn_login));

        //assert
        Then.iShouldSeeEditTextWithError(withId(R.id.login_email), getString(R.string.email_empty));
        Then.iShouldSeeEditTextWithError(withId(R.id.login_password), getString(R.string.password_empty));
    }

    @Test
    public void loginWithIncorrectPassword() {
        When.iEnterTextIntoElement(withId(R.id.login_email), "josef.novak@siemens.com");
        When.iEnterTextIntoElement(withId(R.id.login_password), "IncorrectPassword");

        When.iClickOnElement(withId(R.id.btn_login));

        //assert
        Then.iShouldSeeEditTextWithNoError(withId(R.id.login_email));
        Then.iShouldSeeEditTextWithNoError(withId(R.id.login_password));
        Then.iShouldSeeSnackbarWithMessage(getString(R.string.invalid_credentials));
    }

    @Test
    public void loginWithCorrectPassword() {
        When.iEnterTextIntoElement(withId(R.id.login_email), "josef.novak@siemens.com");
        When.iEnterTextIntoElement(withId(R.id.login_password), "ASDF001");

        When.iClickOnElement(withId(R.id.btn_login));

        //assert
        Then.iShouldSeeElement(withId(R.id.permissionsView));
    }

    private String getString(int stringId) {
        return getContext().getString(stringId);
    }

    private Activity getContext() {
        return mActivityTestRule.getActivity();
    }
}

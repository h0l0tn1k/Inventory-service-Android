package android.inventory.siemens.cz.siemensinventory.scenarios;

import android.inventory.siemens.cz.siemensinventory.R;
import android.inventory.siemens.cz.siemensinventory.helpers.Then;
import android.inventory.siemens.cz.siemensinventory.helpers.When;

import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by I333206 on 24.03.2018.
 */

public class Login {

    public static void loginWithCorrectCredentials() {
        When.iEnterTextIntoElement(withId(R.id.login_email), "josef.novak@siemens.com");
        When.iEnterTextIntoElement(withId(R.id.login_password), "ASDF001");

        When.iClickOnElement(withId(R.id.btn_login));

        //assert
        Then.iShouldSeeElement(withId(R.id.permissionsView));
    }
}

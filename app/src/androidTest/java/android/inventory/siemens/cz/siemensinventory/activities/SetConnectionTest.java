package android.inventory.siemens.cz.siemensinventory.activities;


import android.inventory.siemens.cz.siemensinventory.dashboard.MainActivity;
import android.inventory.siemens.cz.siemensinventory.scenarios.SetConnection;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class SetConnectionTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void setConnectionTest() {
        SetConnection.setConnectionFromLoginScreen();
    }

}

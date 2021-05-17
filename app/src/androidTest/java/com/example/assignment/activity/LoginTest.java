package com.example.assignment.activity;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.rule.ActivityTestRule;

import com.example.assignment.MainActivity;
import com.example.assignment.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LoginTest {
    @Rule
    public ActivityTestRule<Login> activityScenarioRule = new ActivityTestRule<Login>(Login.class);
    private Login login = null;
    Instrumentation.ActivityMonitor monitor =getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        login = activityScenarioRule.getActivity();
    }

    @Test
    public void testLaunchOfSecondActivityOnButtonClick(){
        assertNotNull(login.findViewById(R.id.login_submit));
        onView(withId(R.id.login_edit_email)).perform(typeText("test@test.com"));
        onView(withId(R.id.login_edit_password)).perform(typeText("123456"));

        onView(withId(R.id.login_submit)).perform(click());

        Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(activity);
        activity.finish();
    }

    @After
    public void tearDown() throws Exception {
        login = null;
    }
}
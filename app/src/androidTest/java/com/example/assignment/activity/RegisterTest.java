package com.example.assignment.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.test.rule.ActivityTestRule;

import com.example.assignment.MainActivity;
import com.example.assignment.R;
import com.example.assignment.fragment.AddFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class RegisterTest {
    @Rule
    public ActivityTestRule<Register> activityScenarioRule = new ActivityTestRule<Register>(Register.class);
    private Register register = null;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(Login.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        register = activityScenarioRule.getActivity();

    }

    @Test
    public void testLaunchOfSecondActivityOnButtonClick() {
        onView(withId(R.id.register_edit_email)).perform(typeText("test111111@test.com"));
        onView(withId(R.id.register_edit_password)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.register_edit_repeat_password)).perform(typeText("123456"));
        closeSoftKeyboard();
        onView(withId(R.id.register_submit)).perform(click());
        Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);

        assertNotNull(activity);
        activity.finish();
    }

    @After
    public void tearDown() throws Exception {
        register = null;
    }
}
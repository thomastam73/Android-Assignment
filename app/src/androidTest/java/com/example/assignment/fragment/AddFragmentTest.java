package com.example.assignment.fragment;

import android.widget.RelativeLayout;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.ActivityTestRule;

import com.example.assignment.R;
import com.example.assignment.test.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.*;

public class AddFragmentTest {
    @Rule
    public ActivityTestRule<Test> activityScenarioRule = new ActivityTestRule<Test>(Test.class);

    private Test test = null;

    @Before
    public void setUp() throws Exception {
        test = activityScenarioRule.getActivity();
    }

    @org.junit.Test
    public void testLaunch() {
        RelativeLayout relativeLayout = (RelativeLayout) test.findViewById(R.id.test_container);
        assertNotNull(relativeLayout);
        AddFragment fragment = new AddFragment();
        test.getFragmentManager().beginTransaction().add(relativeLayout.getId(),fragment).commitAllowingStateLoss();


    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }
}
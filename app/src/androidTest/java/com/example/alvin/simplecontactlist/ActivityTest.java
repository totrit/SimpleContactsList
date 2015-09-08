package com.example.alvin.simplecontactlist;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;

import com.example.alvin.simplecontactlist.control.MainActivity;
import com.example.alvin.simplecontactlist.model.ContactsDataManager;
import com.example.alvin.simplecontactlist.model.PerContactInfo;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class ActivityTest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public ActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        waitAWhile(3000);
    }

    @Test
    public void testNetFetch() throws Exception {
        List<PerContactInfo> ret = ContactsDataManager.getInstance().getContactsList(ContactsDataManager.SORT_NONE);
        assertTrue(ret != null && ret.size() > 0);
    }

    @Test
    public void testListViewHasContent() {
        final String SAMPLE_NAME = "Leanne Graham";
        onView(allOf(withText(SAMPLE_NAME), withId(R.id.name))).check(matches(isDisplayed()));
    }

    @Test
    public void testListItemClickAndBack() {
        final String SAMPLE_NAME = "Leanne Graham";
        final String SAMPLE_WEBSITE = "hildegard.org";
        onView(allOf(withText(SAMPLE_NAME), withId(R.id.name))).perform(ViewActions.click());
        waitAWhile(1000);
        onView(allOf(withId(R.id.website_txt), withText(SAMPLE_WEBSITE))).check(matches(isDisplayed()));
        onView(withContentDescription("Navigate up")).perform(ViewActions.click());
        waitAWhile(1000);
        testListViewHasContent();
    }

    private void waitAWhile(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
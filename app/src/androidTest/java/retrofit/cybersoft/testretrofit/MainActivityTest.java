package retrofit.cybersoft.testretrofit;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by nagendra on 07/09/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ensureListPopulatesAndItemClick(){
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        sleepThread(1000);
        onView(withText(R.string.action_settings)).perform(click());
        sleepThread(3000);
        onData(anything()).inAdapterView(withId(R.id.question_list)).atPosition(1).perform(click());
        sleepThread(3000);
        pressBack();

    }


    @Test
    public void ensureListPopulatesAndUserClick(){
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        sleepThread(1000);
        onView(withText(R.string.action_settings)).perform(click());
        sleepThread(3000);
        onData(anything()).inAdapterView(withId(R.id.question_list)).atPosition(1).onChildView(withId(R.id.owner)).perform(click());
        sleepThread(3000);
        pressBack();
    }

    public void sleepThread(long milli){
        try {
            Thread.sleep(milli);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}



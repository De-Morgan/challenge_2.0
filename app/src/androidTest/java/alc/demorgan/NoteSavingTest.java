package alc.demorgan;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import alc.demorgan.jounal.NoteActivity.NotepadActivity;
import alc.demorgan.jounal.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NoteSavingTest {
    @Rule
    public ActivityTestRule<NotepadActivity> mNoteActivityTestRule = new ActivityTestRule<>(NotepadActivity.class);

    @Test
    public void clickToAddJournal() throws Exception {

        onView(withId(R.id.fab))
                .perform(click());
        onView(withId(R.id.content))
                .check(matches(isDisplayed()));
        onView(withId(R.id.content))
                .perform(typeText("This is a Test String"));

    }
}

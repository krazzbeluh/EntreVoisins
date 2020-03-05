package com.openclassrooms.entrevoisins.neighbour_detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.matcher.IntentMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.widget.ImageView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.ActivityResultMatchers.hasResultCode;
import static android.support.test.espresso.contrib.ActivityResultMatchers.hasResultData;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.FAVORITES_STRING;
import static org.hamcrest.core.IsNull.notNullValue;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.BUNDLE_IDENTIFIER;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.SERIALIZABLE_IDENTIFIER;

@RunWith(AndroidJUnit4.class)
public class NeighbourDetailTest {

    private static final String TAG = NeighbourDetailTest.class.getSimpleName();

    private Neighbour mNeighbour = new Neighbour(42, "Lorem Ipsum", "", "Lorem Ipsum Dolor Sit Amet", "1234567890", "Lorem Ipsum Dolor Sit Amet Consectetuor Adiscipiling Elit");
    private SharedPreferences mSharedPreferences;
    private Context mTargetContext;

    @Rule
    public ActivityTestRule<NeighbourDetailActivity> mActivityRule =
            new ActivityTestRule(NeighbourDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    super.getActivityIntent();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(SERIALIZABLE_IDENTIFIER, mNeighbour);
                    intent.putExtra(BUNDLE_IDENTIFIER, bundle);
                    return intent;
                }
            };

    @Before
    public void setUp() {
        mTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mSharedPreferences = mTargetContext.getSharedPreferences("FavoritesInInstrumentedTests", Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString(FAVORITES_STRING, "").apply();
        mActivityRule.getActivity().setSharedPreferences(mSharedPreferences);
    }

    @Test
    public void detailViewShouldDisplayNeighbourInfos() {
        onView(withId(R.id.detail_name)).check(matches(withText(mNeighbour.getName())));
        onView(withId(R.id.detail_location)).check(matches(withText(mNeighbour.getAddress())));
        onView(withId(R.id.detail_phone)).check(matches(withText(mNeighbour.getPhoneNumber())));
        onView(withId(R.id.detail_network)).check(matches(withText("www.facebook.fr/" + mNeighbour.getName())));
        onView(withId(R.id.detail_description)).check(matches(withText(mNeighbour.getAboutMe())));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(mNeighbour.getName()))));
    }

    @Test
    public void starButtonShouldAddThenRemoveFromFavorites() {
        onView(withId(R.id.fav_neighbour_button)).check(matches(withContentDescription(mTargetContext.getString(R.string.star_button_description_is_not_favorite))));
        onView(withId(R.id.fav_neighbour_button)).perform(click()).check(matches(withContentDescription(mTargetContext.getString(R.string.star_button_description_is_favorite))));
        onView(withId(R.id.fav_neighbour_button)).perform(click()).check(matches(withContentDescription(mTargetContext.getString(R.string.star_button_description_is_not_favorite))));
    }
}

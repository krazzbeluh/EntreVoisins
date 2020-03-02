package com.openclassrooms.entrevoisins.neighbour_detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNull.notNullValue;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.BUNDLE_IDENTIFIER;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.SERIALIZABLE_IDENTIFIER;

@RunWith(AndroidJUnit4.class)
public class NeighbourDetailTest {

    private NeighbourDetailActivity mActivity;

    private Neighbour mNeighbour;

    @Rule
    public ActivityTestRule<NeighbourDetailActivity> mActivityRule =
            new ActivityTestRule(NeighbourDetailActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(SERIALIZABLE_IDENTIFIER, mNeighbour);
                    intent.putExtra(BUNDLE_IDENTIFIER, bundle);
                    return intent;
                }
            };

    @Before
    public void setUp() {
        mNeighbour = new Neighbour(42, "Lorem Ipsum", "https://openclassrooms.com", "Lorem Ipsum Dolor Sit Amet", "1234567890", "Lorem Ipsum Dolor Sit Amet Consectetuor Adiscipiling Elit");
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void detailViewShouldDisplayNeighbourInfos() {
        onView(ViewMatchers.withId(R.id.detail_name)).check(matches(withText(mNeighbour.getName())));
    }
}

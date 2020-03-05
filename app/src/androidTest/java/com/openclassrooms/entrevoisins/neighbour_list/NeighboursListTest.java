
package com.openclassrooms.entrevoisins.neighbour_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerViewAccessibilityDelegate;
import android.util.Log;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.DummyNeighbourApiService;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;
import com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.FAVORITES_STRING;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.atPosition;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;
    public static final String TAG = NeighboursListTest.class.getSimpleName();

    private Context mTargetContext;
    private SharedPreferences mSharedPreferences;
    private NeighbourApiService mApiService = new DummyNeighbourApiService();

    @Rule
    public IntentsTestRule<ListNeighbourActivity> intentsTestRule =
            new IntentsTestRule<>(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mSharedPreferences = mTargetContext.getSharedPreferences("FavoritesInInstrumentedTests", Context.MODE_PRIVATE);
        mSharedPreferences.edit().putString(FAVORITES_STRING, "").apply();
        intentsTestRule.getActivity().setSharedPreferences(mSharedPreferences);
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        // First scroll to the position that needs to be matched and click on it.
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        // Given : We remove the element at position 2
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours))).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours))).check(withItemCount(ITEMS_COUNT-1));
    }

    @Test
    public void myNeighbourList_onClick_ShouldOpenNeighbourDetail() {
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours))).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(NeighbourDetailActivity.class.getName()));
    }

    @Test
    public void myFavoriteList_shouldDisplayOnlyFavorites() {
        onView(withContentDescription(R.string.tab_favorites_title)).perform(click());
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours))).check(withItemCount(0));

        List<Long> favoritesID = new ArrayList<>();
        favoritesID.add(Long.valueOf(1));
        favoritesID.add(Long.valueOf(3));
        favoritesID.add(Long.valueOf(4));
        favoritesID.add(Long.valueOf(6));
        favoritesID.add(Long.valueOf(10));
        favoritesID.add(Long.valueOf(12));

        StringBuilder builder = new StringBuilder();
        for (Long id: favoritesID) {
            builder.append(String.valueOf(id));
            builder.append(";");
        }

        mSharedPreferences.edit().putString(FAVORITES_STRING, builder.toString()).apply();

        // refresh view
        onView(withContentDescription(R.string.tab_neighbour_title)).perform(click());
        onView(withContentDescription(R.string.tab_favorites_title)).perform(click());
        onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours))).check(withItemCount(6));

        List<Neighbour> neighbours = mApiService.getNeighbours();
        List<String> favoritesNames = neighbours.stream()
                .filter(n -> favoritesID.contains(n.getId()))
                .collect(Collectors.toList())
                .stream()
                .map(n -> n.getName())
                .collect(Collectors.toList());
        Log.d(TAG, "myFavoriteList_shouldDisplayOnlyFavorites: " + favoritesNames);

        for (int i = 0; i < favoritesNames.size(); i++) {
            onView(CoreMatchers.allOf(isCompletelyDisplayed(), ViewMatchers.withId(R.id.list_neighbours))).check(matches(atPosition(i, hasDescendant(withText(favoritesNames.get(i))))));
        }
    }
}
package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.FavoritesManager;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailActivity.FAVORITES_STRING;


public class NeighbourFragment extends Fragment {

    private NeighbourApiService mApiService;
    private RecyclerView mRecyclerView;
    private boolean mIsFavorite;

    private SharedPreferences mSharedPreferences;
    private FavoritesManager mFormatter = new FavoritesManager();
    private boolean isVisible = false;

    void setSharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    private static final String TAG = "NeighbourFragment";
    private static final String BUNDLE_IDENTIFIER_ISFAVORITE = "isFavorite";

    /**
     * Create and return a new instance
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(boolean isFavorite, SharedPreferences sharedPreferences) {
        NeighbourFragment fragment = new NeighbourFragment();

        if (isFavorite) fragment.setSharedPreferences(sharedPreferences);

        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_IDENTIFIER_ISFAVORITE, isFavorite);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        mIsFavorite = args.getBoolean(BUNDLE_IDENTIFIER_ISFAVORITE, false);

        View view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        Context context = view.getContext();

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        if (mIsFavorite) {
            String favoritesString = mSharedPreferences.getString(FAVORITES_STRING, "");
            Log.d(TAG, "initList: " + favoritesString);
            List<Long> favoritesIds = mFormatter.getFavorites(favoritesString);

            List<Neighbour> neighbours = mApiService.getNeighbours();
            List<Neighbour> favorites = new ArrayList<>();

            for (Long id: favoritesIds) {
                for (Neighbour neighbour: neighbours) {
                    if (id == neighbour.getId()) {
                        favorites.add(neighbour);
                        break;
                    }
                }
            }

            mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(favorites));
        } else {
            List<Neighbour> neighbours = mApiService.getNeighbours();
            mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter(neighbours));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
        hasLaunched = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean hasLaunched = false;

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        isVisible = visible;

        if (hasLaunched) initList();
    }

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        if (this.isVisible) {
            if (!mIsFavorite) {
                Log.d(TAG, "onDeleteNeighbour: isFavorite: " + mIsFavorite);
                mApiService.deleteNeighbour(event.neighbour);
            } else {
                Log.d(TAG, "onDeleteFavorite: ");
                String favoritesStr = mSharedPreferences.getString(FAVORITES_STRING, "");
                List<Long> favorites = mFormatter.getFavorites(favoritesStr);

                favorites.remove(event.neighbour.getId());

                mSharedPreferences.edit().putString(FAVORITES_STRING, mFormatter.getFavoritesString(favorites)).apply();
            }
            initList();
        }
    }
}

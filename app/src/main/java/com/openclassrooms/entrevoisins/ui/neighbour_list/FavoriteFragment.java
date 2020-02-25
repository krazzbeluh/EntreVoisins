package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import static android.content.Context.MODE_PRIVATE;


public class FavoriteFragment extends Fragment {

    private SharedPreferences mSharedPreferences;
    private FavoritesManager mFormatter = new FavoritesManager();

    private void setSharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    private NeighbourApiService mApiService;
    private RecyclerView mRecyclerView;

    private static final String TAG = "NeighbourFragment";

    /**
     * Create and return a new instance
     * @return @{@link FavoriteFragment}
     */
    public static FavoriteFragment newInstance(SharedPreferences sharedPreferences) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setSharedPreferences(sharedPreferences);
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
        List<Long> favoritesIds = mFormatter.getFavorites(mSharedPreferences.getString(FAVORITES_STRING, ""));

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
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
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

    /**
     * Fired if the user clicks on a delete button
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        String favoritesStr = mSharedPreferences.getString(FAVORITES_STRING, "");
        List<Long> favorites = mFormatter.getFavorites(favoritesStr);

        favorites.remove(event.neighbour.getId());

        mSharedPreferences.edit().putString(FAVORITES_STRING, mFormatter.getFavoritesString(favorites)).apply();

        initList();
    }
}

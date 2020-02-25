package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.openclassrooms.entrevoisins.model.Neighbour;


class ListNeighbourPagerAdapter extends FragmentPagerAdapter {
    public static final String TAG = ListNeighbourPagerAdapter.class.getSimpleName();

    private SharedPreferences mSharedPreferences;

    public ListNeighbourPagerAdapter(FragmentManager fm, SharedPreferences sharedPreferences) {
        super(fm);
        mSharedPreferences = sharedPreferences;
    }

    /**
     * getItem is called to instantiate the fragment for the given page.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: bite");
        switch (position) {
            case 1: return FavoriteFragment.newInstance(mSharedPreferences);
            default: return NeighbourFragment.newInstance();
        }
    }

    /**
     * get the number of pages
     * @return
     */
    @Override
    public int getCount() {
        return 2;
    }
}
package com.openclassrooms.entrevoisins.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {

    public static final String TAG = FavoritesManager.class.getSimpleName();

    public List<Long> getFavorites(String favoritesString) {
        List<Long> ids = new ArrayList<>();

        if (favoritesString == null || favoritesString == "") {
            return ids;
        }

        String[] splitedStrings = favoritesString.split(";");

        for (String favoriteString: splitedStrings) {
            ids.add(Long.valueOf(favoriteString));
        }

        return ids;
    }

    public String getFavoritesString(List<Long> favorites) {
        StringBuilder builder = new StringBuilder();

        for (Long id: favorites) {
            builder.append(String.valueOf(id));
            builder.append(";");
        }

        return builder.toString();
    }
}

package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.FavoritesManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FavoritesManagetTest {
    private FavoritesManager formatter;

    private List<Long> favoritesList;
    private String favoritesStr;

    @Before
    public void setup() {
        formatter = new FavoritesManager();

        favoritesList = new ArrayList<Long>() {
            {
                add(Long.valueOf(4));
                add(Long.valueOf(6));
                add(Long.valueOf(2));
                add(Long.valueOf(9));
            }
        };
        favoritesStr = "4;6;2;9;";
    }

    @Test
    public void getFavoritesWithSuccess() {
        List<Long> favorites = formatter.getFavorites(favoritesStr);
        assertEquals(favorites, favoritesList);
    }

    @Test
    public void getFavoritesStringWithSuccess() {
        String favorites = formatter.getFavoritesString(favoritesList);
        assertEquals(favorites, favoritesStr);
    }
}

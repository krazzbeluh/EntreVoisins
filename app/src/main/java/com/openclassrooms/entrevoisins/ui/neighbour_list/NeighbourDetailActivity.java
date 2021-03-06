package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.model.FavoritesManager;

import java.util.ArrayList;
import java.util.List;

public class NeighbourDetailActivity extends AppCompatActivity {

    public static final String BUNDLE_IDENTIFIER = "NeighbourDetailActivityBundle";
    public static final String SERIALIZABLE_IDENTIFIER = "NeighbourDetailActivitySerializable";
    public static final String FAVORITES_STRING = "PreferencesFavorites";
    private static final String TAG = "NeighbourDetailActivity";

    private Neighbour mNeighbour;
    private FavoritesManager mFormatter;

    private SharedPreferences mSharedPreferences;

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    private ImageButton mBackButton;
    private ImageView mAvatar;
    private FloatingActionButton mFavButton;
    private TextView mAdress;
    private TextView mPhone;
    private TextView mWebProfile;
    private TextView mDescription;
    private TextView mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_detail);

        setSupportActionBar(findViewById(R.id.toolbar));

        mSharedPreferences = this.getSharedPreferences(getString(R.string.SHAREDP_FAVORITES), MODE_PRIVATE);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra(BUNDLE_IDENTIFIER);
        this.mNeighbour = (Neighbour) bundle.getSerializable(SERIALIZABLE_IDENTIFIER);

        mFormatter = new FavoritesManager();

        this.mBackButton = findViewById(R.id.image_details_back_arrow);
        this.mAvatar = findViewById(R.id.neighbour_avatar_image);
        this.mFavButton = findViewById(R.id.fav_neighbour_button);
        this.mAdress = findViewById(R.id.detail_location);
        this.mPhone = findViewById(R.id.detail_phone);
        this.mWebProfile = findViewById(R.id.detail_network);
        this.mDescription = findViewById(R.id.detail_description);
        this.mName = findViewById(R.id.detail_name);
        final Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(mNeighbour.getName());
        mAdress.setText(mNeighbour.getAddress());
        mPhone.setText(mNeighbour.getPhoneNumber());
        mWebProfile.setText("www.facebook.fr/" + mNeighbour.getName());
        mDescription.setText(mNeighbour.getAboutMe());
        mName.setText(mNeighbour.getName());

        Glide.with(this).load(mNeighbour.getAvatarUrl()).placeholder(R.drawable.ic_account)
                .apply(RequestOptions.centerCropTransform()).into(mAvatar);
        updateFavButton();

        mBackButton.setOnClickListener(v -> finish());

        mFavButton.setOnClickListener(v -> {
            if (getFavorites().contains(mNeighbour.getId())) {
                List<Long> favorites = getFavorites();
                favorites.remove(mNeighbour.getId());

                mSharedPreferences.edit().putString(FAVORITES_STRING, mFormatter.getFavoritesString(favorites)).apply();
            } else {
                List<Long> favorites = getFavorites();
                favorites.add(mNeighbour.getId());

                mSharedPreferences.edit().putString(FAVORITES_STRING, mFormatter.getFavoritesString(favorites)).apply();
            }

            updateFavButton();
        });
    }

    private String getFavoritesString() {
        return mSharedPreferences.getString(FAVORITES_STRING, "");
    }

    private List<Long> getFavorites() {
        String favoritesStr = getFavoritesString();

        List<Long> favorites = mFormatter.getFavorites(favoritesStr);

        return favorites;
    }

    private void updateFavButton() {
        mFavButton.setImageResource(checkFavorite() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp);
        mFavButton.setContentDescription(checkFavorite() ? getString(R.string.star_button_description_is_favorite) : getString(R.string.star_button_description_is_not_favorite));
    }

    private boolean checkFavorite() {
        return getFavorites().contains(mNeighbour.getId());
    }

    public Neighbour getNeighbour() {
        return mNeighbour;
    }

    public void setNeighbour(Neighbour neighbour) {
        mNeighbour = neighbour;
    }
}

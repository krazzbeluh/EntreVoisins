package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

public class NeighbourDetailActivity extends AppCompatActivity {

    public static final String BUNDLE_IDENTIFIER = "NeighbourDetailActivityBundle";
    public static final String SERIALIZABLE_IDENTIFIER = "NeighbourDetailActivitySerializable";
    private static final String TAG = "NeighbourDetailActivity";

    private Neighbour mNeighbour;

    private ImageButton mBackButton;
    private ImageView mAvatar;
    private FloatingActionButton mFavButton;
    private TextView mAdress;
    private TextView mPhone;
    private TextView mWebProfile;
    private TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_detail);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra(BUNDLE_IDENTIFIER);
        this.mNeighbour = (Neighbour) bundle.getSerializable(SERIALIZABLE_IDENTIFIER);

        this.mBackButton = findViewById(R.id.image_details_back_arrow);
        this.mAvatar = findViewById(R.id.neighbour_avatar_image);
        this.mFavButton = findViewById(R.id.fav_neighbour_button);
        this.mAdress = findViewById(R.id.detail_location);
        this.mPhone = findViewById(R.id.detail_phone);
        this.mWebProfile = findViewById(R.id.detail_network);
        this.mDescription = findViewById(R.id.detail_description);
        final Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(mNeighbour.getName());
        mAdress.setText(mNeighbour.getAddress());
        mPhone.setText(mNeighbour.getPhoneNumber());
        mWebProfile.setText("www.facebook.fr/" + mNeighbour.getName());
        mDescription.setText(mNeighbour.getAboutMe());

        Glide.with(this).load(mNeighbour.getAvatarUrl()).placeholder(R.drawable.ic_account)
                .apply(RequestOptions.centerCropTransform()).into(mAvatar);
        updateFavButton();

        mBackButton.setOnClickListener(v -> finish());

        mFavButton.setOnClickListener(v -> {
            getPreferences(MODE_PRIVATE).edit().putBoolean(String.valueOf(mNeighbour.getId()), !checkFavorite()).apply();
            updateFavButton();
        });
    }

    private void updateFavButton() {
        mFavButton.setImageResource(checkFavorite() ? R.drawable.ic_star_white_24dp : R.drawable.ic_star_border_white_24dp);
    }

    private boolean checkFavorite() {
        return getPreferences(MODE_PRIVATE).getBoolean(String.valueOf(mNeighbour.getId()), false);
    }

    public Neighbour getNeighbour() {
        return mNeighbour;
    }

    public void setNeighbour(Neighbour neighbour) {
        mNeighbour = neighbour;
    }
}

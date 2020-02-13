package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.model.Neighbour;

public class NeighbourDetailActivity extends AppCompatActivity {

    public static final String BUNDLE_IDENTIFIER = "NeighbourDetailActivityBundle";
    public static final String SERIALIZABLE_IDENTIFIER = "NeighbourDetailActivitySerializable";
    private static final String TAG = "NeighbourDetailActivity";

    private Neighbour mNeighbour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_detail);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra(BUNDLE_IDENTIFIER);
        this.mNeighbour = (Neighbour) bundle.getSerializable(SERIALIZABLE_IDENTIFIER);
    }
}

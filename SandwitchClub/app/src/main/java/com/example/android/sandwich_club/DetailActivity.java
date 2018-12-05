package com.example.android.sandwich_club;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.example.android.sandwich_club.model.Sandwich;
import com.example.android.sandwich_club.utils.JsonUtils;

import java.io.Console;
import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    ImageView mImageView;
    TextView mIngradientsTextView;
    TextView mAKATextView;
    TextView mDescriptionTextView;
    TextView mPOOTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(this, sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Context context, Sandwich sandwich) {

        if (sandwich == null) {
            closeOnError();
            return;
        }

        mPOOTextView         = (TextView) findViewById(R.id.origin_tv);
        mDescriptionTextView = (TextView) findViewById(R.id.description_tv);
        mAKATextView         = (TextView) findViewById(R.id.also_known_tv);
        mIngradientsTextView = (TextView) findViewById(R.id.ingredients_tv);
        mImageView           = (ImageView) findViewById(R.id.image_iv);

        for (String ingredient: sandwich.getIngredients()) {
            mIngradientsTextView.append(ingredient + "\n");
        }

        mDescriptionTextView.setText(sandwich.getDescription());

        for (String aka: sandwich.getAlsoKnownAs()) {
            mAKATextView.append(aka + "\n");
        }
        mPOOTextView.setText(sandwich.getPlaceOfOrigin());

        Picasso.with(context).load(Uri.parse(sandwich.getImage())).into(mImageView);
        
    }
}

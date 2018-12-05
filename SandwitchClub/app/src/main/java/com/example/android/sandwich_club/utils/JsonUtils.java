package com.example.android.sandwich_club.utils;

import android.util.Log;

import com.example.android.sandwich_club.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwitch = new Sandwich();

        try {
            JSONObject jsonSandwich = new JSONObject(json);

            JSONObject name = jsonSandwich.getJSONObject("name");
            String mainName = name.getString("mainName");
            JSONArray aKA = name.getJSONArray("alsoKnownAs");


            String pOO = jsonSandwich.getString("placeOfOrigin");
            String description = jsonSandwich.getString("description");
            String imageURl = jsonSandwich.getString("image");
            JSONArray ingredients = jsonSandwich.getJSONArray("ingredients");

            ArrayList<String> akaList = new ArrayList<String>();
            if (aKA != null) {
                for (int i=0;i<aKA.length();i++){
                    akaList.add(aKA.getString(i));
                }
            }

            ArrayList<String> ingredientsList = new ArrayList<String>();
            if (ingredients != null) {
                for (int i=0;i<ingredients.length();i++){
                    ingredientsList.add(ingredients.getString(i));
                }
            }

            sandwitch.setAlsoKnownAs(akaList);
            sandwitch.setDescription(description);
            sandwitch.setImage(imageURl);
            sandwitch.setIngredients(ingredientsList);
            sandwitch.setMainName(mainName);
            sandwitch.setPlaceOfOrigin(pOO);

        } catch (Exception e) {
            Log.e("JsonUtils","JSON PARSING EXCEPTION");
        }


        return sandwitch;
    }
}

package com.example.mrityunjay.bakingapplication.utils;

import android.content.Context;

import com.example.mrityunjay.bakingapplication.data_model.Ingredients;
import com.example.mrityunjay.bakingapplication.data_model.ResponseHandler;
import com.google.gson.Gson;

import java.util.List;

public class SharedPreferences {

    public static String BAKING_SHARED_PREFERENCES = "bakingSharedPrefs";

    public static void saveRecipeInPreferences(Context context, ResponseHandler recipe){
        Gson gson = new Gson();
        String json = gson.toJson(recipe);
        android.content.SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);

        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.KEY_RECIPE, json);
        editor.apply();
    }

    public static ResponseHandler getRecipeFromPreferences(Context context){
        android.content.SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);
        String json = preferences.getString(Constants.KEY_RECIPE, "");
        return new Gson().fromJson(json, ResponseHandler.class);
    }

    public static List<Ingredients> getIngredientListFromPreferences(Context context){
        ResponseHandler recipe = getRecipeFromPreferences(context);
        if (recipe == null){
            return null;
        }

        return getRecipeFromPreferences(context).getIngredients();
    }

    public static String getRecipeNameFromPreferences(Context context){
        ResponseHandler recipe = getRecipeFromPreferences(context);
        if (recipe == null){
            return "";
        }

        return recipe.getName();
    }

    public static void removeFromPreferences(Context context){
        android.content.SharedPreferences preferences = context.getSharedPreferences(
                BAKING_SHARED_PREFERENCES, 0);

        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}

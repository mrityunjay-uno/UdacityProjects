package com.example.mrityunjay.bakingapplication.data_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ResponseHandler implements Parcelable {

    private String name;
    private int servings;
    private String image;
    private List<Ingredients> ingredients;
    private List<Steps> steps;

    protected ResponseHandler(Parcel in) {
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        ingredients = in.createTypedArrayList(Ingredients.CREATOR);
        steps = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Creator<ResponseHandler> CREATOR = new Creator<ResponseHandler>() {
        @Override
        public ResponseHandler createFromParcel(Parcel in) {
            return new ResponseHandler(in);
        }

        @Override
        public ResponseHandler[] newArray(int size) {
            return new ResponseHandler[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public List<Steps> getStepsWithIngredients() {
        Steps ingredientMasterListItem = new Steps("Ingredients");
        List<Steps> stepsWithIngredient = new ArrayList<>();
        stepsWithIngredient.add(0, ingredientMasterListItem);
        stepsWithIngredient.addAll(1, steps);
        return stepsWithIngredient;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", servings=" + servings +
                ", image='" + image + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }
}

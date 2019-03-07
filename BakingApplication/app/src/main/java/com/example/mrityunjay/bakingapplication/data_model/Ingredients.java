package com.example.mrityunjay.bakingapplication.data_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Ingredients implements Parcelable {

    private static final String MEASURE_UNIT = "UNIT";
    private static final String MEASURE_CUP = "CUP";

    private float quantity;
    private String measure;
    private String ingredient;

    protected Ingredients(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

    public String getQuantity() {
        return String.format(Locale.getDefault(), "%.0f", quantity);
    }

    public String getMeasureUnit() {
        if(measure.contentEquals(MEASURE_UNIT)) return "";
        else if (measure.contentEquals(MEASURE_CUP) && quantity > 1)
            return measure.toLowerCase() + "s";

        return measure.toLowerCase();
    }

    public String getIngredient() {
        return ingredient.substring(0,1).toUpperCase() + ingredient.substring(1);
    }

    public String getQuantityWithMeasure(){
        String formattedQuantityWithMeasure = getQuantity();
        String measure = getMeasureUnit();

        if (!measure.isEmpty()){
            return formattedQuantityWithMeasure + " " + measure;
        }

        return formattedQuantityWithMeasure;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}

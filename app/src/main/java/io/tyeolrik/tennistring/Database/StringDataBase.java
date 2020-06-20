package io.tyeolrik.tennistring.Database;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class StringDataBase {
    private static StringDataBase instance;

    private static ArrayList<String> brands = new ArrayList<>();

    private static HashMap<String, ArrayList<String>> brandAndName = new HashMap<>();

    public static StringDataBase getInstance() {
        if (instance == null) {
            instance = new StringDataBase();
        }
        return instance;
    }

    private StringDataBase() {

    }

    public void putBrandAndName(String brand, String name) {
        if (brandAndName.get(brand) == null) {
            brandAndName.put(brand, new ArrayList<String>());
            brands.add(brand);
        }
        brandAndName.get(brand).add(name);
        // Log.d("putBrandAndName", "Brand: " + brand + "  Name: " + name);
    }

    public static HashMap<String, ArrayList<String>> getBrandAndName() {
        return brandAndName;
    }

    public static ArrayList<String> getBrands() {
        return brands;
    }

    public static ArrayList<String> getNamesInBrands(String brand) {
        return brandAndName.get(brand);
    }
}

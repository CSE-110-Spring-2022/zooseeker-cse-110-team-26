package com.example.zooapplication;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Arrays;
import java.util.List;

public class Converters {
    @TypeConverter
    public static String convertToString(List<String> tags){
        String value = "";
        for(String s : tags){
            value += s + ",";
        }
        return value;
    }
    @TypeConverter
    public static List<String> convertToList(String value){
        List<String> list = Arrays.asList(value.split("\\s*,\\s*"));
        return list;
    }
}

/**
 * This file contains the the methods to convert between different data types
 */
package com.example.zooapplication;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Arrays;
import java.util.List;

public class Converters {
    /**
     * Converts a List of Strings into one String
     *
     * @param tags
     * @return String
     */
    @TypeConverter
    public static String convertToString(List<String> tags){
        String value = "";
        for(String s : tags){
            value += s + ",";
        }
        return value;
    }
    /**
     * Converts a String back into a List of strings
     *
     * @param value
     * @return List<String>
     */
    @TypeConverter
    public static List<String> convertToList(String value){
        List<String> list = Arrays.asList(value.split("\\s*,\\s*"));
        return list;
    }
}

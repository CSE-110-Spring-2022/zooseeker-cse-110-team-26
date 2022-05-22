package com.example.zooapplication;

import java.util.ArrayList;
import java.util.List;

public class DetailedtoBrief {
    public static String toBrief(String detailed) {
        String[] split = detailed.split("\n");
        List<String> splitArrayList = new ArrayList<>();
        for(String direction: split) {
            splitArrayList.add(direction);
        }
        for(int index = splitArrayList.size() - 1; index >= 0; index--) {
            if(splitArrayList.get(index).charAt(0) == 'C') {
                double totalSubDistance = findNum(splitArrayList.get(index));
                int currIndex = index - 1;
                splitArrayList.remove(index);
                while(splitArrayList.get(currIndex).charAt(0) == 'C') {
                    totalSubDistance += findNum(splitArrayList.get(currIndex));
                    splitArrayList.remove(currIndex);
                    currIndex--;
                }
                index = currIndex;
                splitArrayList.set(index, editNum(totalSubDistance, splitArrayList.get(index)));
            }
        }
        String toReturn = "";
        for(String s: splitArrayList) {
            toReturn += s;
        }
        return toReturn;
    }
    public static String editNum(double num, String old) {
        String replacement = old.replaceAll(String.valueOf(findNum(old)), String.valueOf(num + findNum(old)));
        return replacement;
    }
    public static double findNum(String direction) {
        String numString = "";
        int startIndex = 0, endIndex = 0;
        for(int charIndex = 0; charIndex < direction.length(); charIndex++) {
            int asciiValue = direction.charAt(charIndex);
            if (asciiValue >= 48 && asciiValue <= 57) {
                startIndex = charIndex;
                int currIndex = charIndex;
                while(currIndex < direction.length()) {
                    if (direction.charAt(currIndex) == 'f') {
                        endIndex = currIndex;
                        break;
                    }
                    currIndex++;
                }
                break;
            }
        }
        for (int currIndex = startIndex; currIndex < endIndex; currIndex++) {
            numString = numString + direction.charAt(currIndex);
        }
        return Double.valueOf(numString);
    }
}

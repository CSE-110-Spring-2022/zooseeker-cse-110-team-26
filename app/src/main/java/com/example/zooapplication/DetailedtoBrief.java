package com.example.zooapplication;

import java.util.*;

public class DetailedtoBrief {
    public static String toBrief(String detailed) {
        Set<Integer> deletedIndices = new HashSet<Integer>();
        String[] split = detailed.split("\n");
        List<String> splitArrayList = new ArrayList<>();
        for(String direction: split) {
            splitArrayList.add(direction);
        }
        for(int index = 0; index < splitArrayList.size(); index++) {
            if(splitArrayList.get(index).charAt(0) == 'P') {
                double distance = findNum(splitArrayList.get(index));
                int currPointer = index + 1;
                while(currPointer < splitArrayList.size() && splitArrayList.get(currPointer).charAt(0) == 'C') {
                    deletedIndices.add(currPointer - 1);
                    distance += findNum(splitArrayList.get(currPointer));
                    currPointer++;
                }
                splitArrayList.set(currPointer - 1, editNum(distance,splitArrayList.get(currPointer - 1)));
                splitArrayList.set(currPointer - 1, editStartWord(splitArrayList.get(currPointer - 1)));
            }
        }
        for(int index = splitArrayList.size() - 1; index >= 0; index--) {
            if(deletedIndices.contains(index)) {
                splitArrayList.remove(index);
            }
        }
        String toReturn = "";
        for(String s: splitArrayList) {
            toReturn += s + "\n";
        }
        return toReturn;
    }
    public static String editNum(double num, String old) {
        String replacement = old.replaceAll(String.valueOf(findNum(old)), String.valueOf(num));
        return replacement;
    }
    public static String editStartWord(String old) {
        String replacement = old.replaceFirst("Continue", "Proceed");
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

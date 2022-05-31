package com.example.zooapplication;

import java.util.*;

public class DetailedtoBrief {
    /**
     * Converts a string of detailed directions to brief directions
     *
     * @param detailed directions
     * @return brief directions
     */
    public static String toBrief(String detailed) {
        //Set to keep to track of what indices to delete after iteration
        Set<Integer> deletedIndices = new HashSet<Integer>();
        //Split the directions into sentences
        String[] split = detailed.split("\n");
        //convert into an array list for mutation
        List<String> splitArrayList = new ArrayList<>();
        for(String direction: split) {
            splitArrayList.add(direction);
        }
        // This for loop iterattes
        for(int index = 0; index < splitArrayList.size(); index++) {
            //Finds a Proceed and tracks all of the Conintues below it
            if(splitArrayList.get(index).charAt(0) == 'P') {
                double distance = findNum(splitArrayList.get(index));
                int currPointer = index + 1;
                while(currPointer < splitArrayList.size() && splitArrayList.get(currPointer).charAt(0) == 'C') {
                    //Deletes the continues
                    deletedIndices.add(currPointer - 1);
                    //Keeps track of the total distance on the same street
                    distance += findNum(splitArrayList.get(currPointer));
                    currPointer++;
                }
                //Changes the directions sentence to update to the right distance and start word
                splitArrayList.set(currPointer - 1, editNum(distance,splitArrayList.get(currPointer - 1)));
                splitArrayList.set(currPointer - 1, editStartWord(splitArrayList.get(currPointer - 1)));
            }
        }
        //Deletes all the continues
        for(int index = splitArrayList.size() - 1; index >= 0; index--) {
            if(deletedIndices.contains(index)) {
                splitArrayList.remove(index);
            }
        }
        String toReturn = "";
        //Reconstruct the brief directions to return
        for(String s: splitArrayList) {
            toReturn += s + "\n";
        }
        return toReturn;
    }
    /**
     * Changes the distance in a given directions sentence
     *
     * @param num: number to replace the old number,
     *        old: direction sentence to edit
     * @return correct direction
     */
    public static String editNum(double num, String old) {
        String replacement = old.replaceAll(String.valueOf(findNum(old)), String.valueOf(num));
        return replacement;
    }
    /**
     * Changes the starting word given a direction line
     *
     * @param old: direction to replace
     * @return correct direction
     */
    public static String editStartWord(String old) {
        String replacement = old.replaceFirst("Continue", "Proceed");
        return replacement;
    }
    /**
     * Retrieves the distance number in a direction
     *
     * @param direction: direction to find the number
     * @return the direction number
     */
    public static double findNum(String direction) {
        String numString = "";
        int startIndex = 0, endIndex = 0;
        for(int charIndex = 0; charIndex < direction.length(); charIndex++) {
            int asciiValue = direction.charAt(charIndex);
            //When a number is found, record the substring until an 'f' is found.
            //Loop finds the index when the number ends
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
        //Constructs the number and returns it as a double
        for (int currIndex = startIndex; currIndex < endIndex; currIndex++) {
            numString = numString + direction.charAt(currIndex);
        }
        return Double.valueOf(numString);
    }
}

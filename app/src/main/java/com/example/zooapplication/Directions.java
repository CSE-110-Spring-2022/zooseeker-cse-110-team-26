/**
 * This file contains the functions findPath and findDistance, where findPath finds the specific
 * route to take to get from the start point to the end point, and findDistance returns the
 * total travel distance of that path. It is used by other classes to find optimized routes from one
 * location to another.
 */
package com.example.zooapplication;


import android.util.Log;

import com.google.gson.Gson;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * find the closest exhibit and the path base on current location
 */
public class Directions {
    /**
     * Finds the path from current position to destination
     *
     * @param start, end g, vInfo, eInfo
     * @return String directions from start to end
     */
    public static String findPath(String start, String end, Graph g, Map<String,
            ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo){
        String plan = "";
        GraphPath<String, IdentifiedWeightedEdge> shortestPath =
                DijkstraShortestPath.findPathBetween(g, start, end);
        String currentLoc = start;
        String previous_edge = "";
        //Goes through every edge (as there can be multiple) to get from start to end location
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()) {
            String startWord = "Proceed";
            String current_edge = eInfo.get(e.getId()).street;
            if(previous_edge.equals(current_edge)) {
                startWord = "Continue";
            }

            String correctTarget = vInfo.get(g.getEdgeTarget(e).toString()).name;
            //Log.d("test", correctTarget);
            //Log.d("test", vInfo.get(g.getEdgeTarget(e).toString()).name);
            //Log.d("test", vInfo.get(g.getEdgeSource(e).toString()).name);
            if(currentLoc.equals(vInfo.get(g.getEdgeTarget(e).toString()).id)
                    || currentLoc.equals(vInfo.get(g.getEdgeTarget(e).toString()).name)) {
                correctTarget = vInfo.get(g.getEdgeSource(e).toString()).name;
            }
            currentLoc = correctTarget;
            //Concatenates instructions onto the returned String
            plan += startWord + " on " + current_edge + " "+ g.getEdgeWeight(e) + " ft towards "
                    + correctTarget + "\n";
            //    edge name, edge weight, node name,
            previous_edge = current_edge;

        }
        return plan;
    }

    /**
     * Finds the distance from current position to destination
     *
     * @param start, end g, vInfo, eInfo
     * @return int distance
     */
    public static int findDistance(String start, String end, Graph g, Map<String,
            ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo) {
        GraphPath<String, IdentifiedWeightedEdge> shortestPath =
                DijkstraShortestPath.findPathBetween(g, start, end);
        int distance = 0;
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()){
            distance += g.getEdgeWeight(e);
        }
        return distance;
    }


    public static String findStreet(String exhibit,Graph g, Map<String,
            ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> edgeInfo){

        //getting the edges that connect to the given exhibit
        Set<String> edges = g.edgesOf(exhibit);

        Iterator val = edges.iterator();

        System.out.println("The iterator values are: ");
        String id = "";
        String iter;
        List<String> edgeIds = new ArrayList<>();

        //Iterating through the edges iterator and getting the ids of the edges for each
        while (val.hasNext()) {
            iter = val.next().toString();

            /*g.edgesOf returns edges like this (flamingo :flamingo_to_capuchin: capuchin)
              - cutting string using delimeter, then taking the index 1 which is id
             */
            String [] arr = iter.split(":");
            edgeIds.add(arr[1]);
            id = arr[1];

            System.out.println(iter);
        }
        String street = null;
        String curr = null;

        //Checking to see if the street names are the same or not
        for (int i = 0; i < edgeIds.size(); i++) {

            if(street != null && !street.equals(edgeInfo.get(edgeIds.get(i)).street)){
                street = street + "/" + edgeInfo.get(edgeIds.get(i)).street;
            }
            else{
                street = edgeInfo.get(edgeIds.get(i)).street;
            }
        }


        return street;
    }
}
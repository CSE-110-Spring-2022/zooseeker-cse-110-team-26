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
import java.util.HashMap;
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
        if(start.equals(end)){
            return "We are already here " + end;
        }
        Gson gson = new Gson();
        String temp = ShareData.getGroup(App.getContext(), "group");
        //allow for testing

            Map<String, String> map = gson.fromJson(temp, HashMap.class);
            String plan = "";
            if(map != null) {
                if (map.containsKey(start)) {
                    start = map.get(start);
                }

                if (map.containsKey(end)) {
                    end = map.get(end);
                }
            }
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
        Gson gson = new Gson();
        String temp = ShareData.getGroup(App.getContext(), "group");
        Map<String, String> map = gson.fromJson(temp, HashMap.class);
        if(map != null) {
            if (map.containsKey(start)) {
                start = map.get(start);
            }

            if (map.containsKey(end)) {
                end = map.get(end);
            }
        }

        GraphPath<String, IdentifiedWeightedEdge> shortestPath =
                DijkstraShortestPath.findPathBetween(g, start, end);
        int distance = 0;
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()){
            distance += g.getEdgeWeight(e);
        }
        return distance;
    }

    /**
     * Finds the street/intersection in which the exhibit is located in
     *
     * @param exhibit, end g, vInfo, edgeInfo
     * @return String street/intersection
     */
    public static String findStreet(String exhibit,Graph g, Map<String,
            ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> edgeInfo){


        //checks if the given exhibit exist as node in the graph if not it just returns the name of the edge, which is the street
        if (!g.containsVertex(exhibit)) {
            return edgeInfo.get(exhibit).street;
        }


        //getting the edges that connect to the given exhibit
        Set<String> edges = g.edgesOf(exhibit);

        Iterator val = edges.iterator();


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

//            System.out.println(iter);
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

    /**
     * Finds the ID of the given exhibit
     *
     * @param exhibit, end g, vInfo, edgeInfo
     * @return String street/intersection
     */
    public static String getID (String exhibit,Graph g, Map<String,
            ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> edgeInfo){
        Gson gson = new Gson();
        String temp = ShareData.getGroup(App.getContext(), "group");
        Map<String, String> map = gson.fromJson(temp, HashMap.class);


        //if exhibit vertex exists in the graph, returns the id exhibit given, if not it searches for id in the map
        if(g.containsVertex(exhibit)){
//             System.out.println("Contains");
            return exhibit;
        }
        else{
//            System.out.println("DOES NOT Contains");
            String real = map.get(exhibit);
            System.out.println(real);

            return real;
        }


    }
}
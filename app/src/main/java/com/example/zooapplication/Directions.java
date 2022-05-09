/**
 * This file contains the functions findPath and findDistance, where findPath finds the specific
 * route to take to get from the start point to the end point, and findDistance returns the
 * total travel distance of that path. It is used by other classes to find optimized routes from one
 * location to another.
 */
package com.example.zooapplication;


import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
            Log.d("test", correctTarget);
            Log.d("test", vInfo.get(g.getEdgeTarget(e).toString()).name);
            Log.d("test", vInfo.get(g.getEdgeSource(e).toString()).name);
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
}
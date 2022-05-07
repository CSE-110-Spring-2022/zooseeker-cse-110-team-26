package com.example.zooapplication;


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
    //find the path from current position to destination
    public static String findPath(String start, String end, Graph g, Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo){
        String plan = "";
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(g, start, end);
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()) {
            String previous_edge = "";
            String startWord = "Proceed";
            String current_edge = eInfo.get(e.getId()).street;
            if(previous_edge == current_edge) {
                startWord = "Continue";
            }
            plan += startWord + " on " + current_edge + " "+ g.getEdgeWeight(e) + " ft towards " + vInfo.get(g.getEdgeTarget(e).toString()).name + "\n";
            //    edge name, edge weight, node name,
            previous_edge = current_edge;
        }
        return plan;
    }

    //find the closest exhibit base on current location
    public static int findDistance(String start, String end, Graph g, Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo) {
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(g, start, end);
        int distance = 0;
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()){
            distance += g.getEdgeWeight(e);
        }
        return distance;
    }
}
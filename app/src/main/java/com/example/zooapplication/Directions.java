package com.example.zooapplication;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Directions {
    public static List<String> printDirections(String start, List<String> unvisited, Graph g, Map<String, ZooData.VertexInfo> vInfo, Map<String, ZooData.EdgeInfo> eInfo) {
        List<String> directions = new ArrayList<String>();
        float maximum = Integer.MAX_VALUE;
        String nearestNeighbor;
        GraphPath<String, IdentifiedWeightedEdge> shortestPath = null;
        for(String dest: unvisited) {
            GraphPath<String, IdentifiedWeightedEdge> path =
                    DijkstraShortestPath.findPathBetween(g, start, dest);
            int totalDistance = 0;
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                totalDistance += g.getEdgeWeight(e);
            }
            if (totalDistance < maximum) {
                maximum = totalDistance;
                nearestNeighbor = dest;
                shortestPath = path;
            }
        }
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()) {
            String previous_edge = "";
            String startWord = "Proceed";
            String current_edge = eInfo.get(e.getId()).street;
            if(previous_edge == current_edge) {
                startWord = "Continue";
            }
            System.out.printf("%s on %s %.0f ft towards %s", startWord,
                    current_edge, g.getEdgeWeight(e), vInfo.get(g.getEdgeTarget(e).toString()).name);
            directions.add(startWord + " on " + current_edge + " "+ g.getEdgeWeight(e) + " ft towards " + vInfo.get(g.getEdgeTarget(e).toString()).name);
            //    edge name, edge weight, node name,
            previous_edge = current_edge;
        }
        return directions;
    }

}
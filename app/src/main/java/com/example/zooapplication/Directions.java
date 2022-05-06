package com.example.zooapplication;

import java.util.List;

public class Directions {
    public void printDirections(String start, List<String> unvisited, Graph g, Map<T> vInfo, Map<T> eInfo) {
        float maximum = Integer.MAX_VALUE;
        String nearestNeighbor;
        GraphPath<String, IndentifiedWeightedEdge> shortestPath;
        for(String exhibit: unvisited) {
            GraphPath<String, IdenditifiedWeightedEdge> path =
                    DijkstraShortestPath.findPathBetween(g, start, exhbit);
            totalDistance = 0;
            for (IdentifiedWeightedEdge e : path.getEdgeList()) {
                totalDistance += g.getEdgeWeight(e);
            }
            if (totalDistance < maximum) {
                maximum = totalDistance;
                nearestNeighbor = exhibit;
                shortestPath = path;
            }
        }
        for(IdentifiedWeightedEdge e: shortestPath.getEdgeList()) {
            String previous_edge "";
            String startWord = "Proceed";
            String current_edge = eInfo.get(e.getId()).street;
            if(previous_edge == current_edge) {
                startWord = "Continue";
            }
            printf("%s on %s %.0f ft towards %s", startWord,
                    current_edge, g.getEdgeWeight(e), vinfo.get(g.getEdgeTarget(e).toString()).name);
        //    edge name, edge weight, node name,
            previous_edge = current_edge;
        }

    }

}

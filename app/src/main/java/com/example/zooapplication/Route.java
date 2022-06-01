package com.example.zooapplication;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class name: Route.java
 * Description: Given a unsorted plan list, we need to sort the list base on the distance
 *              Given a list, we need to create a route.
 */
public class Route {
    /**
     * Using the graph to sort the list base on the distance
     * @param id unsorted list
     * @param copyStart starting point
     * @param g the whole graph
     * @param vertexInfo all vertex
     * @param edgeInfo all edge
     * @return a sorted list
     */
    public static List<String> sortExhibits(List<String> id, String copyStart, Graph g, Map<String,
            ZooData.VertexInfo> vertexInfo, Map<String, ZooData.EdgeInfo> edgeInfo){
        String temp = ShareData.getGroup(App.getContext(), "group");
        Gson gson = new Gson();
        Map<String, String> group = gson.fromJson(temp, HashMap.class);
        List<String> sortUnvisited = new LinkedList<>();
        //sortUnvisited.add(copyStart);
        String endPoint = copyStart;
        //Creates the route and puts the order in which we visit the animals into sortUnvisited list
        while(id.size() > 0){
            int distance = Integer.MAX_VALUE;
            int count = 0;
            if (group != null && group.containsKey(copyStart)) {
                    copyStart = group.get(copyStart);
                }
                while (count < id.size()) {
                    String end = id.get((count));
                    if (group != null && group.containsKey(id.get(count))) {
                        end = group.get(id.get(count));
                    }
                    int tempDis = Directions.findDistance
                            (copyStart, end, g, vertexInfo, edgeInfo);
                    //Takes smallest distance
                    if (tempDis < distance) {
                        endPoint = id.get(count);
                        distance = tempDis;
                    }

                    count++;
                }

            sortUnvisited.add(endPoint);
            id.remove(endPoint);
            copyStart = endPoint;
        }
        return sortUnvisited;
    }

    /**
     * Create a route by using a graph.
     * @param sortUnvisited Use the order of the list to create a route
     * @param startExhibit Starting point
     * @param g the whole graph
     * @param vertexInfo all vertex
     * @param edgeInfo alledge
     * @return a list that contains the description of the route
     */
    public static List<String> createRoute(List<String> sortUnvisited, String startExhibit,
                                           Graph g, Map<String, ZooData.VertexInfo> vertexInfo,
                                           Map<String, ZooData.EdgeInfo> edgeInfo){
        List<String> plan = new LinkedList<>();
        String temp = ShareData.getGroup(App.getContext(), "group");
        Gson gson = new Gson();
        Map<String, String> group = gson.fromJson(temp, HashMap.class);
        for(String s: sortUnvisited){
            if(group != null && group.containsKey(s)){
                s = group.get(s);
            }
            plan.add(Directions.findPath(startExhibit, s, g, vertexInfo, edgeInfo));
            startExhibit = s;
        }
        return plan;
    }
}

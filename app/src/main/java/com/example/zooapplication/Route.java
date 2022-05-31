package com.example.zooapplication;

import com.google.gson.Gson;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Route {
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

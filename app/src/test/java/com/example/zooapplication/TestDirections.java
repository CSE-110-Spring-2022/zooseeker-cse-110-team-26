package com.example.zooapplication;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class TestDirections {
    Graph<String, IdentifiedWeightedEdge> g;
    List<String> sampleList;
    Map<String, ZooData.VertexInfo> vInfo;
    Map<String, ZooData.EdgeInfo> eInfo;
    Context context;
    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        g = ZooData.loadZooGraphJSON("sample_zoo_graph.json",context);
        vInfo = ZooData.loadVertexInfoJSON("sample_node_info.json",context);
        eInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json",context);
    }


//    @Test
//    public void testSample(){
//        sampleList = new ArrayList<String>(){{
//            add("gators");
////            add("elephant_odyssey");
////            add("arctic_foxes");
////            add("gorillas");
//            add("lions");
//        }};
////        List<String> actual = Directions.printDirections("entrance_exit_gate",sampleList, g, vInfo, eInfo);
//        List<String> expected = new ArrayList<String>(){{
//            add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza");
//            add("Proceed on Reptile Road 100.0 ft towards Alligators");
//        }};
//        assertEquals(expected, actual);
//    }

    //make another test from gators --> lions
//    @Test
//    public void testSample2(){
//        sampleList = new ArrayList<String>(){{
////            add("gators");
////            add("elephant_odyssey");
////            add("arctic_foxes");
////            add("gorillas");
//            add("lions");
//        }};
//
//        List<String> actual = Directions.printDirections("gators",sampleList, g, vInfo, eInfo);
//        List<String> expected = new ArrayList<String>(){{
//            add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza");
//        }};
//        assertEquals(expected, actual);
//
//
//
//    }
}


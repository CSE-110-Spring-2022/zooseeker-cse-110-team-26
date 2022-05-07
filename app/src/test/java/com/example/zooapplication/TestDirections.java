package com.example.zooapplication;

import static org.junit.Assert.assertEquals;

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
    @Before
    public void setUp() {
        g = ZooData.loadZooGraphJSON("src/main/assets/sample_zoo_graph.json");
        sampleList = new ArrayList<String>(){{
            add("lion");
            add("elephant_odyssey");
            add("arctic_foxes");
            add("gorillas");
            add("gators");
        }};

        vInfo = ZooData.loadVertexInfoJSON("sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json");
    }


    @Test
    public void testSample(){
        List<String> actual = Directions.printDirections("entrance_exit_gate",sampleList, g, vInfo, eInfo);
        List<String> expected = new ArrayList<String>(){{
            add("Proceed on Entrance Way 10ft towards Entrance Plaza");
            add("Proceed on Reptile Road 100ft towards Alligators");
        }};
        assertEquals(actual, expected);
    }
}

package com.example.zooapplication;

import static com.example.zooapplication.Directions.findDistance;
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
public class TestFindDistance {
    Graph g;
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


    //Test from entrance/exit gate to the gorillas exhibit
    @Test
    public void testSample(){

        int acutal_distance = findDistance("entrance_exit_gate","gorillas",g,vInfo,eInfo);
        int expected_distance = 210;

        assertEquals(acutal_distance, expected_distance);
    }

    //Test from entrance/exit gate to the lions exhibit
    @Test
    public void testSample2(){

        int acutal_distance = findDistance("entrance_exit_gate","lions",g,vInfo,eInfo);
        int expected_distance = 310;

        assertEquals(acutal_distance, expected_distance);
    }

    //Test from entrance/exit gate to the elephants exhibit
    @Test
    public void testSample3(){

        int acutal_distance = findDistance("entrance_exit_gate","elephant_odyssey",g,vInfo,eInfo);
        int expected_distance = 510;

        assertEquals(acutal_distance, expected_distance);
    }

    //Test from elephants gate to the elephants exhibit
    @Test
    public void testSample4(){

        int acutal_distance = findDistance("elephant_odyssey","arctic_foxes",g,vInfo,eInfo);
        int expected_distance = 800;

        assertEquals(acutal_distance, expected_distance);
    }

}
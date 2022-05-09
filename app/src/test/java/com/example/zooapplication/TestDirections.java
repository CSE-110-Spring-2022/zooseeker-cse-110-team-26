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


    @Test
    public void testSample(){
        String actual = Directions.findPath("entrance_exit_gate","gorillas", g, vInfo, eInfo);
        String expected = "Proceed on Entrance Way 10.0 ft towards Entrance Plaza" + "\n";
        expected+="Proceed on Africa Rocks Street 200.0 ft towards Gorillas" + "\n";
        assertEquals(expected, actual);
    }

    //make another test from gators --> lions
    @Test
    public void testSample2(){
        String actual = Directions.findPath("gorillas","elephant_odyssey", g, vInfo, eInfo);
        String expected = "Proceed on Africa Rocks Street 200.0 ft towards Lions" + "\n";
        expected+="Continue on Africa Rocks Street 200.0 ft towards Elephant Odyssey" + "\n";
        assertEquals(expected, actual);
    }

    @Test
    public void testSample3(){
        String actual = Directions.findPath("elephant_odyssey","arctic_foxes", g, vInfo, eInfo);
        String expected = "Proceed on Africa Rocks Street 200.0 ft towards Lions" + "\n";
        expected+="Proceed on Sharp Teeth Shortcut 200.0 ft towards Alligators" + "\n";
        expected+="Proceed on Reptile Road 100.0 ft towards Entrance Plaza" + "\n";
        expected+="Proceed on Arctic Avenue 300.0 ft towards Arctic Foxes" + "\n";
        assertEquals(expected, actual);
    }

}
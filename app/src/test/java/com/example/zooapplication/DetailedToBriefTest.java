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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class DetailedToBriefTest {

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


    //Basic Sanity test of function
    @Test
    public void testSample1(){
        String test = "Proceed on Power Lane 200.50 ft towards Monkeys\nContinue on Power Lane 300.50 ft towards Goldfish";
        String expected = "Proceed on Power Lane 501.00 ft towards Goldfish\n";
        String actual = DetailedtoBrief.toBrief(test);
        assertEquals(expected, actual);
    }
    //Test of a more robust case
    @Test
    public void testSample2(){
        String test = "Proceed on Power Lane 200.50 ft towards Monkeys\nContinue on Power Lane 300.50 ft towards Goldfish\nContinue on Power Lane 400.00 ft towards Apes\nProceed on Treetops Way 100.00ft towards Birds";
        String expected = "Proceed on Power Lane 901.00 ft towards Apes\nProceed on Treetops Way 100.00ft towards Birds\n";
        String actual = DetailedtoBrief.toBrief(test);
        assertEquals(expected, actual);
    }

}
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


    //Test from entrance/exit gate to the gorillas exhibit
    //Why does CREATE ROUTE return the first index as empty
    @Test
    public void testSample(){

        List<String> ids = new ArrayList<>();
        ids.add("elephant_odyssey");
        ids.add("gorillas");

        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);
        List<String> route = Route.createRoute(srtEx,"entrance_exit_gate",g,vInfo,eInfo);



        List<String> expected = new LinkedList<>();
        expected.add("");
        expected.add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza\n" +
                "Proceed on Africa Rocks Street 200.0 ft towards Gorillas\n");
        expected.add("Proceed on Africa Rocks Street 400.0 ft towards Elephant Odyssey\n");

        List<String> brief = new LinkedList<>();
        brief.add("");

        for(int i = 1; i < route.size(); i++){
            brief.add(DetailedtoBrief.toBrief(route.get(i)));
        }

        assertEquals(expected,brief);


    }

}
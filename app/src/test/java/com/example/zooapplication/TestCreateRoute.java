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
public class TestCreateRoute {
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
        ids.add("arctic_foxes");


        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);
        List<String> route = Route.createRoute(srtEx,"entrance_exit_gate",g,vInfo,eInfo);

        route.forEach(System.out::println);

        List<String> expected = new LinkedList<>();
        expected.add("");
        expected.add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza\n" +
                "Proceed on Arctic Avenue 300.0 ft towards Arctic Foxes\n");

        assertEquals(expected,route);



    }

    @Test
    public void testSample2(){

        List<String> ids = new ArrayList<>();
        ids.add("arctic_foxes");
        ids.add("elephant_odyssey");


        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);
        List<String> route = Route.createRoute(srtEx,"entrance_exit_gate",g,vInfo,eInfo);

        route.forEach(System.out::println);

        List<String> expected = new LinkedList<>();
        expected.add("");
        expected.add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza\n" +
                "Proceed on Arctic Avenue 300.0 ft towards Arctic Foxes\n");
        expected.add("Proceed on Arctic Avenue 300.0 ft towards Entrance Plaza\n" +
                "Proceed on Reptile Road 100.0 ft towards Alligators\n" +
                "Proceed on Sharp Teeth Shortcut 200.0 ft towards Lions\n" +
                "Proceed on Africa Rocks Street 200.0 ft towards Elephant Odyssey\n");

        assertEquals(expected,route);



    }

    @Test
    public void testSample3(){

        List<String> ids = new ArrayList<>();
        ids.add("gorillas");
        ids.add("lions");
        ids.add("gators");


        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);
        List<String> route = Route.createRoute(srtEx,"entrance_exit_gate",g,vInfo,eInfo);

        route.forEach(System.out::println);

        List<String> expected = new LinkedList<>();
        expected.add("");
        expected.add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza\n" +
                "Proceed on Reptile Road 100.0 ft towards Alligators\n");
        expected.add("Proceed on Sharp Teeth Shortcut 200.0 ft towards Lions\n");
        expected.add("Proceed on Africa Rocks Street 200.0 ft towards Gorillas\n");

        expected.forEach(System.out::println);




        assertEquals(expected,route);



    }

    @Test
    public void testSample4(){

        List<String> ids = new ArrayList<>();
        ids.add("gorillas");
        ids.add("elephant_odyssey");
        ids.add("gators");
        ids.add("arctic_foxes");
        ids.add("lions");


        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);
        List<String> route = Route.createRoute(srtEx,"entrance_exit_gate",g,vInfo,eInfo);

        route.forEach(System.out::println);

        List<String> expected = new LinkedList<>();
        expected.add("");
        expected.add("Proceed on Entrance Way 10.0 ft towards Entrance Plaza\n" +
                "Proceed on Reptile Road 100.0 ft towards Alligators\n");
        expected.add("Proceed on Sharp Teeth Shortcut 200.0 ft towards Lions\n");
        expected.add("Proceed on Africa Rocks Street 200.0 ft towards Gorillas\n");
        expected.add("Proceed on Africa Rocks Street 200.0 ft towards Lions\n" +
                "Continue on Africa Rocks Street 200.0 ft towards Elephant Odyssey\n");
        expected.add("Proceed on Africa Rocks Street 200.0 ft towards Lions\n" +
                "Proceed on Sharp Teeth Shortcut 200.0 ft towards Alligators\n" +
                "Proceed on Reptile Road 100.0 ft towards Entrance Plaza\n" +
                "Proceed on Arctic Avenue 300.0 ft towards Arctic Foxes\n");

        assertEquals(expected,route);



    }
}
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
public class TestSortExhibits {
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
    public void testSampleSingle(){

        List<String> ids = new ArrayList<>();
        ids.add("arctic_foxes");

        ids.forEach(System.out::println);

        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);

        List<String> expected = new ArrayList<>();
        expected.add("entrance_exit_gate");
        expected.add("arctic_foxes");



        srtEx.forEach(System.out::println);

        assertEquals(expected,srtEx);


    }

    //Test from entrance/exit gate to the gorillas exhibit
    @Test
    public void testSample(){

        List<String> ids = new ArrayList<>();
        ids.add("arctic_foxes");
        ids.add("lions");
        ids.add("gorillas");

        ids.forEach(System.out::println);

        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);

        List<String> expected = new ArrayList<>();
        expected.add("entrance_exit_gate");
        expected.add("gorillas");
        expected.add("lions");
        expected.add("arctic_foxes");



        srtEx.forEach(System.out::println);

        assertEquals(expected,srtEx);


    }

    //Test from entrance/exit gate to the gorillas exhibit
    @Test
    public void testSample2(){

        List<String> ids = new ArrayList<>();
        ids.add("arctic_foxes");
        ids.add("gators");
        ids.add("elephant_odyssey");

        ids.forEach(System.out::println);

        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);

        List<String> expected = new ArrayList<>();
        expected.add("entrance_exit_gate");
        expected.add("gators");
        expected.add("arctic_foxes");
        expected.add("elephant_odyssey");



        srtEx.forEach(System.out::println);

        assertEquals(expected,srtEx);


    }

    //Test from entrance/exit gate to the gorillas exhibit
    @Test
    public void testSample4(){

        List<String> ids = new ArrayList<>();
        ids.add("elephant_odyssey");
        ids.add("arctic_foxes");


        ids.forEach(System.out::println);

        List<String> srtEx = Route.sortExhibits(ids,"entrance_exit_gate",g,vInfo,eInfo);

        List<String> expected = new ArrayList<>();
        expected.add("entrance_exit_gate");
        expected.add("arctic_foxes");
        expected.add("elephant_odyssey");



        srtEx.forEach(System.out::println);

        assertEquals(expected,srtEx);


    }


}
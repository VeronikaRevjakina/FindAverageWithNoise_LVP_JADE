package ru.spbu.mas.agent;

import jade.core.Agent;
import ru.spbu.mas.behaviour.FindAverage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MyAgent extends Agent {

    public String[] linkedAgents;
    public HashMap<String, Integer> agentsNumbers = new HashMap<>();
    public HashMap<String, Integer> freshAgentsNumbers = new HashMap<>();

    @Override
    protected void setup() {

        //first argument in agent declaration is value
        int myNumber=Integer.valueOf((String) getArguments()[0]);
        String myName=getAID().getLocalName();
        freshAgentsNumbers.put(myName,myNumber);

        //copy all linked agents from declaraion, from 1 parameter
        linkedAgents= Arrays.copyOfRange(getArguments(),1,
                getArguments().length,String[].class);

        addBehaviour(new FindAverage
                (this, TimeUnit.SECONDS.toMillis(1)));
    }

}


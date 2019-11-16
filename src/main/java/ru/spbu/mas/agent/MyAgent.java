package ru.spbu.mas.agent;

import jade.core.Agent;
import ru.spbu.mas.behaviour.FindAverage;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class MyAgent extends Agent {

    public String[] linkedAgents;
    private Double myNumber;

    public Double getMyNumber() {
        return this.myNumber;
    }

    public void setMyNumber(double myNumber) {
        this.myNumber = myNumber;
    }


    @Override
    protected void setup() {


        //first argument in agent declaration is value
        myNumber = Double.valueOf((String) getArguments()[0]);
        String myName = getAID().getLocalName();
        System.out.println("Agent: " + myName + " initial number = " + myNumber);


        //copy all linked agents from declaraion, from 1 parameter
        linkedAgents = Arrays.copyOfRange(getArguments(), 1,
                getArguments().length, String[].class);

        addBehaviour(new FindAverage
                (this, TimeUnit.SECONDS.toMillis(1)));
    }
}


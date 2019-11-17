package ru.spbu.mas.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ru.spbu.mas.agent.MyAgent;
import ru.spbu.mas.main.ContainerKiller;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FindAverage extends TickerBehaviour {
    // 1->2 breakable connection
    private final static double PROBABILITY_OF_CONNECTION_BREAK = 0.3;
    // 2->3 delay connection
    private final static double PROBABILITY_OF_DELAY = 0.25;
    private final static int MAX_DELAY = 10;
    private static final double DEFAULT_ALPHA = 0.2;

    private final MyAgent agent;
    private int currentStep = 0;
    private State state = State.SEND;

    public FindAverage(MyAgent agent, long period) {
        super(agent, period);
        this.setFixedPeriod(true);
        this.agent = agent;
    }

    @Override
    protected void onTick() {
        currentStep++;

        switch (state) {
            case SEND:
                send();
                break;
            case RECEIVE:
                receive();
                break;
            case END:
                end();
                break;
            default:
                block();
        }
    }

    private void send() {

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        for (String linkedAgent : agent.linkedAgents) {
            //if 1->2 connection then break it if probability more than 0.3
            if (agent.getAID().getLocalName().equals("1")
                    && linkedAgent.equals("2")) {
                double connectionExistParam = Math.random();
                if (connectionExistParam > PROBABILITY_OF_CONNECTION_BREAK) {
                    continue;
                }
            }
            msg.addReceiver(new AID(linkedAgent, AID.ISLOCALNAME));

            //if 6->8 connection then delay if probability more than 0.25
            if (agent.getAID().getLocalName().equals("6")
                    && linkedAgent.equals("8")) {
                double connectionDelayParam = Math.random();
                if (connectionDelayParam > PROBABILITY_OF_DELAY) {
                    //Generate delay
                    int delay = (int) (Math.random() * MAX_DELAY);

                    try {
                        TimeUnit.MILLISECONDS.sleep(delay);
                    } catch (InterruptedException e) {
                        System.out.println(e.toString());
                    }
                }
            }
        }
        double noisyContent=generateNoise();
        msg.setContent(String.valueOf(noisyContent));


        System.out.println(currentStep + ") " +"Agent:" + agent.getAID().getLocalName() +
                " Sending " + String.valueOf(noisyContent));
        agent.send(msg);
        state = State.RECEIVE;
    }

    private double generateNoise() {
        double noise = 0.1*Math.sin(Instant.now().toEpochMilli());
        return (agent.getMyNumber() - noise);
    }

    private void receive() {
        double res = 0;
        Set<String> processed = new HashSet<>();
        double agentNumber = agent.getMyNumber();
        while ((agent.receive()) != null) {
            ACLMessage msg = agent.receive();
            if (msg != null) {
                if (processed.isEmpty() || !processed.contains(msg.getSender().getLocalName())) {
                    double numberReceived = Double.parseDouble(msg.getContent());

                    System.out.println(currentStep + ") " +"Agent:" + agent.getAID().getLocalName()
                            +" Received " + numberReceived);

                    res += numberReceived - agentNumber;
                    processed.add(msg.getSender().getLocalName());

                }
                else{
                    if(processed.size()==agent.linkedAgents.length){
                        break;
                    }
                }
            }
        }
        agent.setMyNumber(agentNumber + DEFAULT_ALPHA * res);

        if (currentStep >= 1000) {
            end();
        }
        state = State.SEND;
    }

    private void end() {
        String name = agent.getAID().getLocalName();

        DecimalFormat df = new DecimalFormat("#.######");
        System.out.println(currentStep + ") " + "Agent: " + name
                + " calculated average : " + df.format(agent.getMyNumber()));

        ContainerKiller.killContainerOf(agent);
    }

}

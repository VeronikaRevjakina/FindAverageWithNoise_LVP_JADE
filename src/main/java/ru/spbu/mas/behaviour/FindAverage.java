package ru.spbu.mas.behaviour;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ru.spbu.mas.agent.MyAgent;
import ru.spbu.mas.main.ContainerKiller;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;

public class FindAverage extends TickerBehaviour {


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
            msg.addReceiver(new AID(linkedAgent, AID.ISLOCALNAME));
        }
        msg.setContent(Utils.serializeToString(agent.freshAgentsNumbers));
        agent.send(msg);

        agent.agentsNumbers.putAll(agent.freshAgentsNumbers);
        agent.freshAgentsNumbers.clear();

        state = State.RECEIVE;
    }

    private void receive() {
        for (int i = 0; i < agent.linkedAgents.length; i++) {
            ACLMessage msg = agent.receive();
            if (msg != null) {

                @SuppressWarnings("unchecked")
                HashMap<String, Integer> map = (HashMap<String, Integer>)
                        Utils.deserializeFromString(msg.getContent());

                map.keySet().removeAll(agent.agentsNumbers.keySet());
                agent.freshAgentsNumbers.putAll(map);
            } else
                block();

        }
        state = agent.freshAgentsNumbers.size() > 0
                ? State.SEND
                : State.END;
    }

    private void end() {
        String name = agent.getAID().getLocalName();

        if (name.equals(agent.agentsNumbers.keySet().stream()
                .max(Comparator.naturalOrder()).orElse(name))) {

            int sum = agent.agentsNumbers.values().stream().mapToInt(i -> i).sum();
            double average = (double) sum / agent.agentsNumbers.size();

            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println(currentStep + ") " + name
                    + " calculated average : " + df.format(average));

            ContainerKiller.killContainerOf(agent);
        }
    }

}

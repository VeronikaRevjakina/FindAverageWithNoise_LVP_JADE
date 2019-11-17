package ru.spbu.mas.main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.ArrayList;
import java.util.Arrays;

class MainController {
    //TODO: CHANGE GRAPH STRUCTURE EACH CONNECTION 2 ENDS

    private static final ArrayList<String []> parameters = new ArrayList<>(Arrays.asList(
            new String[]{"20", "2", "3", "4"},
            new String[]{"5", "1", "3", "5"},
            new String[]{"10", "1", "2"},
            new String[]{"10", "1", "5"},
            new String[]{"5", "1", "4"}));

    void initAgents() {
        // Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();
        // Create a container to host the Default Agent
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "10098");
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createMainContainer(p);
        int i = 0;

        try {
            for (String[] agentParameter : parameters) {
                i++;// for agent name
                AgentController agent = cc.createNewAgent(Integer.toString(i),
                        "ru.spbu.mas.agent.MyAgent", agentParameter);

                agent.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


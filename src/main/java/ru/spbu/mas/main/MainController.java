package ru.spbu.mas.main;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

class MainController {

    private static final int numberOfAgents = 5;

    void initAgents() {
        // Retrieve the singleton instance of the JADE Runtime
        Runtime rt = Runtime.instance();
        // Create a container to host the Default Agent
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "10098");
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createMainContainer(p);


        try {

            //Object[] arr=new Object[]{"5"};

            AgentController agent1 = cc.createNewAgent(Integer.toString(1),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"5","2","4","8"});
            AgentController agent2 = cc.createNewAgent(Integer.toString(2),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"15","1","3","6"});
            AgentController agent3 = cc.createNewAgent(Integer.toString(3),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"10","1","4","5"});
            AgentController agent4 = cc.createNewAgent(Integer.toString(4),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"3","1","3","8"});
            AgentController agent5 = cc.createNewAgent(Integer.toString(5),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"2","4","3","7","10"});
            AgentController agent6 = cc.createNewAgent(Integer.toString(6),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"10","3","2","1"});
            AgentController agent7 = cc.createNewAgent(Integer.toString(7),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"10","6","2","10"});
            AgentController agent8 = cc.createNewAgent(Integer.toString(8),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"5","6","7","5","4"});
            AgentController agent9 = cc.createNewAgent(Integer.toString(9),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"8","1","6","4","3"});
            AgentController agent10 = cc.createNewAgent(Integer.toString(10),
                    "ru.spbu.mas.agent.MyAgent", new Object[]{"10","9","7","5"});

            agent1.start();
            agent2.start();
            agent3.start();
            agent4.start();
            agent5.start();
            agent6.start();
            agent7.start();
            agent8.start();
            agent9.start();
            agent10.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


package ru.spbu.mas.main;

import jade.core.Agent;
import jade.core.AgentContainer;
import jade.wrapper.StaleProxyException;

public class ContainerKiller {
    public static void killContainerOf(Agent agent) {

        jade.wrapper.AgentContainer container = agent.getContainerController();

        agent.doDelete();

        new Thread(() -> {
            try {
                container.kill();
            } catch (StaleProxyException ignored) {
            }
        }).start();
    }
}

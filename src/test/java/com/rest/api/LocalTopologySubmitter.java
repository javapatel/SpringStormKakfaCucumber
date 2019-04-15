package com.rest.api;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(value = "test")
public class LocalTopologySubmitter implements StormTopologySubmitter {
    @Override
    public void submitTopology(String name, Config config, StormTopology topology) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(name, config, topology);
    }
}

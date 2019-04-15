package com.rest.api;

import org.apache.storm.Config;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;

public interface StormTopologySubmitter {

    void submitTopology(String name, Config config, StormTopology topology) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException;
}




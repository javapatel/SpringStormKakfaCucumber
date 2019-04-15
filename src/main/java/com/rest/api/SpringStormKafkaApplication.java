package com.rest.api;

import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SpringStormKafkaApplication {

    @Autowired
    StormTopologyBuilder stormTopologyBuilder;

    @Autowired
    StormTopologySubmitter topologySubmitter;

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringStormKafkaApplication.class).web(WebApplicationType.NONE).run(args);
    }

    @Component
    public class CommandLiner implements CommandLineRunner {

        @Override
        public void run(String... args) throws Exception {
            Config config = getConfig();
            StormTopology topology = stormTopologyBuilder.build();

            topologySubmitter.submitTopology("kafka-storm-kafka", config, topology);
            System.out.println("\n\n\nTopology submitted\n\n\n");
        }
    }

    protected Config getConfig() {
        Config config = new Config();
        config.put(Config.TOPOLOGY_DEBUG, false);
        config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        config.put(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS, 10);//alters the default 30 second of tuple timeout to 10 second
        return config;
    }
}


package com.rest.api;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.testing.IdentityBolt;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class StormTopologyBuilder {

    @Autowired
    HbaseBolt hbaseBolt;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.request.topic}")
    private String topic;

    public StormTopology build() {
        final TopologyBuilder builder = new TopologyBuilder();
        final Fields fields = new Fields("topic", "key", "message");
        KafkaSpout<String, String> kafkaSpout = getKafkaSpout();

        // Identity bolt (just for testing, doing nothing)
        IdentityBolt identityBolt = new IdentityBolt(fields);


        // Building the topology: KafkaSpout -> Identity -> KafkaBolt
        builder.setSpout("kafka-spout", kafkaSpout);
        builder.setBolt("identity", identityBolt).shuffleGrouping("kafka-spout");
        builder.setBolt("kafka-bolt", hbaseBolt, 1).globalGrouping("identity");

        return builder.createTopology();
    }

    private Config getConfig() {
        Config conf = new Config();
        conf.setDebug(true);
        return conf;
    }

    private KafkaSpout<String, String> getKafkaSpout() {
        // Properties
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("acks", "1");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    /*props.put("security.protocol", "SASL_PLAINTEXT");
    props.put("sasl.jaas.config", "com.sun.security.auth.module.Krb5LoginModule required "
            + "useTicketCache=false "
            + "renewTicket=true "
            + "serviceName=\"kafka\" "
            + "useKeyTab=true "
            + "keyTab=\"/home/pvillard/pvillard.keytab\" "
            + "principal=\"pvillard@EXAMPLE.COM\";");
*/
        // Kafka spout getting data from "inputTopicStorm"
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig
                .builder(props.getProperty("bootstrap.servers"), topic)
                .setGroupId("storm")
                .setOffsetCommitPeriodMs(1000)
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST)
                .setProp(props)
                .setRecordTranslator((r) -> new Values(r.topic(), r.key(), r.value()), new Fields("topic", "key", "message"))
                .build();


        return new KafkaSpout<>(kafkaSpoutConfig);
    }
}

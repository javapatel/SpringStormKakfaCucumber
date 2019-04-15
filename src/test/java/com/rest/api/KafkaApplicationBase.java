package com.rest.api;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static com.rest.api.KafkaApplicationBase.HELLOWORLD_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = TestConfiguration.class)
@DirtiesContext
@ActiveProfiles(value = "test")
@EmbeddedKafka(partitions = 1,topics = HELLOWORLD_TOPIC)
@ContextConfiguration
public class KafkaApplicationBase {
    static final String HELLOWORLD_TOPIC = "test.input.topic";

    @Autowired
    protected PaymentStoreService paymentStoreService;

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Autowired
    KafkaTemplate kafkaTemplate1;

    @Value("${kafka.request.topic}")
    protected String topicName;

    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        // list of host:port pairs used for establishing the initial connections to the Kakfa cluster
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return props;
    }

    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }


    protected KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());

}


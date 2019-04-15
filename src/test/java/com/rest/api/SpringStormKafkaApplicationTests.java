package com.rest.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import kafka.Kafka;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfiguration.class)
@ComponentScan(value = "com.rest.api")
@DirtiesContext
@ActiveProfiles(value = "test")
@EmbeddedKafka(partitions = 1, topics = {SpringStormKafkaApplicationTests.HELLOWORLD_TOPIC})
public class SpringStormKafkaApplicationTests {

    static final String HELLOWORLD_TOPIC = "test.input.topic";

    @Autowired
    PaymentStoreService paymentStoreService;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Test
    public void testReceive() throws Exception {
        ProducerRecord producerRecord = new ProducerRecord<String, String>(HELLOWORLD_TOPIC, "paymentId");
        kafkaTemplate.send(producerRecord);

        Thread.sleep(10000);
        Assert.assertEquals(false, paymentStoreService.getPayment("paymentId").isEmpty());
    }
}


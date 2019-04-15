package com.rest.api;

import com.rest.api.KafkaApplicationBase;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class KafkaStepsBase extends KafkaApplicationBase {

    @Given("^the bag is empty$")
    public void the_bag_is_empty() throws Exception {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^I put (\\d+) potato in the bag$")
    public void i_put_potato_in_the_bag(String paymentId) throws Exception {
        // Write code here that turns the phrase above into concrete actions
        ProducerRecord producerRecord = new ProducerRecord<String, String>(topicName, paymentId);
        kafkaTemplate1.send(producerRecord);
    }

    @Then("^the bag should contain only (\\d+) potato$")
    public void the_bag_should_contain_only_potato(String arg1) throws Exception {
        Thread.sleep(100000);
        Assert.assertEquals(false, paymentStoreService.getPayment(arg1).isEmpty());
    }

}

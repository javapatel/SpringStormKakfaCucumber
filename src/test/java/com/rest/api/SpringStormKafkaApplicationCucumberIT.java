package com.rest.api;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Profile;

@RunWith(Cucumber.class)
@Profile(value = "test")
@CucumberOptions(features = "src/test/resources/Test.feature", format = {"pretty", "html:target/reports/cucumber/html",
        "json:target/cucumber.json"})
public class SpringStormKafkaApplicationCucumberIT {
}

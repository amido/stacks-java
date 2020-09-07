package com.xxAMIDOxx.xxSTACKSxx.api;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = "src/test/resources/features",
    tags = "(not @Ignore) and (@Smoke or @Regression or @Functional)")
public class CucumberTestSuite {}

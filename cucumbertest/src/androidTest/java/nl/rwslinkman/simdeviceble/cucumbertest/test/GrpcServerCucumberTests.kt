package nl.rwslinkman.simdeviceble.cucumbertest.test

import io.cucumber.junit.CucumberOptions

@CucumberOptions(
    features = ["features"],
    tags = ["not @ignored"],
    glue = ["nl.rwslinkman.simdeviceble.cucumbertest.test.steps"],
    plugin = []
)
class GrpcServerCucumberTests
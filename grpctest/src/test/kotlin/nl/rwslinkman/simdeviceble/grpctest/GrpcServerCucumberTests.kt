package nl.rwslinkman.simdeviceble.grpctest

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources/features"],
    tags = "not @ignored",
    publish = false
)
class GrpcServerCucumberTests
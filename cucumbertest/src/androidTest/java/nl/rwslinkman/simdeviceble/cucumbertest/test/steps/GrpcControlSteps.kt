package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import nl.rwslinkman.simdeviceble.cucumbertest.test.grpc.SimDevice
import nl.rwslinkman.simdeviceble.cucumbertest.test.grpc.SimDeviceGrpcClient
import org.junit.Assert.*

class GrpcControlSteps {

    val grpcClient = SimDeviceGrpcClient(tabletIP, grpcPort)

    private var supportedDevicesResponse: List<SimDevice>? = null

    @Given("I have a gRPC client")
    fun grpcSetup() {
        assertNotNull(grpcClient)
    }

    @When("the simulator is instructed to return a list of supported devices")
    fun executeGrpcRequestListDevices() {
        supportedDevicesResponse = grpcClient.listAvailableSimDevices()
    }

    @Then("it should return all supported devices")
    fun verifyGrpcRequestListDevices() {
        assertNotNull(supportedDevicesResponse)
        assertTrue(supportedDevicesResponse?.isNotEmpty() ?: false)
    }

    @Given("the simulator is instructed to start advertising as a {string} device")
    fun executeGrpcRequestStartAdvertisement(deviceName: String) {
        val advertisementResponse = grpcClient.startAdvertisement(deviceName)
        assertNotNull(advertisementResponse)
    }

    @When("the simulator is instructed to stop advertising")
    fun executeGrpcRequestStopAdvertisement() {
        grpcClient.stopAdvertisement()
    }

    companion object {
        private const val tabletIP = "192.168.2.6"
        private const val grpcPort = 8911
    }

    @After
    fun stopAdvertisingOnTargetServer() {
        grpcClient.stopAdvertisement()
    }
}
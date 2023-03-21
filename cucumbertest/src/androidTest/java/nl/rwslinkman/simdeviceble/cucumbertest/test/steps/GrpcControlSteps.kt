package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import android.util.Log
import com.google.protobuf.Empty
import io.cucumber.java.After
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.grpc.ManagedChannelBuilder
import nl.rwslinkman.simdeviceble.grpc.server.SimDeviceBLEGrpc
import nl.rwslinkman.simdeviceble.grpc.server.StartAdvertisementRequest
import nl.rwslinkman.simdeviceble.grpc.server.StartAdvertisementResponse
import org.junit.Assert.assertNotNull

class GrpcControlSteps {

    private var response: StartAdvertisementResponse? = null

    private val commChannel = ManagedChannelBuilder.forAddress(tabletIP, grpcPort).usePlaintext().build()
    private val grpcClient = SimDeviceBLEGrpc.newBlockingStub(commChannel)

    @Given("I'm executing a call")
    fun executeCall() {
        val request = StartAdvertisementRequest.newBuilder()
        request.deviceName = "Digital Clock"
        request.advertiseDeviceName = true
        request.connectable = true

        response = grpcClient.startAdvertisement(request.build())
        Log.i(TAG, "response: ${response?.advertisementName ?: "niks"}")
    }

    @Then("It should not crash")
    fun verify() {
        assertNotNull(response)
    }

    companion object {
        private const val tabletIP = "192.168.2.6"
        private const val grpcPort = 8911
        private const val TAG = "GrpcControlSteps"
    }

    @After
    fun stopAdvertisingOnTargetServer() {
        grpcClient.stopAdvertisement(Empty.getDefaultInstance())
    }
}
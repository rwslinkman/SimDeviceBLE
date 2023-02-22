package nl.rwslinkman.simdeviceble.grpctest.steps

import io.cucumber.java.en.Given

class GrpcSteps
{
    @Given("I am saying {string} to the console")
    fun givenSayingToConsole(words: String) = println("step says $words")
}
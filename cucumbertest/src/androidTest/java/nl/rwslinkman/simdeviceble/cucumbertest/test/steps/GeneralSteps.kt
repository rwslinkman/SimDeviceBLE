package nl.rwslinkman.simdeviceble.cucumbertest.test.steps

import android.util.Log
import io.cucumber.java.en.Given
import nl.rwslinkman.simdeviceble.cucumbertest.test.ActivityScenarioHolder

class GeneralSteps {

    private val scenario: ActivityScenarioHolder = ActivityScenarioHolder()

    @Given("I am saying {string} to the console")
    fun givenSayingToConsole(words: String) {
        scenario.launchTestActivity()
        Log.e(TAG, "givenSayingToConsole: step says $words")
    }

    companion object {
        private const val TAG = "GeneralSteps"
    }
}
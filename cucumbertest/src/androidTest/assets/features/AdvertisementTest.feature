Feature: Advertisement using gRPC server

  Scenario: I'm testing if cucumber works with Kotlin
    Given I am saying "Hello World" to the console

  Scenario: Starting to scan for advertising devices
    Given I have configured the Bluetooth scanner
    When I start a BLE discovery for 5 seconds
    Then it should find some devices
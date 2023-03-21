Feature: Advertisement using gRPC server

  Scenario: I'm testing if cucumber works with Kotlin
    Given I am saying "Hello World" to the console

  Scenario: Starting to scan for advertising devices
    Given I have set up the BLE stuff
    When I start a BLE discovery
    Then it should find some devices
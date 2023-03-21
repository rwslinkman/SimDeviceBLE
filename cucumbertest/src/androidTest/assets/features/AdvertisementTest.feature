Feature: Advertisement using gRPC server

  Scenario: Starting to scan for advertising devices
    Given I have configured the Bluetooth scanner
    When I start a BLE discovery for 5 seconds
    Then it should find the target BLE device
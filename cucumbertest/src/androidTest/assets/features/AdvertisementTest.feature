Feature: Advertisement using gRPC server

  @ignored
  Scenario: Starting to scan for advertising devices
    Given I have configured the Bluetooth scanner
    When I start a BLE discovery for 5 seconds
    Then it should find the "Lenovo Tab P11 Pro" BLE device

  Scenario: Executing a gRPC call to the tablet
    Given I'm executing a call
    Then It should not crash
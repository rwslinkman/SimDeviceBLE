Feature: Advertisement using gRPC server

  Scenario Outline: Starting to scan for advertising devices
    Given I have configured the Bluetooth scanner
    And the simulator is instructed to start advertising as a <simulatedDevice> device
    When I start a BLE discovery for <scanDuration> seconds
    Then it should find the <expectedAdvertisementName> BLE device

  Examples:
    | simulatedDevice     | scanDuration  | expectedAdvertisementName |
    | "Digital Clock"     | 5             | "Lenovo Tab P11 Pro"      |
    | "Thermometer (Ear)" | 5             | "Lenovo Tab P11 Pro"      |

  Scenario: Changing the type of device that is advertised
    Given I have configured the Bluetooth scanner
    And the simulator is instructed to start advertising as a "Thermometer (Ear)" device
    When I start a BLE discovery for 5 seconds
    And it has found the "Lenovo Tab P11 Pro" device advertising the "Health Thermometer" service UUID
    And the simulator is instructed to stop advertising
    And I wait for 2 seconds
    And the simulator is instructed to start advertising as a "Digital Clock" device
    And I start a BLE discovery for 5 seconds
    Then it has found the "Lenovo Tab P11 Pro" device advertising the "Current Time" service UUID

  Scenario: Verifying the advertised services and characteristics for the advertised device
    Given I have configured the Bluetooth scanner
    And the simulator is instructed to start advertising as a "Digital Clock" device
    When I start a BLE discovery for 5 seconds
    And it has found the "Lenovo Tab P11 Pro" device advertising the "Current Time" service UUID
    And I connect to the target device
    And I wait for the connection
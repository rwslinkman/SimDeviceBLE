Feature: SimDeviceBLE has a gRPC server to control the simulated device

  Scenario: Presenting a list of supported devices
    Given I have a gRPC client
    When the simulator is instructed to return a list of supported devices
    Then it should return all supported devices
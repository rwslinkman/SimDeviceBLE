Feature: SimDeviceBLE has a gRPC server to control the simulated device

  @ignored
  Scenario: I'm testing if cucumber works with Kotlin
    Given I am saying "Hello World" to the console

  Scenario: Presenting a list of supported devices
    Given I have a gRPC client
    When the simulator is instructed to return a list of supported devices
    Then it should return all supported devices
Feature: Validate transmission dates
  Scenario: Start date should be before end date
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest"
    Then each item should have "transmission_start" before "transmission_end"
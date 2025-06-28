Feature: Validate response header date
  Scenario: Check 'Date' header exists
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest"
    Then the response should contain a "Date" header
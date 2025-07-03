Feature: API GET request to ibltest endpoint

  Scenario: Verify status code and response time
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest"
    Then the response code should be 200
    And the response time should be below 3000 milliseconds

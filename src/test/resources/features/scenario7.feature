Feature: Validate 404 and error object for invalid date

  Scenario: Verify response for invalid date
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest/2023-09-11"
    Then the response code should be 404
    And the response body should contain "details" and "http_response_code"

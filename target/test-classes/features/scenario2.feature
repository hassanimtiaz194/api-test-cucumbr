Feature: Validate data integrity and type

  Scenario: Check 'id' and 'episode.type'
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest"
    Then each item in the data array should have a non-empty "id"
    And each item should have "episode.type" equal to "episode"

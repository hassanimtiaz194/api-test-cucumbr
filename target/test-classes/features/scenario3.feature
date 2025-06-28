Feature: Validate episode title
  Scenario: Check 'episode.title' is never empty
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest"
    Then each item should have a non-empty "episode.title"
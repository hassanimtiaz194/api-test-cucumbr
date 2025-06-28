Feature: Validate only one live episode
  Scenario: Check only one live episode exists
    Given I make a GET request to "https://testapi.io/api/RMSTest/ibltest"
    Then only one item should have "episode.live" equal to true
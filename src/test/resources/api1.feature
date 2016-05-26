Feature: First API

  Scenario: Query status - Healthy
    Given System is healthy
    When API "Query status" is called
    Then API response should have:
      | what          | value |
      | httpstatus    | 200   |
      | body:$.status | OK    |

  Scenario: Query status - Something invalid
    Given Something invalid
    When API "Query status" is called
    Then API response should have:
      | what          | value |
      | httpstatus    | 400   |
      | body:$.code   | 123   |

  Scenario: Query status - Unexpected error
    Given Unexpected errors
    When API "Query status" is called
    Then API response should have:
      | what          | value |
      | httpstatus    | 500   |
      | body:$.code   | 000   |

Feature: CRU Workflow

  Scenario: Create workflow with correct data
    Given Prepare create workflow request
    When Call create workflow endpoint
    Then In response we have status code 201
    And Full set of fields

  Scenario Outline: Create workflow with data
    Given Prepare create workflow request with: "<appId>" ,"<name>" , "<description>"
    When Call create workflow endpoint
    Then In response we have status: <code>
    And Full set of fields

    Examples:
      | appId | name    | description      | code |
      | a1    | name_a1 | some description | 201  |
      | a 1   | name_a2 | some description | 201  |
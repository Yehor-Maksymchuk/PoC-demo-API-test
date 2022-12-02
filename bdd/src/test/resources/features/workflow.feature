Feature: CRU Workflow

  Scenario: Create workflow with correct data
    Given Prepare create workflow request
    When Call create workflow endpoint
    Then In response we have status code 201
    And Full set of fields
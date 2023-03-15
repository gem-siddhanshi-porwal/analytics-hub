@test
Feature: Snowflakes conn

  Scenario Outline: create snowFlakes connections and fetch data
    When The user creates connection with "<schemaName>" schema and execute "<query>" query
    Then The user writes the data in "<fileName>" file for from DB
    Examples:
      | fileName    | schemaName | query              |
      | resultErros | TPCH_SF1   | fetchCustomerTable |
#      | DB_5     | TPCH_SF1   | fetchNationTable   |

@Regression-Test
Feature: Snowflakes conn

  @columnCountValidation
  Scenario: Validate column Count for Raw db and STG db
    When The user creates connection with "TPCH_SF1" schema and execute "fetchCustomerTable" and "fetchNationTable" query
    Then The user validates the column count for both the tables

  @columnNameValidation
  Scenario: Validate column Name for Raw db and STG db
    When The user creates connection with "TPCH_SF1" schema and execute "fetchCustomerTable" and "fetchNationTable" query
    Then The user validates column names for both the table and writes the differences in "resultErrorsColumnName" file

  @columnTypeValidation
  Scenario: Validate column Type for Raw db and STG db
    When The user creates connection with "TPCH_SF1" schema and execute "fetchCustomerTable" and "fetchNationTable" query
    Then The user validates column types for both the table and writes the differences in "resultErrorsColumnType" file

  @validateRowCount
  Scenario: validate no of rows/records in tables
    When The user creates connection with "<schemaName>" schema and execute "<rawDbQuery>" and "<dbQuerySTG>" query
    Then The user compares the row count for both the tables

  @getTheMissingColumnNames
  Scenario: validate no of columns in tables
    When The user creates connection with "TPCH_SF1" schema and execute "<customerTable>" and "<nationTable>" query
    Then The user compares data of "Customer" table with data of "Nation" table



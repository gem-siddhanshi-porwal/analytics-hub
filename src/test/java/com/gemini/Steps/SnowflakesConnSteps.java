package com.gemini.Steps;

import com.gemini.CommonFunctions.ConnectionUtils;
import com.gemini.CommonFunctions.UtilsFunction;
import com.gemini.Implementation.DbImplementation;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.sql.SQLException;

public class SnowflakesConnSteps {

    DbImplementation snow= new DbImplementation();
    ConnectionUtils connect= new ConnectionUtils();

    @When("^The user creates connection with \"(.*?)\" schema and execute \"(.*?)\" and \"(.*?)\" query$")
    public void createConn(String DBName, String sourceQuery, String targetQuery) {
        //how are storing the data in result set ?

        connect.snowflakeConnection(DBName);
        snow.fetchDbResultSet(sourceQuery, targetQuery);

    }

    @Then("^The user validates the column count for both the tables$")
    public void validateColumnCount() {
        snow.validateColumnCount();

    }

    @Then("^The user validates column names for both the table and writes the differences in \"(.*?)\" file$")
    public void validateColumnName(String fileName) {
        snow.validateColumnNameAndType(fileName, "names");
    }

    @Then("^The user validates column types for both the table and writes the differences in \"(.*?)\" file$")
    public void validateColumnType(String fileName) {
        snow.validateColumnNameAndType(fileName, "types");
    }

    @Then("^The user compares the row count for both the tables$")
    public void compareRecordCount() {
        try {
            snow.validateRowCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Then("^The user compares data of \"(.*?)\" table with data of \"(.*?)\" table$")
    public void compareColumns(String sourceTableName,  String targetTableName) throws SQLException,  IOException {
       snow.getMissingColumns(sourceTableName,targetTableName);
    }

}

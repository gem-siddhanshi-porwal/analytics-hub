package com.gemini.Steps;

import com.gemini.Implementation.SnowFlakesConnImplementations;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.sql.SQLException;

public class SnowflakesConnSteps {

    SnowFlakesConnImplementations snow;
    @When("^The user creates connection with \"(.*?)\" schema and execute \"(.*?)\" query$")
        public void createConn(String DBName, String query) throws SQLException {
        snow.createSnowflakeConn(DBName,query);
        }

    @Then("^The user writes the data in \"(.*?)\" file for from DB$")
    public void writeDataInCsvFileDB1(String fileName)throws IOException, SQLException {
        snow.writeDataDb1(fileName);

    }
}

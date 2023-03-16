package com.gemini.CommonFunctions;

import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import java.sql.*;
import java.util.Properties;


public class ConnectionUtils {
    public static Connection connection;
    public static Statement statement;
    public static ResultSet dbToBeFetched;

    public static void snowflakeConnection(String schemaName)  {
        // Set up the Snowflake JDBC driver properties will be calling it from serenity.conf
        Properties properties = new Properties();
        properties.put("user",EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty("userID")) ;
        properties.put("password", EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty("password"));
        properties.put("account", EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty("accountId"));
        properties.put("warehouse", EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty("warehouseName"));
        properties.put("db", EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty("dataBaseName"));
        properties.put("schema", schemaName);

        try{
            Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
            //Create a connection
            connection = DriverManager.getConnection("jdbc:snowflake://"+EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty("accountId")+".snowflakecomputing.com", properties);
            // Create a statement
            statement = connection.createStatement();
        }catch (ClassNotFoundException e){
            UtilsFunction.LOGGER.debug("Driver not found "+e);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public static ResultSet fetchDBResultSet(String query){
        // Execute a query
        try{
        dbToBeFetched = statement.executeQuery(EnvironmentSpecificConfiguration.from(ConfiguredEnvironment.getEnvironmentVariables()).getProperty(query));
        }catch (Exception e){
          UtilsFunction.LOGGER.debug("Found exception while fetching the DB: "+e);
        }
        return dbToBeFetched;
    }



}

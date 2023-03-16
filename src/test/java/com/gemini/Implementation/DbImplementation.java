package com.gemini.Implementation;

import com.gemini.CommonFunctions.ConnectionUtils;
import com.gemini.CommonFunctions.UtilsFunction;
import org.junit.Assert;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


public class DbImplementation {

    public static ArrayList<Object> rawDbColumnData=new ArrayList<>();
    public static ArrayList<Object> dbColumnDataSTG=new ArrayList<>();
    public static ResultSet rawDbTableData;
    public static ResultSet dbTableDataSTG;
    public static ResultSetMetaData rawDbMetaData;
    public static ResultSetMetaData dbMetaDataSTG;
    public static BufferedWriter buffer;

    public static void validateColumnNameAndType(String fileName, String type) {

        try {
            if (type.equalsIgnoreCase("names")){
        buffer = UtilsFunction.createFile(fileName, "This File records the differences of Column Names for Raw Table to STG table", "ColumNameRawDb", "ColumnNameSTGDb");
        }else{
                buffer= UtilsFunction.createFile(fileName,"This File records the differences of Column Type for Raw Table to STG table","ColumTypeRawDb", "ColumnTypeSTGDb" );
            }
                //fetching meta data
            rawDbMetaData = UtilsFunction.getMetaData(rawDbTableData);
            dbMetaDataSTG = UtilsFunction.getMetaData(dbTableDataSTG);
            boolean flag = false;
            //fetching the column names and column Types
            UtilsFunction.getColumnName(rawDbColumnData, dbColumnDataSTG, rawDbMetaData, dbMetaDataSTG, type);
            for (int i = 0; i < rawDbColumnData.size(); i++) {
                //comparing the data index wise
                if (!rawDbColumnData.get(i).equals(dbColumnDataSTG.get(i))) {
                    if (type.equalsIgnoreCase("names")) {
                        buffer.write( rawDbColumnData.get(i).toString()+","+ dbColumnDataSTG.get(i).toString());
                       buffer.newLine();
                        flag = true;
                          } else {
                        buffer.write( rawDbColumnData.get(i).toString()+","+ dbColumnDataSTG.get(i).toString());
                        buffer.newLine();
                        flag = true;
                         }
                } else {

                    UtilsFunction.LOGGER.info("The Column " + type + " matched for both the tables");
                }
            }
            if (flag) {
                Assert.fail("The columns "+type+" didn't match");
            } else {
                UtilsFunction.LOGGER.info("The Column "+type+" matched for Raw DB tables and STG DB tables");
            }
        } catch (Exception e) {
            UtilsFunction.LOGGER.info("Exception: " + e);
        } finally {
            try {
                //closing
                ConnectionUtils.connection.close();
                ConnectionUtils.statement.close();
                buffer.close();
                dbTableDataSTG.close();
                rawDbTableData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static void validateColumnCount()  {
        try {
            //fetching meta data
            rawDbMetaData = UtilsFunction.getMetaData(rawDbTableData);
            dbMetaDataSTG = UtilsFunction.getMetaData(dbTableDataSTG);
            //validating column count
            UtilsFunction.validateColumnNumber(rawDbMetaData, dbMetaDataSTG);
            UtilsFunction.LOGGER.info("The user is validating column count for both the tables");
        } catch (Exception e) {
            UtilsFunction.LOGGER.info("Exception: " + e);
            Assert.fail("The columns count did not match");
        } finally {
            try {
                //closing
                ConnectionUtils.connection.close();
                ConnectionUtils.statement.close();
                dbTableDataSTG.close();
                rawDbTableData.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fetchDbResultSet(String rawQuery, String dbQuerySTG) {

            //fetching result sets
            rawDbTableData = ConnectionUtils.fetchDBResultSet(rawQuery);
            dbTableDataSTG = ConnectionUtils.fetchDBResultSet(dbQuerySTG);
            UtilsFunction.LOGGER.info("The user is fetching the result set for both the tables");

    }
    public void getMissingColumns(String sourceTableName,String targetTableName ) throws SQLException, IOException {

        UtilsFunction.getMissingColumn(sourceTableName, rawDbTableData, targetTableName, dbTableDataSTG);
        UtilsFunction.LOGGER.info("Getting the names of missing columns from both the table");
    }
    public void validateRowCount() throws SQLException {
        UtilsFunction.validateRowCount(rawDbTableData,dbTableDataSTG);
        UtilsFunction.LOGGER.info("Validating if the row count is same");
    }


}

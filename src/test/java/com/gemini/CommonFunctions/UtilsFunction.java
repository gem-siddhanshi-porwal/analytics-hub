package com.gemini.CommonFunctions;

import net.serenitybdd.core.environment.ConfiguredEnvironment;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class UtilsFunction {
    public static Logger LOGGER = LoggerFactory.getLogger(UtilsFunction.class);
    public static ResultSetMetaData baseTableMetaData;
    public static ResultSetMetaData targetTableMetaData;
    public static ArrayList<String> baseTableColumns = new ArrayList<>();
    public static ArrayList<String> targetTableColumns = new ArrayList<>();
    public static SimpleDateFormat date = new SimpleDateFormat("ddMMMMyyyy_");
    public static BufferedWriter buffer;
    //Meta data
    public static ResultSetMetaData getMetaData(ResultSet resultSet) throws SQLException {

        LOGGER.info("Getting the metadata for the given result set");
        return resultSet.getMetaData();

    }

    //Columns type & Name(Same sze)
    public static void getColumnName(ArrayList<Object> sourceColumn, ArrayList<Object> targetColumn, ResultSetMetaData sourceMetaData, ResultSetMetaData targetMetaData, String type) {
        try {
            LOGGER.info("The user gets column " + type + " of the tables");
            if (sourceMetaData.getColumnCount() == targetMetaData.getColumnCount()) {
                for (int i = 1; i < sourceMetaData.getColumnCount(); i++) {
                    if (type.equalsIgnoreCase("names")) {

                        //getting the list of column names for both the tables
                        sourceColumn.add(sourceMetaData.getColumnName(i));
                        targetColumn.add(targetMetaData.getColumnName(i));

                    } else {
                        //getting the list of column types for both the tables
                        sourceColumn.add(sourceMetaData.getColumnType(i));
                        targetColumn.add(targetMetaData.getColumnType(i));
                    }

                }
                LOGGER.info("The number of columns present in both the table are same");
            }
        } catch (Exception e) {
            LOGGER.debug("The no. of columns are not same for both the tables" + e);
        }
    }

    //File writing
    public static BufferedWriter createFile(String fileName, String fileDescription, String sourceColumnName, String targetColumnName) throws IOException {

        SimpleDateFormat date = new SimpleDateFormat("ddMMMyyyy_HH.mm.ss");
        FileWriter resultDataDifferenceFIle = new FileWriter(System.getProperty("user.dir") + "\\src\\test\\resources\\data\\" + fileName + "_" + date.format(new Date()) + ".csv");
        BufferedWriter buffer = new BufferedWriter(resultDataDifferenceFIle);
        buffer.write(fileDescription);
        buffer.newLine();
        buffer.write(sourceColumnName + "," + targetColumnName);
        buffer.newLine();
        return buffer;
    }

    //Colum No.
    public static void validateColumnNumber(ResultSetMetaData sourceMetaData, ResultSetMetaData targetMetaData) {

        try {
            Assert.assertEquals(sourceMetaData.getColumnCount(), targetMetaData.getColumnCount());
            LOGGER.info("The count of column for source Table is : " + sourceMetaData.getColumnCount() + "and the column count for target table is : " + targetMetaData.getColumnCount());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void validateRowCount(ResultSet sourceResultSet, ResultSet targetResultSet) throws SQLException {
        try {
            if (getRowCount(sourceResultSet) == getRowCount(targetResultSet)) {
                LOGGER.info("Records in both the rows are same which is : " + getRowCount(sourceResultSet));
            } else {
                int differenceInRecords = getDifference(getRowCount(sourceResultSet), getRowCount(targetResultSet));
                LOGGER.info("The records in both the resultSet are not same");
                LOGGER.info("One of the resultSet has " + differenceInRecords + " more records");
                Assert.fail();
            }
        }catch (SQLException e){
            LOGGER.info(String.valueOf(e));
        }  finally {
            ConnectionUtils.connection.close();
            ConnectionUtils.statement.close();
        }
    }
    public static long getRowCount(ResultSet rs) throws SQLException {
        long count = 0;
        while (rs.next()) {
            count++;
        }
        return count;
    }
    public static int getDifference(long val1, long val2) {
        long greaterNum = Math.max(val1, val2);
        long smallerNum = Math.min(val1, val2);
        return Math.toIntExact(Math.subtractExact(greaterNum, smallerNum));
    }

    public static ArrayList<String> getListOfColumns(ResultSetMetaData rsData) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= rsData.getColumnCount(); i++) {
            columnNames.add(rsData.getColumnName(i));
        }
        return columnNames;
    }
    //Extra columns (Comparison of smaller hash map to bigger one and then extra columns will get printed)
    // missing columns
     public static void getMissingColumn(String sourceTableName,ResultSet sourceResultSet,String targetTableName,ResultSet targetResultSet) throws SQLException, IOException {

       BufferedWriter bufferWriter= createFile("missingColumns","This file will record the missing columns present","missingColumnSource","MissingColumnTarget");
        try {
        baseTableMetaData = getMetaData(sourceResultSet);
        targetTableMetaData = getMetaData(targetResultSet);
        baseTableColumns = getListOfColumns(baseTableMetaData);
        targetTableColumns = getListOfColumns(targetTableMetaData);
        if (!(baseTableMetaData.getColumnCount() == targetTableMetaData.getColumnCount())) {
            Collections.sort(baseTableColumns);
            Collections.sort(targetTableColumns);
            if (baseTableColumns.size() > targetTableColumns.size()) {
                bufferWriter.write("Extra columns in " + sourceTableName + " are :");
                LOGGER.info(sourceTableName + "has more columns then " + targetTableName);
                for (int i = targetTableColumns.size(); i < baseTableColumns.size(); i++) {
                    bufferWriter.write(baseTableColumns.get(i)+" | ");
                    bufferWriter.newLine();
                    //bufferWriter.flush();                        System.out.println(baseTableColumns.get(i));
                }
            } else {
                bufferWriter.write("Extra columns in " + targetTableName + " are : ");
                LOGGER.info(targetTableName + "has more columns then " + targetTableName);
                for (int i = baseTableColumns.size(); i < targetTableColumns.size(); i++) {
                    bufferWriter.write(targetTableColumns.get(i));
                    bufferWriter.newLine();
                }
            }
        } else {
            LOGGER.info("Columns count is same there are not extra columns");
            LOGGER.info("The Columns count for both the table is : " + baseTableMetaData.getColumnCount());
        }
    }catch (Exception e){
        System.out.println(e);
    }finally {
        ConnectionUtils.connection.close();
        ConnectionUtils.statement.close();
        bufferWriter.close();
    }
}


}



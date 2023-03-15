package com.gemini.CommonFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UtilsFunction {
    public static Logger LOGGER= LoggerFactory.getLogger(UtilsFunction.class);
    public static ResultSetMetaData resultSetMetaData;

    //Meta data
public static  ResultSetMetaData getMetaData(ResultSet resultSet) throws SQLException {

    LOGGER.info("Getting the metadata for the given result set");
        return resultSet.getMetaData();

}
    //Columns type & Name(Same sze)

    //File writing

    //Colum No.

    //rowNo.

    //Extra columns (Comparison of smaller hash map to bigger one and then extra columns will get printed)

    //missing columns

}

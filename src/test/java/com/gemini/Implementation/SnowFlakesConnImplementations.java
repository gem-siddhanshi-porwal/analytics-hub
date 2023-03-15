package com.gemini.Implementation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SnowFlakesConnImplementations {


    public static  ResultSet resultSet1;
    public static Connection connection;
    public static Statement statement;
    public static  ResultSet resultSet2;
    public static List<String> colNames1=new ArrayList<>();
    public static List<String> colNames2=new ArrayList<>();
    public static void writeDataDb1(String fileName) throws IOException, SQLException {

        FileWriter DBData1 = new FileWriter( System.getProperty("user.dir")+"\\src\\test\\resources\\data\\"+fileName + ".csv");
//        FileWriter DBData1 = new FileWriter(System.getProperty("user.dir")+"\\src\\test\\resources\\Data\\" + fileName + ".txt");
//        CSVWriter DBData2 = new CSVWriter(new FileWriter(System.getProperty("user.dir")+"\\IdeaProjects\\Snowiee\\src\\test\\resources\\Data\\" + fileName + ".csv"));
        BufferedWriter buffer = new BufferedWriter(DBData1);
//collection utils  apache commons
        //finally add close connection
        if (!colNames1.isEmpty() && !colNames2.isEmpty()) {
            if(colNames2.size()==colNames1.size() && resultSet2.getRow()==resultSet1.getRow()){
            for (int i = 0; i <colNames1.size(); i++){
                //add the list in sorted order collection .sort
                //print the extra or less columns
                //create a function of 2 list to check if the functions are extra
                if(!colNames2.get(i).equals(colNames1.get(i))){
                    buffer.write(colNames1.get(i)+",");
//                    DBData2.writeAll(resultSet1, true);
                    System.out.println("Data feed in csv successfully");

        }}
                System.out.println("All the column names are matching");
            }
            System.out.println("Number of count does not match the for both the dbs");
        }
        //throw exception and assertions to be added
        resultSet1.close();
        resultSet2.close();
        connection.close();
        statement.close();
        buffer.close();

    }
    public static void createSnowflakeConn(String schemaName, String query) throws SQLException {
        try{
            Class.forName("net.snowflake.client.jdbc.SnowflakeDriver");
        }catch (ClassNotFoundException e){
            System.out.println("Driver not found "+e);
        }

        //this will be removed in future
        String queryToExecute="";
        if (query.equals("fetchCustomerTable")){
            queryToExecute="select * from TPCH_SF1.CUSTOMER";
        }else if(query.equals("fetchNationTable")){
            queryToExecute="select * from TPCH_SF1.NATION";
        }

        // Create a connection to Snowflake
//
//         connection = DriverManager.getConnection("jdbc:snowflake://no73553.ap-south-1.snowflakecomputing.com", properties);
//        // Create a statement
//        statement = connection.createStatement();

        // Execute a query
        resultSet1 = statement.executeQuery(queryToExecute);
        resultSet2 = statement.executeQuery("select * from TPCH_SF1.NATION");
        ResultSetMetaData rsmd1 = resultSet1.getMetaData();
        ResultSetMetaData rsmd2 = resultSet2.getMetaData();

        //adding col names in a list
        int columnsNumber1 = rsmd1.getColumnCount();
        int columnsNumber2 = rsmd2.getColumnCount();
        //create an enum 1. column count 2. total no. of count 3.type of column validation
//        key=column name , value= column type (Linked Hash Maps)
        for (int i=1;i<=columnsNumber1;i++){
            colNames1.add(rsmd1.getColumnName(i));

        }
        for (int i=1;i<=columnsNumber2;i++){
            colNames2.add(rsmd2.getColumnName(i));
        }
        System.out.println(resultSet1);
        System.out.println(resultSet2);
    }

}

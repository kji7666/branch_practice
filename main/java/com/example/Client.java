package com.example;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        Client unit = new Client();
        unit.insert("000000", "111111", "222222");
        List<String> result = unit.query("000000");
        for(String data : result){
            System.out.println(data);
        }

    }

    private DataBaseConnector dataBaseConnector;

    public Client(){
        try{
            dataBaseConnector = new DataBaseConnector(); // Instantiate the database connector
        } catch (Exception e){
            System.out.println(e.getStackTrace());
            System.out.println(e.getMessage());
        }
    }

    public void insert(String transactionID, String blockIndex, String height){
        //System.out.println("first in");
        String sql = "INSERT INTO transaction_info (transaction_id, block_index, height) VALUES (?, ?, ?);";
        String[] dataArray = new String[]{transactionID, blockIndex, height};
        try{
            dataBaseConnector.insert(sql, dataArray);
            System.out.println("Success");
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }
    }

    public List<String> query(String transactionID){
        String sql = "SELECT * FROM transaction_info WHERE transaction_iD = ?";  // SQL query to search for a transactionID in the specified table
        String[] dataArray = new String[]{transactionID};
        List<String> resultList = new ArrayList<>();
        try{
            ResultSet resultSet = dataBaseConnector.query(sql, dataArray);
            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) { // The JDBC specification states that column indexes start at 1
                    resultList.add(resultSet.getString(i));
                }
            }
            return resultList;
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return null;
    }
}


/* 
class DataBaseOperator {
    private Connection connection;
    private Statement statement;

    public DataBaseOperator(Connection connection, Statement statement) {
        this.connection = connection; // Initialize the connection object
        this.statement = statement; // Initialize the statement object
    }

    public String[] getColumnsName(String tableName) throws SQLException {
        // Get database metadata
        DatabaseMetaData metaData = (DatabaseMetaData) connection.getMetaData();

        // Get column names of the table
        try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null)) {
            // List to store column names
            List<String> columnNamesList = new ArrayList<>();

            // Process the result set
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columnNamesList.add(columnName);
            }

            // Convert the list of column names to an array
            String[] columnNamesArray = columnNamesList.toArray(new String[0]);

            // Print the column names array
            System.out.println("Column names in table " + tableName + ":");
            for (String columnName : columnNamesArray) {
                System.out.println(columnName);
            }
            return columnNamesArray; // Return the array of column names
        }
    }

    public void insert(String tableName, String[] transactionData, String[] columnNames) throws SQLException {
        // Construct the SQL INSERT statement dynamically
        StringJoiner columns = new StringJoiner(", ", "(", ")");
        StringJoiner placeholders = new StringJoiner(", ", "(", ")");

        for(int i=1; i<columnNames.length; i++){ // don't need to insert id
            columns.add(columnNames[i]); // Add column names to the string joiner
            placeholders.add("?"); // Add placeholders for prepared statement
        }

        String sql = "INSERT INTO " + tableName + columns.toString() + " VALUES " + placeholders.toString();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < transactionData.length; i++) {
                preparedStatement.setString(i + 1, transactionData[i]); // Set values for prepared statement parameters
            }

            // Execute the update
            int rowsInserted = preparedStatement.executeUpdate();
            System.out.println(rowsInserted + " rows inserted.");
        }
    }

    public String[] search(String tableName, String transactionID, String[] columnNames) {
        String sql = "SELECT * FROM " + tableName + " WHERE transactionID = ?"; // SQL query to search for a transactionID in the specified table
        List<String> resultList = new ArrayList<>(); // List to store the search results

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, transactionID); // Set the query parameter

            // Execute the query and process the results
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Add the query results to the list
                    for(int i=1; i<columnNames.length; i++){ // Exclude the id column
                        resultList.add(resultSet.getString(columnNames[i])); // Add each column value to the result list
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Convert the list to a string array and return
        return resultList.toArray(new String[0]);
    }
}
*/
package com.company;

import Objects.Cocktail;
import Objects.Ingredients;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by p998hon on 23.08.2017.
 */
public class DBConnection {

    public static final String DB_DRIVER = "org.sqlite.JDBC";
    private static ArrayList<Cocktail> returnedCocktailList = new ArrayList<>();
    private static ArrayList<Ingredients> returnedIngredientList = new ArrayList<>();

    private static Connection getConnection() {
        Connection dbConnection = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            dbConnection = DriverManager.getConnection("jdbc:sqlite:test.db");
            return dbConnection;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbConnection;
    }

    public static void createTables() throws Exception {
        Connection dbConnection = null;
        Statement stat = null;

        try {
            dbConnection = getConnection();
            stat = dbConnection.createStatement();
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS COCKTAIL_NAME (ID integer PRIMARY KEY autoincrement, name text, FOREIGN KEY (ID) REFERENCES COCKTAIL_RECEPIES (C_ID))");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS COCKTAIL_RECEPIES (ID integer PRIMARY KEY autoincrement, C_ID integer, I_ID integer)");
            stat.executeUpdate("create table if not exists COCKTAIL_INGREDIENTS (ID integer PRIMARY KEY autoincrement, NAME text, FOREIGN KEY (ID) REFERENCES COCKTAIL_RECEPIES (I_ID))");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                stat.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
    }

    static void populateCoctailsNamesTable(String c_name) throws SQLException {
        Connection dbConnection = null;
        Statement stat = null;

        try {
            dbConnection = getConnection();
            stat = dbConnection.createStatement();
            PreparedStatement prep = dbConnection.prepareStatement("insert into COCKTAIL_NAME values (?, ?)");

            prep.setString(2, c_name);
            prep.addBatch();

            dbConnection.setAutoCommit(false);
            prep.executeBatch();
            dbConnection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                stat.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }
    }

    static void populateIngredientsNamesTable(String i_name) throws SQLException {
        Connection dbConnection = getConnection();
        Statement state = dbConnection.createStatement();

        try {
            System.out.println("Populating ingredient entries entries");
            dbConnection = getConnection();
            state = dbConnection.createStatement();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("insert into COCKTAIL_INGREDIENTS values (?,?)");
            preparedStatement.setString(2, i_name);
            preparedStatement.addBatch();

            dbConnection.setAutoCommit(false);
            preparedStatement.executeBatch();
            dbConnection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (state != null) {
                state.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }

        }

    }

    static void linkDBEntriesCocktailsToIngredients(String c_name, String i_name) throws SQLException {
        Connection dbConnection = getConnection();
        String readCocktailId = "select id from COCKTAIL_NAME where name =?";
        String readIngredientId = "select id from COCKTAIL_INGREDIENTS where NAME =?";

        try {
            System.out.println("linking ingredients");
            dbConnection = getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("insert into COCKTAIL_RECEPIES values (?,?,?)");
            preparedStatement.setInt(2, returnID(readCocktailId, c_name));
            preparedStatement.setInt(3, returnID(readIngredientId, i_name));
            preparedStatement.addBatch();

            dbConnection.setAutoCommit(false);
            preparedStatement.executeBatch();
            dbConnection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(dbConnection != null){
                dbConnection.close();
            }
        }
    }

    private static int returnID(String statement, String variable)throws SQLException {
        Connection dbConnection = null;
        int id = 0;

        try {
            dbConnection = getConnection();
            PreparedStatement prep = dbConnection.prepareStatement(statement);
            prep.setString(1, variable);
            ResultSet rs = prep.executeQuery();

            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(dbConnection != null){
                dbConnection.close();
            }
        }
        return id;
    }

    public void populateDBEntries(Cocktail cocktail) {
        try {
            populateCoctailsNamesTable(cocktail.returnCoctailName());
            for (Ingredients ingr : cocktail.returnIngredients()) {
                populateIngredientsNamesTable(ingr.returnIngredientName());
                linkDBEntriesCocktailsToIngredients(cocktail.returnCoctailName(), ingr.returnIngredientName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    static boolean checkIfDataAlreadyInDBorNot(String i_name)throws SQLException{
        Connection conn = null;
        int count = 0;
        try{
            conn = getConnection();
            String query = "select count(*) from COCKTAIL_INGREDIENTS where NAME = ?";
            System.out.println("query");
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, i_name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                count = rs.getInt(1);
            }
            System.out.println("numbers of rows " + count);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            if(conn != null){
                conn.close();
            }
        }
        if(count>0){return true;}
        return false;
    }

    static void creatingIngredient(String i_name, String c_name){
        try{
            if(checkIfDataAlreadyInDBorNot(i_name)){
                System.out.println("createRecepieWithExistingIngredient");
                DBConnection.linkDBEntriesCocktailsToIngredients(c_name, i_name);
            }else{
                System.out.println("createRecepeWithNewIngredient");
                DBConnection.populateIngredientsNamesTable(i_name);
                DBConnection.linkDBEntriesCocktailsToIngredients(c_name, i_name);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }



//    public ArrayList<Cocktail> returnDBCocktails(){
//
//    }
//
//    private ArrayList<Ingredients> returnDBIngredients(int c_id){
//
//    }

}

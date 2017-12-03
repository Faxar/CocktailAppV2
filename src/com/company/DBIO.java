package com.company;

import java.sql.*;
import java.util.ArrayList;

import static com.company.DBConnection.getConnection;

class DBIO {

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

    private static void linkDBEntriesCocktailsToIngredients(String c_name, String i_name) throws SQLException {
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

    private static boolean checkIfDataAlreadyInDBorNot(String i_name)throws SQLException{
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
                linkDBEntriesCocktailsToIngredients(c_name, i_name);
            }else{
                System.out.println("createRecepeWithNewIngredient");
                populateIngredientsNamesTable(i_name);
                linkDBEntriesCocktailsToIngredients(c_name, i_name);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private static ArrayList<String> returnCocktailNames() throws SQLException{
        Connection conn = null;
        String name;
        String getCoctails = "select * from COCKTAIL_NAME";
        ArrayList<String> cocktailNamesList = new ArrayList<>();

        try {
            conn = getConnection();
            System.out.println("query");
            PreparedStatement ps = conn.prepareStatement(getCoctails);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                name = rs.getString(2);
                System.out.println("Cocktail with name:" + name + " was found in DB");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.close();
            }
        }
        return cocktailNamesList;
    }

}

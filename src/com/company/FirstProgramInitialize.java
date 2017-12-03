package com.company;

import Objects.Cocktail;
import Objects.Ingredients;

import java.sql.*;
import java.util.ArrayList;

import static com.company.DBConnection.getConnection;

class FirstProgramInitialize {

    static void createTables() throws Exception {
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

    private static ArrayList<Cocktail> returnDBCreatedCocktailList(ArrayList<String> namesInDB) throws SQLException {
        Connection conn = null;
        String ingName;
        String getIngredientsForCocktails = "select ci.NAME from COCKTAIL_INGREDIENTS ci join COCKTAIL_RECEPIES cr on cr.I_ID = ci.ID join COCKTAIL_NAME cn on cn.ID = cr.C_ID where cn.name = ?";
        ArrayList<Cocktail> returnList = new ArrayList<>();
        for (String cName : namesInDB){
            try {
                conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(getIngredientsForCocktails);
                ArrayList<Ingredients> foundIngredients = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    ingName = rs.getString(1);
                    Ingredients ingre = new Ingredients(ingName);
                    foundIngredients.add(ingre);
                }
                Cocktail newlyCreated = new Cocktail(cName, foundIngredients);
                returnList.add(newlyCreated);
            } catch (SQLException e){
                e.printStackTrace();
            } finally {
                if(conn != null){
                    conn.close();
                }
            }
        }

        return returnList;
    }

    static ArrayList<Ingredients> returnDBIngredients() throws SQLException{
        Connection conn = null;
        String returnIngredients = "select * from COCKTAIL_INGREDIENTS";
        int id;
        String name;
        ArrayList<Ingredients> ingredientList = new ArrayList<>();

        try {
            conn = getConnection();
            System.out.println("query");
            PreparedStatement ps = conn.prepareStatement(returnIngredients);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                id = rs.getInt(1);
                name = rs.getString(2);
                System.out.println("Ingredient: " + name + " with ID: " + id);
                Ingredients ingre = new Ingredients(name);
                ingredientList.add(ingre);
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(conn != null){
                conn.close();
            }
        }
        return ingredientList;
    }

}

package com.company;

import Objects.Cocktail;
import Objects.Ingredients;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by p998hon on 23.08.2017.
 */
class DBConnection {

    private static final String DB_DRIVER = "org.sqlite.JDBC";
    private static ArrayList<Cocktail> returnedCocktailList = new ArrayList<>();
    private static ArrayList<Ingredients> returnedIngredientList = new ArrayList<>();

    static Connection getConnection() {
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
}

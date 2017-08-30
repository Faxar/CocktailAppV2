package com.company;




import Objects.Cocktail;
import Objects.Ingredients;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Alazerus on 8/22/2017.
 */
class DBAccess {

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

    public static void pushToDB(String cName) throws SQLException {
        Connection dbConnection = getConnection();

        try {

            PreparedStatement prep = dbConnection.prepareStatement("insert into COCKTAIL_NAME values (?, ?);");

            prep.setString(2, cName);
            prep.addBatch();

            dbConnection.setAutoCommit(false);
            prep.executeBatch();
            dbConnection.setAutoCommit(true);

            dbConnection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables() throws Exception {

        Connection dbConnection = getConnection();
        Statement stat = dbConnection.createStatement();

        try {

            stat.executeUpdate("CREATE TABLE IF NOT EXISTS COCKTAIL_NAME (ID integer PRIMARY KEY autoincrement, name text, FOREIGN KEY (ID) REFERENCES COCKTAIL_RECEPIES (C_ID))");
            stat.executeUpdate("CREATE TABLE IF NOT EXISTS COCKTAIL_RECEPIES (ID integer PRIMARY KEY autoincrement, C_ID integer, I_ID integer)");
            stat.executeUpdate("create table if not exists COCKTAIL_INGREDIENTS (ID integer PRIMARY KEY autoincrement, NAME text, FOREIGN KEY (ID) REFERENCES COCKTAIL_RECEPIES (I_ID))");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Cocktail> returnDBCocktails() throws SQLException{

        Connection dbConnection = null;
        Statement statement = null;
        int counter = 0;
        String selectTableSQL = "SELECT id, name, mainIngredient from cocktails";

        try {
            dbConnection = getConnection();
            statement = dbConnection.createStatement();
            ResultSet rs = statement.executeQuery(selectTableSQL);

            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String ingredientName = rs.getString(3);

                Ingredients ingredient = new Ingredients(ingredientName);
                ArrayList<Ingredients> ingredientsList = new ArrayList<>();

                ingredientsList.add(ingredient);
                returnedIngredientList.add(ingredient);

                Cocktail coctail = new Cocktail(name, ingredientsList);
                returnedCocktailList.add(coctail);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return returnedCocktailList;
    }

    public static ArrayList<Ingredients> returnDBIngredients(){
        return returnedIngredientList;
    }

    public void populateDBEntries(Cocktail cocktail){

    }

    public static void populateCoctailsNamesTable (String c_name) throws SQLException{

        Connection dbConnection = getConnection();
        Statement state = dbConnection.createStatement();
        try{

            PreparedStatement prep = dbConnection.prepareStatement("insert into COCKTAIL_NAME values (?, ?)");

            prep.setString(2, c_name);
            prep.addBatch();

            dbConnection.setAutoCommit(false);
            prep.executeBatch();
            dbConnection.setAutoCommit(true);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void populateIngredientsNamesTable (String i_name) throws SQLException{
        Connection dbConnection = getConnection();
        Statement state = dbConnection.createStatement();

        try{
            PreparedStatement preparedStatement = dbConnection.prepareStatement("insert into COCKTAIL_INGREDIENTS values (?,?)");

            preparedStatement.setString(2, i_name);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private static void linkDBEntriesCocktailsToIngredients(String c_name, String i_name) throws SQLException{

        String readCocktailId = "select id from COCKTAIL_NAME where name =?";
        String readIngredientId = "select id from COCKTAIL_INGREDIENTS where NAME =?";

        try{

            Connection dbConnection = getConnection();
            PreparedStatement preparedStatement = dbConnection.prepareStatement("insert into COCKTAIL_RECEPIES values (?,?,?)");
            preparedStatement.setInt(2, returnID(readCocktailId, c_name));
            preparedStatement.setInt(3, returnID(readIngredientId, i_name));

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static int returnID (String statement, String variable){
        int id = 0;

        try {
            Connection dbConnection = getConnection();
            PreparedStatement prep = dbConnection.prepareStatement(statement);
            prep.setString(1, variable);
            ResultSet rs = prep.executeQuery();

            while (rs.next()){
                id = rs.getInt(1);
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

}

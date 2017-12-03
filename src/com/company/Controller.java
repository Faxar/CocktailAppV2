package com.company;

import Objects.Cocktail;
import Objects.Ingredients;
import org.sqlite.core.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by p998hon on 11.07.2017.
 */
public class Controller {

    private List<Ingredients> ingredientsList;
    private List<Cocktail> coctailList;

    Controller() {
        ingredientsList = new ArrayList<>();
        coctailList = new ArrayList<>();

    }

//    public void initializeDBandLists(){
//        populateCoctailList();
//        populateIngredientList();
//    }

    public List returnIngredients() {
        return ingredientsList;
    }

    void announceIngredientList() {
        if (ingredientsList.size() <= 0) {
            System.out.println("No ingredients in the list.");
        } else {
            for (Ingredients ingr : ingredientsList) {
                System.out.println(ingr.returnIngredientName());
            }
        }
    }

    void announceCocktailList() {
        if (coctailList.size() <= 0) {
            System.out.println("No cocktails in the list.");
        } else {
            for (Cocktail coctail : coctailList) {
                System.out.println(coctail.returnCoctailName());
            }
        }
    }

    void createCocktail() {
        System.out.println("Please type name of the cocktail");
        checkIfCocktailAlreadyInTheList(scannerString());
    }

    private void checkIfCocktailAlreadyInTheList(String cocktailName) {
        for (Cocktail cocktailInTheList : coctailList) {
            if ((cocktailInTheList.returnCoctailName().equals(cocktailName))) {
                System.out.println("This cocktail already exist in the list");
                return;
            }
        }
        createCocktailObject(cocktailName);
    }

    private void createCocktailObject(String cocktailName) {
        Scanner scanner = new Scanner(System.in);
        boolean onlyIngredient = true;
        ArrayList<Ingredients> cocktailIngredients = new ArrayList<>();
        do {
            System.out.println("Please specify ingredients");
            cocktailIngredients.add(createIngredientObject());
            System.out.println("Add more ingredients?\n" +
                    "1.Yes.\n" +
                    "2.No.");
            int choice = scanner.nextInt();
            switch (choice) {
                case 2:
                    onlyIngredient = false;
            }
        } while (onlyIngredient);
        Cocktail cocktail = new Cocktail(cocktailName, cocktailIngredients);
        addCocktailToOverallList(cocktail);
        pushNewlyCreatedCocktailToDB(cocktail);

        System.out.println("Cocktail " + cocktailName);
        System.out.println("Ingredients");
        for (Ingredients ingre : cocktail.returnIngredients()) {
            System.out.print(ingre.returnIngredientName() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private boolean checkIfIngredientAlreadyInTheList(String ingredientName) {
        for (Ingredients ingredientInTheList : ingredientsList) {
            if (ingredientInTheList.returnIngredientName().equals(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    private Ingredients returnIngredientFromAlreadyCreatedIngredients(String i_name) {
        for (Ingredients ingredientsThatAlreadyInTheList : ingredientsList) {
            if (ingredientsThatAlreadyInTheList.returnIngredientName().equals(i_name)) {
                return ingredientsThatAlreadyInTheList;
            }
        }
        return null;
    }


    void createIngredient() {
        System.out.println("Please specify ingredient name that you like to add.");
        pushNewlyCreatedIngredientToDB(createIngredientObject());
        //Ingredients createdSingleIngredient = createIngredientObject();
    }

    private Ingredients createIngredientObject() {
        String ingredientName = scannerString();
        if (checkIfIngredientAlreadyInTheList(ingredientName)) {
            return returnIngredientFromAlreadyCreatedIngredients(ingredientName);
        } else {
            Ingredients createdIngredient = new Ingredients(ingredientName);
            addIngredientsToOverallList(createdIngredient);
            System.out.println("Created Ingredient " + createdIngredient.returnIngredientName());
            return createdIngredient;
        }
    }

    private void addIngredientsToOverallList(Ingredients addThis) {
        ingredientsList.add(addThis);
    }

    private void addCocktailToOverallList(Cocktail addThis) {
        coctailList.add(addThis);
    }

    private String scannerString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void populateCoctailList() {
    }

//    private void populateIngredientList(){
//        ingredientsList = removeIngredientDuplicates(DBConnection.returnDBIngredients());
//    }

    private ArrayList<Ingredients> removeIngredientDuplicates(ArrayList<Ingredients> inreList) {
        ArrayList<Ingredients> returnedList = new ArrayList<>();
        for (Ingredients ingre : inreList) {
            for (int i = 0; i < returnedList.size(); i++) {
                if (ingre.returnIngredientName().equals(returnedList.get(i).returnIngredientName())) {
                    break;
                } else {
                    returnedList.add(ingre);
                }
            }
        }
        return returnedList;
    }

    private void pushNewlyCreatedCocktailToDB(Cocktail cocktail) {
        try {
            DBIO.populateCoctailsNamesTable(cocktail.returnCoctailName());
            for (Ingredients ingre : cocktail.returnIngredients()) {
                DBIO.creatingIngredient(ingre.returnIngredientName(), cocktail.returnCoctailName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void pushNewlyCreatedIngredientToDB(Ingredients ingre) {
        try {
            DBIO.populateIngredientsNamesTable(ingre.returnIngredientName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializationStart() {
        try {
            ingredientsList = FirstProgramInitialize.returnDBIngredients();
            //coctailList = DBConnection.returnCocktailList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

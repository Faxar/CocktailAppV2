package com.company;

import Objects.Cocktail;
import Objects.Ingredients;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Alazerus on 8/22/2017.
 */
public class Controller {

    private List<Ingredients> ingredientsList;
    private List<Cocktail> coctailList;

    Controller(){
        ingredientsList = new ArrayList<>();
        coctailList = new ArrayList<>();
    }

    public List returnIngredients(){
        return ingredientsList;
    }

    void announceIngredientList(){
        if(ingredientsList.size() <= 0){
            System.out.println("No ingredients in the list.");
        } else {
            for(Ingredients ingr : ingredientsList){
                System.out.println(ingr.returnIngredientName());
            }
        }
    }

    void announceCocktailList(){
        if(coctailList.size() <= 0){
            System.out.println("No cocktails in the list.");
        } else {
            for(Cocktail coctail: coctailList){
                System.out.println(coctail.returnCoctailName());
            }
        }
    }

    void createCocktail(){
        System.out.println("Please type name of the cocktail");
        checkIfCocktailAlreadyInTheList(scannerString());
    }

    private void checkIfCocktailAlreadyInTheList (String cocktailName){
        for (Cocktail cocktailInTheList : coctailList) {
            if((cocktailInTheList.returnCoctailName().equals(cocktailName))){
                System.out.println("This cocktail already exist in the list");
                return;
            }
        }
        createCocktailObject(cocktailName);
    }

    private void createCocktailObject(String cocktailName){
        Scanner scanner = new Scanner(System.in);
        boolean onlyIngredient = true;
        ArrayList<Ingredients> cocktailIngredients = new ArrayList<>();
        System.out.println("Please specify ingredients");
        do{
            cocktailIngredients.add(createIngredientObject());
            System.out.println("Add more ingredients?\n" +
                    "1.Yes.\n" +
                    "2.No.");
            int choice = scanner.nextInt();
            switch (choice){
                case 2:
                    onlyIngredient = false;
            }
        }while(onlyIngredient);
        Cocktail coctail = new Cocktail(cocktailName, cocktailIngredients);
        addCocktailToOverallList(coctail);
        System.out.println("Cocktail {" + cocktailName + "} were created.");
    }

    private boolean checkIfIngredientAlreadyInTheList (String ingredientName){
        for (Ingredients ingredientInTheList: ingredientsList) {
            if (ingredientInTheList.returnIngredientName().equals(ingredientName)){
                return true;
            }
        }
        return false;
    }

    private Ingredients returnIngredientFromAlreadyCreatedIngredients(String name){
        for (Ingredients ingredientsThatAlreadyInTheList: ingredientsList) {
            if(ingredientsThatAlreadyInTheList.returnIngredientName().equals(name)){
                return ingredientsThatAlreadyInTheList;
            }
        }
        return null;
    }


    void createIngredient(){
        System.out.println("Please specify ingredient name that you like to add.");
        Ingredients createdSingleIngredient = createIngredientObject();
    }

    private Ingredients createIngredientObject(){
        String ingredientName = scannerString();
        System.out.println("Scanner = " + ingredientName);
        if(checkIfIngredientAlreadyInTheList(ingredientName)){
            return returnIngredientFromAlreadyCreatedIngredients(ingredientName);
        } else {
            Ingredients createdIngredient = new Ingredients(ingredientName);
            addIngredientsToOverallList(createdIngredient);
            return createdIngredient;
        }
    }

    private void addIngredientsToOverallList(Ingredients addThis){
        ingredientsList.add(addThis);
    }

    private void addCocktailToOverallList(Cocktail addThis){
        coctailList.add(addThis);
    }

    private String scannerString(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void enterEntryToDB(){
        System.out.println("Enter cocktail");
        String cocktailName = scannerString();
        try{
            DBAccess.populateCoctailsNamesTable(cocktailName);
        }catch (SQLException e){
            e.printStackTrace();
        }
        try{
            DBAccess.linkDBEntriesCocktailsToIngredients(cocktailName);
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

}

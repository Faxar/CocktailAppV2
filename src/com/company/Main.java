package com.company;

import jdk.internal.org.xml.sax.XMLReader;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        Controller controller = new Controller();

        while (!exit){
            System.out.println("1. View cocktails\n" +
                    "2. View used ingredients\n" +
                    "3. Add new cocktail\n" +
                    "4. Add new ingredient");
            int selection = scanner.nextInt();
            switch (selection){
                case 1:
                    controller.announceCocktailList();
                    break;
                case 2:
                    controller.announceIngredientList();
                    break;
                case 3:
                    controller.createCocktail();
                    break;
                case 4:
                    controller.createIngredient();
                    break;
            }
        }

    }

    private static String returnString(){
        return new Scanner(System.in).nextLine();
    }

}

package Objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alazerus on 8/22/2017.
 */
public class Cocktail {

    private String coctailName;
    private List<Ingredients> ingridients;

    public Cocktail(){
        coctailName = "Empty";
        ingridients = new ArrayList<>();
    }

    public Cocktail(String name, ArrayList ingridients){
        this.coctailName = name;
        this.ingridients = ingridients;
    }

    public void addIngredients(Ingredients ingridient){
        this.ingridients.add(ingridient);
        System.out.println("Ingredient " + ingridient + " have been added to coctail " + coctailName);
    }

    public String returnCoctailName(){
        return coctailName;
    }

    @Override
    public String toString() {
        return "Cocktail{" +
                "coctailName='" + coctailName + '\'' +
                ", ingridients=" + ingridients +
                '}';
    }

    public List<Ingredients> returnIngredients (){
        return ingridients;
    }

}

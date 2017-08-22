package Objects;

/**
 * Created by Alazerus on 8/22/2017.
 */
public class Ingredients {

    private String ingredientName;

    public Ingredients(){
        ingredientName = "Empty";
    }

    public Ingredients(String ingredient){
        this.ingredientName = ingredient;
    }

    public String returnIngredientName(){
        return ingredientName;
    }

    @Override
    public String toString() {
        return "ingredientName='" + ingredientName + '\'';
    }

}

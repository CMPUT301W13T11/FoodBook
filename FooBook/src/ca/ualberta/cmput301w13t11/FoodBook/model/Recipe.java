package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Random;

import android.content.ContentValues;

/**
 * Class which models a recipe.
 * @author The FoodBook team
 *
 */
public class Recipe {
	private long uri;
	private User author;
	private String title;
	private String instructions;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Photo> photos;
		
	/**
	 * Constructor
	 * Used when a the Recipes Db is queried and a row is returned to transform
	 * the entry into a Recipe.
	 */
	public Recipe(long uri, User author, String title, String instructions, ArrayList<Ingredient> ingreds) {
		this.uri = uri;
		this.setAuthor(author);
		this.title = title;
		this.setInstructions(instructions);
		this.ingredients = ingreds;
		this.photos = new ArrayList<Photo>();	
	}
	
	/**
	 * Constructor
	 * Used when the Recipes Db is queried and a row is returned to transfrom the entry into a recipe..
	 */
	public Recipe(long uri, User author, String title, String instructions, 
			ArrayList<Ingredient> ingreds, ArrayList<Photo> photos) {
		this.uri = uri;
		this.setAuthor(author);
		this.title = title;
		this.setInstructions(instructions);
		this.ingredients = ingreds;
		this.photos = photos;	
	}
	
	/**
	 * Constructor with only a URI arg.
	 * @param uri The URI we want to associate with this recipe.
	 */
	public Recipe(long uri)
	{
		this.uri = uri;
		this.title = "";
		this.author = new User("");
		this.instructions = "";
		this.ingredients = new ArrayList<Ingredient>();
		this.photos = new ArrayList<Photo>();
	}
	

	/**
	 * Constructor.
	 * @param title The title of the recipe.
	 * @param instructions The set of instructions for executing the recipe.
	 */
	public Recipe(User author, String title, String instructions)
	{
		this.author = author;
		this.title = title;
		this.setInstructions(instructions);
		this.ingredients = new ArrayList<Ingredient>();
		this.photos = new ArrayList<Photo>();
		this.uri = System.currentTimeMillis();
	}
	
	/**
	 * Returns title of recipe - method of this name needed to populate ListViews.
	 * @return  Title of recipe.
	 */
	public String toString()
	{
		return this.title;
	}
	
	/**
	 * @return The title of the Recipe.
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * @return The author of the Recipe.
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * @param author The new author for this recipe; this.author = author
	 */
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * @return The instructions for the Recipe.
	 */
	public String getInstructions() {
		return instructions;
	}

	/**
	 * @param instructions The new set of instructions for this recipe; this.instructions = instructions
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @return An ArrayList of the ingredients of this recipe.
	 */
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients The new set of ingredients for thsi recipe; this.ingredients = ingredients
	 */
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * @return An ArrayList of the photos of this recipe.
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	/**
	 * Add the given Photo to this Recipes list of photos.
	 * @param photo The Photo to be added.
	 */
	public void addPhoto(Photo photo)
	{
		photos.add(photo);
	}
	
    /**
     * Converts a Recipe to a ContentValues object to be stored in the database.
     * @param recipe The recipe to be converted.
     * @return An appropriately transformed copy of the Recipe for database storage.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("URI", uri);
        values.put("title", title);
        values.put("author", author.getName());
        values.put("instructions", instructions);
        return values;
    }

	/**
	 * Generates a simple recipe to be used in the tests.
	 * @return A recipe to be used in the tests.
	 */
	static public Recipe generateTestRecipe()
	{
		User user = new User("tester");
		String title = "test";
		String instructions = "test instructions";
		long uri = 110101012;
		
		/* Generate ingredient list. */
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("egg", "whole", (float) 1/2));
		ingredients.add(new Ingredient("butter", "tbsp", (float) 4));
		ingredients.add(new Ingredient("sdf", "sdf", (float) 1/4));
		ingredients.add(new Ingredient("milk", "mL", (float) 5000));
		ingredients.add(new Ingredient("veal", "the whole baby cow", (float) 1));

		Recipe ret = new Recipe(uri, user, title, instructions, ingredients);
		return ret;

	}
	
	/**
	 * Generates a simple recipe to be used in the tests with a random URI.
	 * @return A recipe to be used in the tests.
	 */
	static public Recipe generateRandomTestRecipe()
	{
		User user = new User("tester");
		String title = "test";
		String instructions = "test instructions";
		Random r = new Random();
		int x = r.nextInt(1000);
		long uri = System.currentTimeMillis() + x;
		
		/* Generate ingredient list. */
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredients.add(new Ingredient("egg", "whole", (float) 1/2));
		ingredients.add(new Ingredient("butter", "tbsp", (float) 4));

		Recipe ret = new Recipe(uri, user, title, instructions, ingredients);
		return ret;

	}
	
	/**
	 * Get the recipe's URI.
	 * @return URI
	 */
	public long getUri() {
		return uri;
	}

	/**
	 * Gives a text representation of the Recipe.  To be used when we e-mail the Recipe.
	 */
	public String getTextVersion(){
		String returnString =  "Author: "+author.getName()+"\nTitle: "+title+"\nIngredients:\n";
		for(Ingredient I:ingredients){
			returnString += I.toString()+"\n";
		}
		returnString = returnString +"Instructions: \n"+instructions+"\n";
		return returnString;
	}

	/**
	 * @param title The new title for the Recipe; this.title = title
	 */
	public void setTitle(String title) {
		this.title = title;
	}


}

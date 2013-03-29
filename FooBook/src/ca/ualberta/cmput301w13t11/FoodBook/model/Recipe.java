package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Collection;

import android.content.ContentValues;

/**
 * Class which models a recipe. (TODO: better description)
 * @author 
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
		this.author = new User("");
		this.instructions = "";
		this.ingredients = new ArrayList<Ingredient>();
		this.photos = new ArrayList<Photo>();
	}
	
	/**
	 * Constructor.
	 * @param title The title of the recipe.
	 * @param author The author of the recipe.
	 */
	public Recipe(User author, String title)
	{
		this.setAuthor(author);
		this.title = title;
		this.setInstructions("");
		this.ingredients = new ArrayList<Ingredient>();
		this.photos = new ArrayList<Photo>();
		/* TODO: find a good hashing function in order to assign to each recipe a unique URI
		* such that duplicates cannot be misinterpreted for one another on the server. */
		
		// at the moment, we will just get the current time in milliseconds and use that as the uri
		this.uri = System.currentTimeMillis();

	}
	
	/**
	 * Constructor.
	 * @param title The title of the recipe.
	 * @param instructions The set of instructions for executing the recipe.
	 */
	public Recipe(User author, String title, String instructions)
	{
		this.setAuthor(author);
		this.title = title;
		this.setInstructions(instructions);
		this.ingredients = new ArrayList<Ingredient>();
		this.photos = new ArrayList<Photo>();
		/* TODO: find a good hashing function in order to assign to each recipe a unique URI
		* such that duplicates cannot be misinterpreted for one another on the server. */
		// at the moment, we will just get the current time in milliseconds and use that as the uri
		this.uri = System.currentTimeMillis();
		
	}
	
	/**
	 *  Constructor.
	 * @param title The title of the recipe.
	 * @param instructions The set of instructions used to execute the recipe.
	 * @param ingredients The list of ingredients associated with this recipe.
	 */
	public Recipe(User author, String title, String instructions, ArrayList<Ingredient> ingredients)
	{
		this.setAuthor(author);
		this.title = title;
		this.setInstructions(instructions);
		this.ingredients = ingredients;
		this.photos = new ArrayList<Photo>();

		/* TODO: find a good hashing function in order to assign to each recipe a unique URI
		* such that duplicates cannot be misinterpreted for one another on the server. */
		// at the moment, we will just get the current time in milliseconds and use that as the uri
		this.uri = System.currentTimeMillis();
	}
	
	public Recipe(User author, String title, String instructions, ArrayList<Ingredient> ingredients,
				ArrayList<Photo> photos)
	{
		this.setAuthor(author);
		this.title = title;
		this.setInstructions(instructions);
		this.ingredients = ingredients;
		this.photos = photos;

		/* TODO: find a good hashing function in order to assign to each recipe a unique URI
		* such that duplicates cannot be misinterpreted for one another on the server. */
		// at the moment, we will just get the current time in milliseconds and use that as the uri
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
	
	public String getTitle()
	{
		return this.title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void addPhoto(Photo photo)
	{
		photos.add(photo);
	}
	
	/**
	 * Delete the given photo from this recipe's photos attribute (if it exists).
	 * @param Photo the Photo to be removed from the photos list.
	 */
	public void deletePhoto(Photo photo)
	{
		for(int i = 0; i < photos.size(); i++) {
			Photo p = photos.get(i);
			if (p.getId().equals(photo.getId()))
			{
				photos.remove(p);
			}
		}
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
		
		/* Generate photos */
		//String name1 = "test1";
		//String name2 = "test2";
		//byte[] byte_data1 = new byte[10];
		//byte[] byte_data2 = new byte[10];

		//Photo photo1 = new Photo(name1, byte_data1);
		//Photo photo2 = new Photo(name2, byte_data2);
		//ArrayList<Photo> photos = new ArrayList<Photo>();
		//photos.add(photo1);
		//photos.add(photo2);

		/* Generate recipe. */
		//Recipe ret = new Recipe(uri, user, title, instructions, ingredients, photos);
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
	 * @uml.property  name="serverRecipe"
	 * @uml.associationEnd  inverse="recipe:ca.ualberta.cmput301w13t11.FoodBook.model.ServerRecipe"
	 * @uml.association  name="re-models"
	 */
	private ServerRecipe serverRecipe;

	/**
	 * Getter of the property <tt>serverRecipe</tt>
	 * @return  Returns the serverRecipe.
	 * @uml.property  name="serverRecipe"
	 */
	public ServerRecipe getServerRecipe() {
		return serverRecipe;
	}

	/**
	 * Setter of the property <tt>serverRecipe</tt>
	 * @param serverRecipe  The serverRecipe to set.
	 * @uml.property  name="serverRecipe"
	 */
	public void setServerRecipe(ServerRecipe serverRecipe) {
		this.serverRecipe = serverRecipe;
	}

	/**
	 * @uml.property  name="clientHelper"
	 * @uml.associationEnd  inverse="recipe:ca.ualberta.cmput301w13t11.FoodBook.model.ClientHelper"
	 * @uml.association  name="transforms"
	 */
	private ClientHelper clientHelper;

	/**
	 * Getter of the property <tt>clientHelper</tt>
	 * @return  Returns the clientHelper.
	 * @uml.property  name="clientHelper"
	 */
	public ClientHelper getClientHelper() {
		return clientHelper;
	}

	/**
	 * Setter of the property <tt>clientHelper</tt>
	 * @param clientHelper  The clientHelper to set.
	 * @uml.property  name="clientHelper"
	 */
	public void setClientHelper(ClientHelper clientHelper) {
		this.clientHelper = clientHelper;
	}

	/**
	 * @uml.property  name="user"
	 * @uml.associationEnd  multiplicity="(0 -1)" inverse="recipe:ca.ualberta.cmput301w13t11.FoodBook.model.User"
	 */
	private Collection<?> user;

	/**
	 * Getter of the property <tt>user</tt>
	 * @return  Returns the user.
	 * @uml.property  name="user"
	 */
	public Collection<?> getUser() {
		return user;
	}

	/**
	 * Setter of the property <tt>user</tt>
	 * @param user  The user to set.
	 * @uml.property  name="user"
	 */
	public void setUser(Collection<?> user) {
		this.user = user;
	}
	/**
	 * Gives a text representation of the Recipe
	 */
	public String getTextVersion(){
		String returnString =  "Author: "+author.getName()+"\nTitle: "+title+"\nIngredients:\n";
		for(Ingredient I:ingredients){
			returnString += I.toString()+"\n";
		}
		returnString = returnString +"Instructions: \n"+instructions+"\n";
		return returnString;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

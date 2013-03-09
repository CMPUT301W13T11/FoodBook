package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class which models a recipe. (TODO: better description)
 * @author 
 *
 */
public class Recipe {
	private User author;
	private String title;
	private String instructions;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<Photo> photos;
	private String uri;
	
	
	
	/**
	 * Constructor.
	 * @param title The title of the recipe.
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
		this.uri = "";
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
		this.uri = "";
		
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
		this.uri = "";
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
		this.uri = "";
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
	
	public void deletePhoto(Photo photo)
	{
		for(Photo p : photos) {
			if (p.getName() == photo.getName())
			{
				photos.remove(p);
			}
		}
	}

	public String getUri() {
		return uri;
	}

}

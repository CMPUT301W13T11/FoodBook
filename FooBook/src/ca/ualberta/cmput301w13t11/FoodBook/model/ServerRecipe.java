package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.util.ArrayList;

/**
 * A recipe class that can be written directly to the server -- ie. rather
 * than storing images as a Bitmap it encodes them as a string using Base64
 * encoding.  Also provides functionality for converting a ServerRecipe
 * to a regular recipe which can be utilized by the rest of the program.
 * @author Marko Babic
 *
 */
public class ServerRecipe{
	
	private User author;
	private String title;
	private String instructions;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<ServerPhoto> photos;
	private long uri;
	
	/**
	 * Constructor -- creates a ServerRecipe from the given Recipe.
	 * @param recipe The Recipe from which a ServerRecipe is to be constructed.
	 */
	public ServerRecipe(Recipe recipe)
	{
		this.author = recipe.getAuthor();
		this.title = recipe.getTitle();
		this.instructions = recipe.getInstructions();
		this.ingredients = recipe.getIngredients();
		this.uri = recipe.getUri();
		
		ArrayList<Photo> photos = recipe.getPhotos();
		this.photos = new ArrayList<ServerPhoto>();
		ServerPhoto temp;
		for (Photo p : photos) {
			temp = new ServerPhoto(p);
			this.photos.add(temp);
		}
	}
	

	/**
	 * Converts the given ServerRecipe to a Recipe.
	 * @param sr The ServerRecipe to be converted.
	 * @return A Recipe transfomred from the ServerRecipe.
	 */
	public static Recipe toRecipe(ServerRecipe sr)
	{
		Recipe ret = new Recipe(sr.getUri(), sr.getAuthor(), sr.getTitle(), sr.getInstructions(),
								sr.getIngredients());
		for (ServerPhoto sp : sr.photos) {
			ret.addPhoto(ServerPhoto.toPhoto(sp));
		}
		
		return ret;
	}


	/**
	 * @return ArrayList of photos associated with this ServerRecipe.
	 */
	public ArrayList<ServerPhoto> getPhotos()
	{
		return photos;
	}
	
	/**
	 * @return The Author of this ServerRecipe (a User object).
	 */
	public User getAuthor() {
		return author;
	}


	/**
	 * Set the author attribute to the given author arg.
	 * @param author The setter.
	 */
	public void setAuthor(User author) {
		this.author = author;
	}


	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return instructions;
	}


	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}


	/**
	 * @return the ingredients associated with the ServerRecipe
	 */
	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}


	/**
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}


	/**
	 * @return the uri of the ServerRecipe
	 */
	public long getUri() {
		return uri;
	}


	/**
	 * @param uri The new URI of the ServerRecipe.
	 */
	public void setUri(long uri) {
		this.uri = uri;
	}
}

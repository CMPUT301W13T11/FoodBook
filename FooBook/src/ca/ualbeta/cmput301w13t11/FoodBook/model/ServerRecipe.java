package ca.ualberta.cmput301w13t11.FoodBank.model;

import java.util.ArrayList;

/**
 * A recipe class that can be written directly to the server -- ie. rather
 * than storing images as a Bitmap it encodes them as a string using Base64
 * encoding.  Also provides functionality for converting a ServerRecipe
 * to a regular recipe which can be utilized by the rest of the program.
 * @author Marko
 *
 */
public class ServerRecipe{
	
	private User author;
	private String title;
	private String instructions;
	private ArrayList<Ingredient> ingredients;
	private ArrayList<ServerPhoto> photos;
	private String uri;
	
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
	

	public static Recipe toRecipe(ServerRecipe sr)
	{
		Recipe ret = new Recipe(sr.getAuthor(), sr.getTitle(), sr.getInstructions(),
								sr.getIngredients());
		for (ServerPhoto sp : sr.photos) {
			ret.addPhoto(ServerPhoto.toPhoto(sp));
		}
		
		return ret;
	}


	public ArrayList<ServerPhoto> getPhotos()
	{
		return photos;
	}
	
	
	public User getAuthor() {
		return author;
	}


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
	 * @return the ingredients
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
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}


	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}

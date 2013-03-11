package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.util.Base64;


/**
 * Photo type to be used by Server Recipe -- its constructor makes an explicit conversion.
 * @author Marko
 *
 */
public class ServerPhoto {

	private String name;
	private String encoded_bitmap;
	
	/**
	 *  Constructor - turns the given photo into a photo that can be written to the server.
	 */
	public ServerPhoto(Photo photo)
	{
		this.name = photo.getName();
		this.encoded_bitmap = new String(Base64.encode(photo.getBitData(), Base64.DEFAULT));
	}

	/**
	 * Transforms a ServerPhoto back to a photo.
	 * @param sp The ServerPhoto to be transformed.
	 * @return A photo object constructed from the given ServerPhoto.
	 */
	public static Photo toPhoto(ServerPhoto sp)
	{
		byte[] data = Base64.decode(sp.encoded_bitmap, Base64.DEFAULT);
		return new Photo(sp.getName(), data);
	}
	
	/**
	 * 
	 * @return Name of photo.
	 */
	public String getName() {
		return name;
	}


	/**
	 * 
	 * @return the Base64 String encoding the bitmap of the original image.
	 */
	public String getEncodedBitmap() {
		return encoded_bitmap;
	}
}

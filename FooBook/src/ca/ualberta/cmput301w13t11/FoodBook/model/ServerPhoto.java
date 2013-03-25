package ca.ualberta.cmput301w13t11.FoodBook.model;

import android.util.Base64;


/**
 * Photo type to be used by Server Recipe -- its constructor makes an explicit conversion from
 * Photo to ServerPhoto.  Mainly, the Bitmap used to store the data image in a regular photo
 * is encoded into a Base64 String such that the data can be uploaded to a server.
 * @author Marko Babic
 *
 */

public class ServerPhoto {

	private String id;
	private String path;
	private String encoded_bitmap;
	/**
	 *  Constructor - turns the given photo into a photo that can be written to the server.
	 */
	public ServerPhoto(Photo photo)
	{
		this.id = photo.getId();
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
		return new Photo(sp.getId(), data);
	}
	
	/**
	 * 
	 * @return Name of photo.
	 */
	public String getId() {
		return id;
	}


	/**
	 * @return the Base64 String encoding the bitmap of the original image.
	 */
	public String getEncodedBitmap() {
		return encoded_bitmap;
	}
}


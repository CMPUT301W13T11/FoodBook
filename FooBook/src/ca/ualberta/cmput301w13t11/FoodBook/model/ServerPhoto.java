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
	
	public ServerPhoto(Photo photo)
	{
		this.name = photo.getName();
		this.encoded_bitmap = new String(Base64.encode(photo.getBitData(), Base64.DEFAULT));
	}

	public static Photo toPhoto(ServerPhoto sp)
	{
		byte[] data = Base64.decode(sp.encoded_bitmap, Base64.DEFAULT);
		return new Photo(sp.getName(), data);
	}
	
	public String getName() {
		return name;
	}


	public String getEncodedBitmap() {
		return encoded_bitmap;
	}
}

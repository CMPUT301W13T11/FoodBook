package ca.ualberta.cmput301w13t11.FoodBook.model;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Needs to be implemented at some point.
 * Current proposal: store a Bitmap on local SD card which can then be encoded into a Base 64 string for online
 * publication).
 * @author Marko Tomislav Babic
 *
 */
public class Photo {

	private String id;
	private String path;
	private byte[] bit_data;
	
	/**
	 * Construct photo from file name (data is already stored on disk, must read it if needed)
	 * @param name
	 */
	public Photo(String id)
	{
		this.id = id;
		this.path = null;
		bit_data = null;
	}
	
	/**
	 * Construct photo from given name and byte array.
	 * @param name
	 * @param data
	 */
	public Photo(String id, String path)
	{
		this.id = id;
		this.path = path;
		bit_data = null;
	}
	
	/**
	 * Create a photo from the given bitmap (we first compress the data to make sure
	 * we aren't storing/downloading/uploading huge files).
	 * @param bitmap The Bitmap which encodes the photo information.
	 */
	
	public Photo(String id, String path, Bitmap bitmap)
	{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 30, out);
		bit_data = out.toByteArray();
		long time = System.currentTimeMillis();
		this.id = String.valueOf(time);
	}
	
	
	 //Returns the bitmap associated with this photo.
	 //@return Bitmap associated with the photo.
	 
	public Bitmap getPhotoBitmap()
	{
		return BitmapFactory.decodeByteArray(bit_data, 0, bit_data.length);
	}
	
	public String getId()
	{
		return id;
	}
	public String getPath() {
		return path;
	}
}

package ca.ualberta.cmput301w13t11.FoodBook.model;
import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class representing a photo in the program.
 * @author mbabic
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
	public Photo(String id, byte[] byte_array)
	{
		this.id = id;
		bit_data = byte_array;
		
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
	 * Construct photo from given name, path, and byte_array.
	 * @param name
	 * @param data
	 */
	public Photo(String id, String path, byte[] byte_array)
	{
		this.id = id;
		this.path = path;
		bit_data = byte_array;
	}
	
	/**
	 * Create a photo from the given bitmap (we first compress the data to make sure
	 * we aren't storing/downloading/uploading huge files).
	 * @param bitmap The Bitmap which encodes the photo information.
	 */
	public Photo(String id, String path, Bitmap bitmap)
	{
		this.id = id;
		this.path = path;

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 30, out);
			bit_data = out.toByteArray();
		}
		else {
			bit_data = null;
		}
		this.id = id;
	}
	
	/**
	 * @return The bit data for this photo.
	 */
	public byte[] getBitData()
	{
		return bit_data;
	}
	
	/**
	 *Returns the bitmap associated with this photo.
	 *@return Bitmap associated with the photo.
	 */
	public Bitmap getPhotoBitmap()
	{
		if (bit_data != null)
			return BitmapFactory.decodeByteArray(bit_data, 0, bit_data.length);
		return null;
	}
	
	/**
	 * @return The Id for this photo.
	 */
	public String getId()
	{
		return id;
	}
	
	/**
	 * @return The Id for this photo.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Set the path for this photo to be the given path.
	 * @param The new path for this photo.
	 */
	public void setPath(String path)
	{
		this.path = path;
	}

}

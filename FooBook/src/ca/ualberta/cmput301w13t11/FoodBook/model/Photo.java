package ca.ualberta.cmput301w13t11.FoodBook.model;
import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class representing a photo object.
 * @author mbabic
 *
 */
public class Photo {

	/**
	 * The name of the photo.
	 */
	private String id;
	
	/**
	 * A path indicating where the photo has been saved on a device if it has been saved at all.
	 */
	private String path;
	/**
	 * The byte array which stores the actual image file's data.
	 */
	private byte[] bit_data;
	
	/**
	 * Construct photo from given id.
	 * @param name The id/name of the Photo.
	 */
	public Photo(String id)
	{
		this.id = id;
		this.path = null;
		bit_data = null;
	}
	
	/**
	 * Construct photo from given id and byte array.
	 * @param id The id/name of the Photo.
	 * @param data The byte array storing the image file's data.
	 */
	public Photo(String id, byte[] byte_array)
	{
		this.id = id;
		this.path = null;
		bit_data = byte_array;
		
	}
	
	/**
	 * Construct photo from given id and path.
	 * @param id The id/name of the Photo.
	 * @param path The path where the Photo is stored on the local device.
	 */
	public Photo(String id, String path)
	{
		this.id = id;
		this.path = path;
		bit_data = null;
	}
	
	/**
	 * Construct photo from given id, path, and byte_array.
	 * @param id The id/name of the Photo.
	 * @param path The path where the Photo is stored on the local device.
	 * @param byte_array The byte array storing the image file's data.
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
	 * @param id The id/name of the Photo.
	 * @param path The path where the Photo is stored on the local device.
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
	 *@return The bitmap associated with the photo.
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

	public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("path", path);
        return values;
	}
	
}

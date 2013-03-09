package ca.ualberta.cmput301w13t11.FoodBank.model;
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

	//
	// Open question: should a photo know which recipe it is associated with?
	// Probably not, but I can see arguments for doing it.
	//
	private String name;
	private byte[] bit_data;
	
	
	/**
	 * Construct photo from given name and byte array.
	 * @param name
	 * @param data
	 */
	public Photo(String name, byte[] data)
	{
		this.name = name;
		this.bit_data = data;
	}
	
	/**
	 * Create a photo from the given bitmap (we first compress the data to make sure
	 * we aren't storing/downloading/uploading huge files).
	 * @param bitmap The Bitmap which encodes the photo information.
	 */
	public Photo(Bitmap bitmap)
	{

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 30, out);
		
		bit_data = out.toByteArray();
		
		/* 
		 * A simple timestamp isn't rigorous enough to insure name uniqueness, 
		 * I'll figure out a better way to do this at some point.
		 */
		
		long time = System.currentTimeMillis();
		name = String.valueOf(time);
	}
	
	/**
	 *  Returns the bitmap associated with this photo.
	 * @return Bitmap associated with the photo.
	 */
	public Bitmap getPhotoBitmap()
	{
		return BitmapFactory.decodeByteArray(bit_data, 0, bit_data.length);
	}

	public byte[] getBitData()
	{
		return bit_data;
	}
	public String getName() {
		return name;
	}
}

package ca.ualberta.cmput301w13t11.FoodBook.model;

import java.io.File;
import java.util.Random;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import ca.ualberta.cmput301w13t11.FoodBook.FoodBookApplication;
import java.util.Collection;


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
		if (photo.getBitData() != null) {
			this.encoded_bitmap = new String(Base64.encode(photo.getBitData(), Base64.DEFAULT));
		}
		else
			this.encoded_bitmap = null;
	}

	/**
	 * Transforms a ServerPhoto back to a photo.
	 * @param sp The ServerPhoto to be transformed.
	 * @return A photo object constructed from the given ServerPhoto.
	 */
	public static Photo toPhoto(ServerPhoto sp)
	{
		Random r = new Random();
		int x = r.nextInt(1000);
		String imgPath = null;
		File file = null;
		String newId = Long.toString(System.currentTimeMillis()) + Long.toString((System.currentTimeMillis() + x) % 1000);

		
		/* We now need to obtains the Sd Card from the device. */
		FoodBookApplication app = FoodBookApplication.getApplicationInstance();
		String state = app.getState();
		
		/* We first get the save path on this device. */
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			imgPath = app.getSdCardPath() + newId;
			file = new File(imgPath);
		}
	
		Log.d("ServerPhoto.toPhoto()", "imgPath = " + imgPath);
		Log.d("ServerPhoto.toPhoto()", "id to store = " + newId);
		if (sp.encoded_bitmap != null) {
			byte[] data = Base64.decode(sp.encoded_bitmap, Base64.DEFAULT);
			return new Photo(newId, imgPath, data);
		}
		return new Photo(sp.getId(), imgPath);
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


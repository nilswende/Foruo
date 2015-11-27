package com.fotoup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.view.Display;
import android.widget.Toast;
import java.io.*;

public class Storage {

	protected static Toast saveImageToInternalStorage(Activity act, Bitmap bitmap, ToastText text) {
		Context context = act.getApplicationContext();
		ContextWrapper cw = new ContextWrapper(context);
		File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);
		File[] contents = directory.listFiles();

		int count = contents.length + 1;
		File path = new File(directory, "profile" + count + ".jpg");

		try (FileOutputStream out = new FileOutputStream(path)) {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		Storage.saveSmallImageToInternalStorage(act, bitmap);

		return makeToast(context, directory, count, text);
	}

	private static File saveSmallImageToInternalStorage(Activity act, Bitmap bitmap) {
		ContextWrapper cw = new ContextWrapper(act.getApplicationContext());
		File directory = cw.getDir("SmallPictures", Context.MODE_PRIVATE);
		File[] contents = directory.listFiles();
		int count = contents.length + 1;
		File path = new File(directory, "profile" + count + ".jpg");

		Display display = act.getWindowManager().getDefaultDisplay();
		int displayWidth = display.getWidth();
		int itemSize = ((displayWidth - 70) / 3);

		try (FileOutputStream out = new FileOutputStream(path)) {
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, itemSize, itemSize, false);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	private static Toast makeToast(Context context, File dir, int count, ToastText text) {
		CharSequence sequence;
		switch (text) {
			default:
			case SAVED_TO:
				sequence = "picture saved to: " + dir.toString() + "/" + count;
				break;
			case IMPORTED:
				sequence = "Imported!";
				break;
		}
		int duration = Toast.LENGTH_LONG;
		return Toast.makeText(context, sequence, duration);
	}
}

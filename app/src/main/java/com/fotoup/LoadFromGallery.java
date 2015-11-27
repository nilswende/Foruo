package com.fotoup;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import java.io.File;

public class LoadFromGallery extends Fragment {

	public static int RESULT_LOAD_IMAGE = 1;
	public        int alrCAP            = 0;

	protected View root;

	Bitmap bitmap;
	String path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			alrCAP = 1;
			String path = savedInstanceState.getString("parcebitmap");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			bitmap = BitmapFactory.decodeFile(path, options);
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		this.root = inflater.inflate(R.layout.frag3, container, false);

		if (bitmap != null) {
			ImageView img = (ImageView) root.findViewById(R.id.imgV);
			img.setImageBitmap(bitmap);
		}

		Button loadButton = (Button) root.findViewById(R.id.StartImport);
		loadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (alrCAP == 0) {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					ImageView img = (ImageView) root.findViewById(R.id.imgV);
					img.setBackground(null);
					startActivityForResult(intent, RESULT_LOAD_IMAGE);
				}
				else {
					Context context = getActivity().getApplicationContext();
					CharSequence text = "Picture already selected.";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}

		});

		Button Import = (Button) root.findViewById(R.id.Import);

		Import.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (alrCAP != 0) {
					Toast t = Storage.saveImageToInternalStorage(getActivity(), bitmap, ToastText.IMPORTED);
					t.show();
				}
				else {
					Context context = getActivity().getApplicationContext();
					CharSequence text = "No picture selected!";
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
			}

		});

		Button Cancel = (Button) root.findViewById(R.id.Cancel);

		Cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refresh();
				bitmap = null;
				alrCAP = 0;
			}

		});
		return root;
	}

	public void refresh() {
		FragmentManager fragmentManager = getFragmentManager();
		Fragment fragment;
		fragment = new LoadFromGallery();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragment)
				.commit();
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			final String picturePath;
			try (Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null)) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
			}
			catch (NullPointerException ex) {
				ex.printStackTrace();
				return;
			}

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			bitmap = BitmapFactory.decodeFile(picturePath, options);
			ImageView img = (ImageView) root.findViewById(R.id.imgV);
			img.setImageBitmap(bitmap);
			alrCAP = 1;

			super.onActivityResult(requestCode, resultCode, data);
			Context context = getActivity().getApplicationContext();
			File f = new File(picturePath);
			path = picturePath;
			CharSequence text = "Picture chosen: " + picturePath;
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		if (bitmap != null) {
			savedInstanceState.putString("parcebitmap", path);
			super.onSaveInstanceState(savedInstanceState);
		}
	}

}

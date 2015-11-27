package com.fotoup;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Lucas on 04.11.2015.
 */
public class LoadFromGalery extends Fragment {

    protected View rootview;
    public static int RESULT_LOAD_IMAGE=1;
    public int alrCAP=0;

    Bitmap bitmap;

    String weg;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if(savedInstanceState!=null) {





        alrCAP =1;




        String path = savedInstanceState.getString("parcebitmap");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        bitmap =  BitmapFactory.decodeFile(path , options);


    }

}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag3, container, false);
        this.rootview = view;


        if(bitmap!=null){
            ImageView img=(ImageView) rootview.findViewById(R.id.imgV);
            img.setImageBitmap(bitmap);


        }



        Button Load=(Button) rootview.findViewById(R.id.StartImport);

        Load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(alrCAP==0) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    ImageView img = (ImageView) rootview.findViewById(R.id.imgV);
                    img.setBackground(null);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);


                }
                else {
                    Context context1 = getActivity().getApplicationContext();
                    CharSequence text1 = "Picture already choosen";
                    int duration1 = Toast.LENGTH_LONG;
                    Toast toast1 = Toast.makeText(context1, text1, duration1);
                    toast1.show();

                }
            }

        });

        Button Import=(Button) rootview.findViewById(R.id.Import);

        Import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alrCAP!=0){
                    ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                    File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);
                    File[]contents = directory.listFiles();

                    int FileAnzahl = contents.length+1;
                    saveImageToInternalStorage(bitmap, FileAnzahl);


                }
                else{

                    Context context = getActivity().getApplicationContext();
                    CharSequence text = "No picture to import!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }


            }

        });

        Button Cancel=(Button) rootview.findViewById(R.id.Cancel);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refresh();
                bitmap = null;

                alrCAP = 0;
            }

        });







        return rootview;
    }
    public void refresh(){
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment;
        fragment = new LoadFromGalery();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();




           // Drawable kk;
            //kk = new BitmapDrawable(getResources(),picturePath);
/**
            try {



                File f = new File(picturePath);
                String name = f.getName();
                File file=new File(picturePath);
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));


            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }**/

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            String pathext = picturePath;

            bitmap =  BitmapFactory.decodeFile(pathext , options);







                        ImageView img=(ImageView) rootview.findViewById(R.id.imgV);
            img.setImageBitmap(bitmap);
            alrCAP =1;

            super.onActivityResult(requestCode, resultCode, data);
            Context context = getActivity().getApplicationContext();
            File f = new File(picturePath);
            weg = picturePath;
            String name = f.getName();
            CharSequence text = "Picture choosen"+picturePath;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();



        }
    }
    protected String saveImageToInternalStorage(Bitmap bitmapImage, int Anzahl){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile"+Anzahl+".jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SmallsaveImageToInternalStorage(bitmapImage);
        return directory.getAbsolutePath();
    }


    protected File SmallsaveImageToInternalStorage(Bitmap bitmap){
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("SmallPictures", Context.MODE_PRIVATE);
        File[]contents = directory.listFiles();
        int Anzahl = contents.length+1;
        File mypath=new File(directory,"profile"+Anzahl+".jpg");

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width1 = display.getWidth();
        int width=((width1-70)/3);



                FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);


            Bitmap scaledbitmap = Bitmap.createScaledBitmap(bitmap, width, width, false);
            scaledbitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // Use the compress method on the BitMap object to write image to the OutputStream


            //fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Context context1 = getActivity().getApplicationContext();
        CharSequence text1 = "Imported!";
        int duration1 = Toast.LENGTH_LONG;
        Toast toast1 = Toast.makeText(context1, text1, duration1);
        toast1.show();

        return mypath;
    }
@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (bitmap != null) {
            savedInstanceState.putString("parcebitmap", weg);
            super.onSaveInstanceState(savedInstanceState);
        }
    }





}

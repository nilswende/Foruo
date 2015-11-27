package com.fotoup;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

/**
 * Created by Lucas on 04.11.2015.
 */
public class PicLoaded extends Activity {
    Bitmap toLoad;
    String ID;

    File uu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_load);



        Intent intent = getIntent();
        String k = intent.getStringExtra("Path");
        String C = intent.getStringExtra("ID");


        loadImageFromStorage(k, C);

        final Button leftButton = (Button) findViewById(R.id.button1);
        leftButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                String subject = "Bild";
                String text = "Siehe Anhang";
                Uri uri = Uri.fromFile(uu);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);


            }
        });

        final Button rightButton = (Button) findViewById(R.id.button);
        rightButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);


                File f = new File(directory, "profile"+ID+".jpg");
                Date lastMod = new Date(f.lastModified());
                String ff = lastMod.toString();
                String fff = ff.substring(8, 10);
                String ffff = ff.substring(4,7);
                String fffff = ff.substring(26,28);
                String ffffff = ff.substring(11,19);
                String description = "imported from FotoUp!";
                String title = "Picture nr."+ID+" from: "+fff+" "+ffff+" "+fffff+" "+ffffff;



                MediaStore.Images.Media.insertImage(getContentResolver(),toLoad, title, description);



                    Context context1 = getApplicationContext();
                    CharSequence text1 = "Pic exported!";
                    int duration1 = Toast.LENGTH_LONG;
                    Toast toast1 = Toast.makeText(context1, text1, duration1);
                    toast1.show();
                    //fos.close();


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pic_load, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


        }

        return super.onOptionsItemSelected(item);
    }

    private void loadImageFromStorage(String path, String Id)
    {


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            String pathext = path+"/profile"+Id+".jpg";

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);


        File f = new File(directory, "profile"+Id+".jpg");
        Date lastMod = new Date(f.lastModified());
        String ff = lastMod.toString();
        String fff = ff.substring(8, 10);
        String ffff = ff.substring(4,7);
        String fffff = ff.substring(26,28);
        String ffffff = ff.substring(11,19);



        uu=new File(directory,"profile"+Id+".jpg");
        ID=Id;
        Context context1 = getApplicationContext();
        CharSequence text1 ="picture date: " + fff+" "+ffff+" "+fffff+" "+ffffff;
        int duration1 = Toast.LENGTH_LONG;
        Toast toast1 = Toast.makeText(context1, text1, duration1);
        toast1.show();

            Bitmap source =  BitmapFactory.decodeFile(pathext , options);


                       ImageView img=(ImageView)findViewById(R.id.Bild);
            img.setImageBitmap(source);
        toLoad=source;




    }

}



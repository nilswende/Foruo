package com.fotoup;

import android.app.Activity;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.*;
import android.widget.*;
import java.io.File;
import java.util.Date;

public class PicLoaded extends Activity {

    private Bitmap toLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_load);

        final Intent intent = getIntent();
        final String path = intent.getStringExtra("Path");
        final String id = intent.getStringExtra("ID");

        final File imgPath = loadImageFromStorage(path, id);

        final Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final String subject = "Picture";
                final String text = "See attachment.";
                final Uri uri = Uri.fromFile(imgPath);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);
            }
        });

        final Button exportButton = (Button) findViewById(R.id.exportButton);
        exportButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final Context context = getApplicationContext();
                final ContextWrapper cw = new ContextWrapper(context);
                final File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);

                final String description = "Exported from FotoUP!";
                final String title = "Picture no. " + id + " from: " + getFormattedDate(directory, id);

                MediaStore.Images.Media.insertImage(getContentResolver(), toLoad, title, description);

                final CharSequence text = "Pic exported!";
                final int duration = Toast.LENGTH_LONG;
                final Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    private String getFormattedDate(File directory, String id) {
        final File file = new File(directory, "profile" + id + ".jpg");
        final Date lastMod = new Date(file.lastModified());
        final String s = lastMod.toString();
        StringBuilder b = new StringBuilder();
        b.append(s, 8, 10).append(" ");
        b.append(s, 4, 7).append(" ");
        b.append(s, 26, 28).append(" ");
        b.append(s, 11, 19);
        return b.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pic_load, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private File loadImageFromStorage(String path, String id) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        final String pathText = path + "/profile" + id + ".jpg";

        final Context context = getApplicationContext();
        final ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);

        final CharSequence text = "Picture date: " + getFormattedDate(directory, id);
        final int duration = Toast.LENGTH_LONG;
        final Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        toLoad = BitmapFactory.decodeFile(pathText, options);

        ImageView img = (ImageView) findViewById(R.id.pic);
        img.setImageBitmap(toLoad);

        return new File(directory, "profile" + id + ".jpg");
    }
}

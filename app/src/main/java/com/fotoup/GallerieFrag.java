package com.fotoup;


import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

public class GallerieFrag extends Fragment{
 int j;
 int width;
 int c;
 String k ;
 Bitmap bit;
    protected View rootview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("SmallPictures", Context.MODE_PRIVATE);
        File[] contents = directory.listFiles();

        View view = inflater.inflate(R.layout.frag2, container, false);
        this.rootview = view;




        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int width1 = display.getWidth();
        width=((width1-70)/3);

        if (contents.length > 0) {

            int t = contents.length;

            for (int i = 0; i < t; i++) {
                load();
            }
        }

        return rootview;
    }

    public void load() {

        TextView UnterText = new TextView(getActivity());
        TableLayout table = (TableLayout) rootview.findViewById(R.id.table);
        table.getChildCount();



        TableRow tableRow1 = (TableRow) rootview.findViewById('a' + j);
        if (table.getChildCount() == 0 || tableRow1.getChildCount() == 3) {
            TableRow tableRow = new TableRow(getActivity());
            tableRow.setMinimumHeight(width + UnterText.getHeight() + 30);
            j++;
            tableRow.setId('a'+j);
            table.addView(tableRow);
        }
        TableRow tableRow = (TableRow) rootview.findViewById('a' + j);

        int a = (table.getChildCount() - 1) * 3;
        int b = tableRow.getChildCount() + 1;
        c = a + b;


        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("SmallPictures", Context.MODE_PRIVATE);
        k = directory.getAbsolutePath();

        try{
            File f=new File(k, "profile"+c+".jpg");
            Bitmap oi = BitmapFactory.decodeStream(new FileInputStream(f));
            bit = (Bitmap) oi;}
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }


        Button button = new Button(getActivity());
        button.setId(c);
        int g =  button.getId();
        button.setMinimumWidth(width);
        button.setMaxWidth(width);
        button.setMinimumHeight(width);
        button.setMaxHeight(width);

        button.setBackground(new BitmapDrawable(bit));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);
                String kkk = directory.getAbsolutePath();
                int d = v.getId();
                String V = Integer.toString(d);
                Intent intent = new Intent(getActivity(), PicLoaded.class);
                intent.putExtra("Path", kkk);
                intent.putExtra("ID", V);
                getActivity().startActivity(intent);

            }

        });
        ContextWrapper cw1 = new ContextWrapper(getActivity().getApplicationContext());
        File directory1 = cw1.getDir("BigPictures", Context.MODE_PRIVATE);
        File f = new File(directory1, "profile"+g+".jpg");
        Date lastMod = new Date(f.lastModified());
        String ff = lastMod.toString();
        String fff = ff.substring(8, 10);
        String ffff = ff.substring(4,7);
        String fffff = ff.substring(26,28);
        String ffffff = ff.substring(11,16);
        //UnterText.setText("Picture: "+g);
        UnterText.setText("" + fff+""+ffff+""+fffff+" "+ffffff);

        LinearLayout teile = new LinearLayout(getActivity());
        teile.setOrientation(LinearLayout.VERTICAL);
        teile.addView(button);
        teile.addView(UnterText);
        teile.setPadding(0, 0, 10, 0);


        tableRow.addView(teile);





    }

}


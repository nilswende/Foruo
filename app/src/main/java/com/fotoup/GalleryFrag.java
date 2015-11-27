package com.fotoup;

import android.app.Activity;
import android.app.Fragment;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.Date;

public class GalleryFrag extends Fragment {
    int numberOfRows;
    int itemSize;
    int indexOfNewItem;
    protected View root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        File directory = cw.getDir("SmallPictures", Context.MODE_PRIVATE);
        File[] thumbnails = directory.listFiles();

        this.root = inflater.inflate(R.layout.frag2, container, false);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int displayWidth = display.getWidth();
        itemSize = ((displayWidth - 70) / 3);

        for (final File thumb : thumbnails) {
            load(thumb);
        }
        return root;
    }

    public void load(File thumb) {
        Activity act = getActivity();
        TextView subText = new TextView(act);
        TableLayout table = (TableLayout) root.findViewById(R.id.table);

        TableRow tableRow;
        if (table.getChildCount() == 0) {
            tableRow = createAndAddNewRow(act, table, subText);
        }
        else {
            tableRow = (TableRow) root.findViewById('a' + numberOfRows);
            if (tableRow.getChildCount() == 3) {
                tableRow = createAndAddNewRow(act, table, subText);
            }
        }

        int indexOfLastFullRowItem = (numberOfRows - 1) * 3;
        int indexOfNewItemInsideRow = tableRow.getChildCount() + 1;
        indexOfNewItem = indexOfLastFullRowItem + indexOfNewItemInsideRow;

        Bitmap bmp;
        try (FileInputStream in = new FileInputStream(thumb)) {
            bmp = BitmapFactory.decodeStream(in);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        addItemToRow(act, tableRow, bmp, subText);
    }

    private TableRow createAndAddNewRow(Activity act, TableLayout table, TextView subText) {
        TableRow tableRow = new TableRow(act);
        tableRow.setMinimumHeight(itemSize + subText.getHeight() + 30);
        tableRow.setId('a' + ++numberOfRows);
        table.addView(tableRow);
        return tableRow;
    }

    private void addItemToRow(final Activity act, TableRow row, Bitmap bmp, TextView subText) {
        Button imageButton = new Button(act);
        imageButton.setId(indexOfNewItem);
        imageButton.setMinimumWidth(itemSize);
        imageButton.setMaxWidth(itemSize);
        imageButton.setMinimumHeight(itemSize);
        imageButton.setMaxHeight(itemSize);

        imageButton.setBackground(new BitmapDrawable(bmp));
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextWrapper cw = new ContextWrapper(act.getApplicationContext());
                File directory = cw.getDir("BigPictures", Context.MODE_PRIVATE);
                String path = directory.getAbsolutePath();
                String viewId = Integer.toString(v.getId());
                Intent intent = new Intent(act, PicLoaded.class);
                intent.putExtra("Path", path);
                intent.putExtra("ID", viewId);
                act.startActivity(intent);

            }

        });
        setSubText(act, subText, imageButton.getId());

        LinearLayout item = new LinearLayout(act);
        item.setOrientation(LinearLayout.VERTICAL);
        item.addView(imageButton);
        item.addView(subText);
        item.setPadding(0, 0, 10, 0);

        row.addView(item);
    }

    private void setSubText(Activity act, TextView subText, int imageButtonId) {
        ContextWrapper cw1 = new ContextWrapper(act.getApplicationContext());
        File fullsizePics = cw1.getDir("BigPictures", Context.MODE_PRIVATE);
        File pic = new File(fullsizePics, "profile" + imageButtonId + ".jpg");
        Date lastModified = new Date(pic.lastModified());
        String lastModifiedString = lastModified.toString();
        String day = lastModifiedString.substring(8, 10);
        String month = lastModifiedString.substring(4, 7);
        String year = lastModifiedString.substring(26, 28);
        String time = lastModifiedString.substring(11, 16);
        subText.setText(day + month + year + " " + time);
    }
}

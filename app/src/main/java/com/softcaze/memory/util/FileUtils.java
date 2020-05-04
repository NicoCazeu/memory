package com.softcaze.memory.util;

import android.content.Context;
import android.util.Log;

import com.softcaze.memory.R;
import com.softcaze.memory.model.RowString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * Read File
     * @param context
     * @param indexFile
     * @return list of lines
     * @throws IOException
     */
    public static List<String> readFile(Context context, int indexFile, boolean isTest) throws IOException {
        List<String> lines = new ArrayList<>();
        String string = "";
        InputStream is = context.getResources().openRawResource(indexFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            lines.add(string);
        }
        is.close();

        return lines;
    }

    public static List<List<String>> readFile(Context context, int indexFile) throws IOException  {
        List<List<String>> rowString = new ArrayList<>();

        String string = "";
        List<String> levelRow = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(indexFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            if(levelRow.size() < 3) {
                levelRow.add(string);
            } else {
                levelRow = new ArrayList<String>();
                levelRow.add(string);
            }

            if(levelRow.size()==3) {
                rowString.add(levelRow);
            }
        }
        is.close();

        return rowString;
    }
}

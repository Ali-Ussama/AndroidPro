package com.example.aliosama.dramatranslation.Configurations;

import android.content.Context;
import android.widget.Toast;

import com.example.aliosama.dramatranslation.model.DownloadModel;
import com.example.aliosama.dramatranslation.model.EpisodeModel;
import com.example.aliosama.dramatranslation.model.MovieModel;
import com.example.aliosama.dramatranslation.model.SerieModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by aliosama on 11/20/2017.
 */

public class Caching {


    static boolean keep = true;
    static ArrayList<MovieModel> simpleClass = null;

    public static boolean SaveMovieObject(Context context, ArrayList<MovieModel> obj) {
        try {
            File dir = context.getFilesDir();
            File[] dirs = context.getFilesDir().listFiles();

            Toast.makeText(context,String.valueOf(dirs.length), Toast.LENGTH_LONG).show();

            final File suspend_f = new File(dir,"test");
            Toast.makeText(context,suspend_f.getPath(), Toast.LENGTH_LONG).show();

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try {
                fos = new FileOutputStream(suspend_f);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
            } catch (Exception e) {
                keep = false;
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (!keep)
                        suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return keep;
    }
    public static boolean SaveSerieObject(Context context, ArrayList<SerieModel> obj) {
        try {
            File dir = context.getFilesDir();
            File[] dirs = context.getFilesDir().listFiles();

            Toast.makeText(context,String.valueOf(dirs.length), Toast.LENGTH_LONG).show();

            final File suspend_f = new File(dir,"test");
            Toast.makeText(context,suspend_f.getPath(), Toast.LENGTH_LONG).show();

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try {
                fos = new FileOutputStream(suspend_f);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
            } catch (Exception e) {
                keep = false;
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (!keep)
                        suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return keep;
    }
    public static boolean SaveEpisodeObject(Context context, ArrayList<EpisodeModel> obj) {
        try {
            File dir = context.getFilesDir();
            File[] dirs = context.getFilesDir().listFiles();

            Toast.makeText(context,String.valueOf(dirs.length), Toast.LENGTH_LONG).show();

            final File suspend_f = new File(dir,"test");
            Toast.makeText(context,suspend_f.getPath(), Toast.LENGTH_LONG).show();

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try {
                fos = new FileOutputStream(suspend_f);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
            } catch (Exception e) {
                keep = false;
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (!keep)
                        suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return keep;
    }
    public static boolean SaveDownloadObject(Context context, ArrayList<DownloadModel> obj) {
        try {
            File dir = context.getFilesDir();
            File[] dirs = context.getFilesDir().listFiles();

            Toast.makeText(context,String.valueOf(dirs.length), Toast.LENGTH_LONG).show();

            final File suspend_f = new File(dir,"test");
            Toast.makeText(context,suspend_f.getPath(), Toast.LENGTH_LONG).show();

            FileOutputStream fos = null;
            ObjectOutputStream oos = null;

            try {
                fos = new FileOutputStream(suspend_f);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(obj);
            } catch (Exception e) {
                keep = false;
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (fos != null) fos.close();
                    if (!keep)
                        suspend_f.delete();
                } catch (Exception e) { /* do nothing */ }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return keep;
    }

    public static ArrayList<MovieModel> GetObject(Context c) {
        File dir = c.getFilesDir();
        final File suspend_f = new File(dir,"test");
        simpleClass = null;
        FileInputStream fis = null;
        ObjectInputStream is = null;

        try {
            fis = new FileInputStream(suspend_f);
            is = new ObjectInputStream(fis);

            simpleClass = (ArrayList<MovieModel>) is.readObject();
        } catch (Exception e) {
            String val = e.getMessage();
        } finally {
            try {
                if (fis != null) fis.close();
                if (is != null) is.close();
            } catch (Exception e) {
//
            }
        }
        return simpleClass;
    }
}

package com.example.peter.focus;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Peter on 2016/8/9.
 */
class DownloadImageTask extends AsyncTask<String, Integer, Bitmap>{

    ImageView imageView;
    Bitmap bitmap;

    public DownloadImageTask(ImageView imageView){
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        HttpURLConnection connection;
        final Bitmap myBitmap;

        try{
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);
            this.bitmap = myBitmap;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap){
        bitmap = this.bitmap;
        this.imageView.setImageBitmap(bitmap);
    }
}

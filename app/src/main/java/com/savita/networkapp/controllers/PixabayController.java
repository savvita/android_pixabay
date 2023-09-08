package com.savita.networkapp.controllers;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.savita.networkapp.configs.PixabayConfig;
import com.savita.networkapp.models.PixabayImage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PixabayController {
    private static List<PixabayImage> hits;
    private static final String LOG_TAG = "PixabayController_tag";

    public static List<PixabayImage> getHits() {
        return hits;
    }

    public static List<PixabayImage> getImages(String params) {
        List<PixabayImage> images = new ArrayList<>();

        params = params.replace(' ', '+');

        try {
            URL url = new URL("https://pixabay.com/api/?key=" + PixabayConfig.PIXABAY_API_KEY + "&q=" + params + "&image_type=photo");
            HttpsURLConnection myConnection = (HttpsURLConnection) url.openConnection();
            if (myConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                InputStream responseBody = myConnection.getInputStream();
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);

                jsonReader.beginObject();

                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("hits")) {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            PixabayImage image = getImage(jsonReader);
                            images.add(image);
                        }

                    } else {
                        jsonReader.skipValue();
                    }
                }

                jsonReader.close();

            } else {
                Log.d(LOG_TAG, String.valueOf(myConnection.getResponseCode()));
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Log.d(LOG_TAG, e.getMessage());
            } else {
                Log.d(LOG_TAG, "Error");
            }
        }

        hits = images;

        return images;
    }

    public static void loadImage(String url, ImageView into) {
        into.post( () -> Picasso.get()
                .load(url)
                .into(into));
    }

    private static PixabayImage getImage(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        PixabayImage image = new PixabayImage();

        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if (key.equals("webformatURL")) {
                image.setPreviewUrl(jsonReader.nextString());
            } else if(key.equals("largeImageURL")) {
                image.setLargeUrl(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        return image;
    }
}

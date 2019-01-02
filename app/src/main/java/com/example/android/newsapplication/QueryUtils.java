package com.example.android.newsapplication;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getName ();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query The Gurdian dataset and return a list of {@link News} objects.
     */
    public static List <News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl ( requestUrl );
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest ( url );
        } catch (IOException e) {
//            Log.e ( LOG_TAG, "Problem making the HTTP request.", e );
            Log.e ( LOG_TAG, String.valueOf ( R.string.httpRequest ), e );

        }
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List <News> news = extractFeatureFromJson ( jsonResponse );

        // Return the list of {@link News}s
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL ( stringUrl );
        } catch (MalformedURLException e) {
            Log.e ( LOG_TAG, String.valueOf ( R.string.urlBuilding ), e );
        }
        return url;
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given
     **/
    private static List <News> extractFeatureFromJson(String newsJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty ( newsJson )) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        List <News> news = new ArrayList <> ();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject ( newsJson );
            // Extract the JSONArray associated with the key called "response",
            // which represents a list of features (or news).
            JSONObject baseJSONResponseResult = baseJsonResponse.getJSONObject ( "response" );
            //Create a json Array for "results"
            JSONArray newsArray = baseJSONResponseResult.getJSONArray ( "results" );
            // For each news in the newsArray, create an {@link News} object
            for (int i = 0; i < newsArray.length (); i++) {

                // Get a single news at position i within the list of news
                JSONObject currentNews = newsArray.getJSONObject ( i );

                String webTitle = currentNews.getString ( "webTitle" );
                String webPubDate = currentNews.getString ( "webPublicationDate" );
                String sectionId = currentNews.getString ( "sectionId" );
                String webUrl = currentNews.getString ( "webUrl" );
                String author = "";

                //Extract the JSONArray with the key "tag"
                JSONArray authorArray = currentNews.getJSONArray ( "tags" );
                if (authorArray.length () != 0) {
                    JSONObject contributorTag = (JSONObject) authorArray.get ( 0 );
                    author = contributorTag.getString ( "webTitle" );
                }

                // Create a new {@link News} object with the title, pubDate, sectionId, url, author
                // from the JSON response.
                News newsItems = new News ( webTitle, webPubDate, sectionId, webUrl, author );

                // Add the new {@link news} to the list of news.
                news.add ( newsItems );
            }
        } catch (JSONException e) {
            Log.e ( "QueryUtils", "Problem parsing the news JSON results.", e );
        }
        return news;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setReadTimeout ( 10000 /* milliseconds */ );
            urlConnection.setConnectTimeout ( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod ( "GET" );
            urlConnection.connect ();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode () == 200) {
                inputStream = urlConnection.getInputStream ();
                jsonResponse = readFromStream ( inputStream );
            } else {
                Log.e ( LOG_TAG, "Error response code: " + urlConnection.getResponseCode () );
            }
        } catch (IOException e) {
            Log.e ( LOG_TAG, "Problem retrieving the earthquake JSON results.", e );
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect ();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close ();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder ();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader ( inputStream, Charset.forName ( "UTF-8" ) );
            BufferedReader reader = new BufferedReader ( inputStreamReader );
            String line = reader.readLine ();
            while (line != null) {
                output.append ( line );
                line = reader.readLine ();
            }
        }
        return output.toString ();
    }
}



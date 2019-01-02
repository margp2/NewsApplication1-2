package com.example.android.newsapplication;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <List <News>> {

    private static final String LOG_TAG = NewsActivity.class.getName ();
    /**
     * URL for news data from The Gurdian dataset
     */
    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?show-tags=contributor&to-date=2018-09-22&q=fees&api-key=4e30ede8-4745-408e-9324-780260b8edae";
    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of news
     */
    private NewsAdapter newsAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_news );

        // Find a reference to the {@link ListView} in the layout
        ListView newsListView = (ListView) findViewById ( R.id.list );

        mEmptyStateTextView = (TextView) findViewById ( R.id.empty_view );
        newsListView.setEmptyView ( mEmptyStateTextView );

        // Create a new adapter that takes an empty list of earthquakes as input
        newsAdapter = new NewsAdapter ( this, new ArrayList <News> () );

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter ( (ListAdapter) newsAdapter );

        newsListView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position, long id) {
                // Find the current news that was clicked on
                News currentNews = (News) ((ListAdapter) newsAdapter).getItem ( position );

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse ( currentNews.getWebUrl () );

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent ( Intent.ACTION_VIEW, newsUri );

                // Send the intent to launch a new activity
                startActivity ( websiteIntent );
            }
        } );

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService ( Context.CONNECTIVITY_SERVICE );

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo ();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected ()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager ();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader ( NEWS_LOADER_ID, null, this );
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById ( R.id.loading_indicator );
            loadingIndicator.setVisibility ( View.GONE );

            // Update empty state with no connection error message
            mEmptyStateTextView.setText ( R.string.noInternet );
        }
    }

    @Override
    public Loader <List <News>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences ( this );
        String orderBy = sharedPrefs.getString ( getString ( R.string.settings_order_by_key ), getString ( R.string.settings_order_by_default ) );
        String searchBy = sharedPrefs.getString ( getString ( R.string.settings_search_key ), getString ( R.string.settings_search_default ) );

        Uri baseUri = Uri.parse ( NEWS_REQUEST_URL );
        Uri.Builder uriBuilder = baseUri.buildUpon ();

        uriBuilder.appendQueryParameter ( getString( R.string.search_key), searchBy );

        uriBuilder.appendQueryParameter ( getString( R.string.order_key), orderBy );

        return new NewsLoader ( this, uriBuilder.toString () );
    }

    @Override
    public void onLoadFinished(Loader <List <News>> loader, List <News> news) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById ( R.id.loading_indicator );
        loadingIndicator.setVisibility ( View.GONE );

        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText ( R.string.noNews );

        // If there is a valid list of {@link New}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty ()) {
            newsAdapter.addAll ( news );
        }
    }

    @Override
    public void onLoaderReset(Loader <List <News>> loader) {
        newsAdapter.clear ();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent ( this, SettingsActivity.class );
            startActivity ( settingsIntent );
            return true;
        }
        return super.onOptionsItemSelected ( item );
    }
}

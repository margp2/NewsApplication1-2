package com.example.android.newsapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter <News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of earthquakes, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List <News> news) {
        super ( context, 0, news );
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of news.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from ( getContext () ).inflate ( R.layout.news_list_item, parent, false );
        }
        // Find the news at the given position in the list of news
        News currentNews = getItem ( position );

        // Find the TextView with view ID webTitle
        TextView webTitle = listItemView.findViewById ( R.id.webTitle );
        webTitle.setText ( currentNews.getWebTitle () );

        // Find the TextView with view ID sectionId
        TextView sectionId = listItemView.findViewById ( R.id.sectionId );
        sectionId.setText ( currentNews.getSectionId () );

        // Find the TextView with view ID webPubDate
        TextView webPubDate = listItemView.findViewById ( R.id.webPubDate );
        webPubDate.setText ( currentNews.getWebPubDate () );

        //Find the TextView with ID webUrl
        TextView webUrl = listItemView.findViewById ( R.id.webUrl );
        webUrl.setText ( currentNews.getWebUrl () );

        //Find the TextView with ID webAuthor
        TextView webAuthor = listItemView.findViewById ( R.id.author );
        webAuthor.setText ( currentNews.getAuthor () );

        //Return listItemView
        return listItemView;
    }
}

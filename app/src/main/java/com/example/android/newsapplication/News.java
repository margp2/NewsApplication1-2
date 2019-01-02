package com.example.android.newsapplication;

public class News {
    //Url of the news
    private String mWebUrl;

    //SectionId of the news
    private String mSectionId;

    //Web Title of the news
    private String mWebTitle;

    //Web publication of the news
    private String mWebPubDate;

    //Author of the news
    private String mAuthor;
    /**
     * Constructs a new {@link News} object.
     *
     * @param webTitle is the tiel of the news
     * @param webUrl is the url of the news
     * @param webTitle is the website Title to find more details about the news
     * @param webPubDate is the date when the news was published
     * @param sectionId is the section of the news happened
     * @param author is the news contributor.
     */
    public News(String webTitle, String webPubDate, String sectionId, String webUrl, String author) {
        mWebPubDate = webPubDate;
        mSectionId = sectionId;
        mWebTitle = webTitle;
        mWebUrl = webUrl;
        mAuthor = author;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebPubDate() {
        return mWebPubDate;
    }

    public String getSectionId() {
        return mSectionId;
    }

    public String getAuthor() {
        return mAuthor;
    }
}


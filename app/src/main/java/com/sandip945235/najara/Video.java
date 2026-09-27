package com.sandip945235.najara;

public class Video {
    public String title, poster, category, embedLink, downloadLink,
                  trailer, rating, print, industry, language, quality;

    public Video(String title, String poster, String category,
                 String embedLink, String downloadLink, String trailer,
                 String rating, String print, String industry,
                 String language, String quality) {
        this.title = title;
        this.poster = poster;
        this.category = category;
        this.embedLink = embedLink;
        this.downloadLink = downloadLink;
        this.trailer = trailer;
        this.rating = rating;
        this.print = print;
        this.industry = industry;
        this.language = language;
        this.quality = quality;
    }
}

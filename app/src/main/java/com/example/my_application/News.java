package com.example.my_application;

public class News {

    private String title;
    private String link;
    private String publisher;
    private String imageURL;

    News(){
        this.title = "Title not found.";
        this.link = "https://www.google.pl/search?sxsrf=ALeKk03owz2K1bE4A8aKsMm713I21xJsog%3A1587895128412&ei=WFulXrXRGMSKmwWX5rEo&q=link+not+found&oq=link+not+found&gs_lcp=CgZwc3ktYWIQAzICCAAyAggAMgIIADIECAAQHjIECAAQHjIECAAQHjIECAAQHjIECAAQHjIECAAQHjIECAAQHjoECAAQR1CrFVirFWCoFmgAcAJ4AIABaIgBaJIBAzAuMZgBAKABAaoBB2d3cy13aXo&sclient=psy-ab&ved=0ahUKEwi13cao6oXpAhVExaYKHRdzDAUQ4dUDCAw&uact=5";
        this.publisher = "Anonymous";
        this.imageURL = "https://i0.wp.com/www.dsdrums.co.uk/wp-content/uploads/2018/04/news.png?w=500";
    }

    public News(String title, String link, String publisher, String imageURL) {
        this.title = title;
        this.link = link;
        this.publisher = publisher;
        this.imageURL = imageURL;
    }

    public News(String title, String link, String imageURL) {
        this.title = title;
        this.link = link;
        this.imageURL = imageURL;
    }

    public News(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}


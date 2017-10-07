package com.example.anubhav.modern.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Anubhav on 02-10-2017.
 */
@IgnoreExtraProperties

public class CatalogItem implements Serializable {
    String catalog_imageURL;
    String details;
    String title;

    public CatalogItem() {
    }

    public CatalogItem(String catalog_imageURL, String details, String title) {
        this.catalog_imageURL = catalog_imageURL;
        this.details = details;
        this.title = title;


    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatalog_imageURL() {
        return catalog_imageURL;
    }
}

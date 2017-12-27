package com.example.anubhav.SurrajKathuriaClothing.Models;


import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Anubhav on 02-10-2017.
 */
@IgnoreExtraProperties

public class CatalogItem implements Serializable {
    String catalogimageURL;
    String details;
    String title;
    String epoch;

    public CatalogItem() {
    }

    public CatalogItem(String catalog_imageURL, String details, String title, String epoch) {
        this.catalogimageURL = catalog_imageURL;
        this.details = details;
        this.title = title;
        this.epoch = epoch;


    }

    public String getDetails() {
        return details;
    }


    public String getTitle() {
        return title;
    }


    public String getCatalogimageURL() {
        return catalogimageURL;
    }

    public String getEpoch() {
        return epoch;
    }
}

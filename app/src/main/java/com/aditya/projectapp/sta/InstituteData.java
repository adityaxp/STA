package com.aditya.projectapp.sta;

/**
 * Created by Aditya on 9/22/2020.
 */

public class InstituteData {

    private String itemName;
    private String itemDescription;
    private String itemImage;
    private String itemDomain;



//    I don't really no why it's retrieving data after not using proguard constructor but it works
//    update - But that's just stupid I wouldn't able to add a new institute write to database
    public InstituteData(String itemName, String itemDescription, String itemImage, String itemDomain) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        this.itemDomain = itemDomain;
   }

//  Update - this constructor would strip proguard constructor I guess so this would pretty much solve my problem
//  It's worked *don't touch it*
   public InstituteData(){}

    public String getItemName() {
        return itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemDomain() {
        return itemDomain;
    }
}

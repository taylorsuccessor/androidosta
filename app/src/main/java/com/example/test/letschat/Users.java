package com.example.test.letschat;

/**
 * Created by PHP2 on 11/3/2017.
 */

public class Users {

    public  String name;
    public String image;
    public String status;
public String thumb_img;


    public  Users(){

    }


    public Users(String name, String image, String status, String thumb_img) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_img = thumb_img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public void setThumb_img(String thumb_img) {
        this.thumb_img = thumb_img;
    }

    public String getThumb_img() {

        return thumb_img;
    }

}


package com.quicsolv.insurance.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

public class PhotoVO implements Serializable {
    String photo;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    String fileName;

    public String getAssociatedUniqueID() {
        return associatedUniqueID;
    }

    public void setAssociatedUniqueID(String associatedUniqueID) {
        this.associatedUniqueID = associatedUniqueID;
    }

    String associatedUniqueID;
    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    String photoID;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    String localPath;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;
}

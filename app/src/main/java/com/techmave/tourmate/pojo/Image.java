package com.techmave.tourmate.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.techmave.tourmate.utils.Constants;

@Entity(tableName = Constants.TABLE_IMAGES)
public class Image {

    @ColumnInfo(name = Constants.IMAGE_ID)
    @PrimaryKey(autoGenerate = true)
    private int imageId;
    @ColumnInfo(name = Constants.IMAGE_URI)
    private String imageUri;
    @ColumnInfo(name = Constants.IMAGE_CAPTURE_TIME)
    private String captureTime;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }
}

package org.dcode.artificialswbackend.puzzle.dto;

import java.util.List;

public class PictureUploadRequest {
    private List<PictureData> pictureData;

    public PictureUploadRequest() {
    }

    public List<PictureData> getPictureData() {
        return pictureData;
    }

    public void setPictureData(List<PictureData> pictureData) {
        this.pictureData = pictureData;
    }
}

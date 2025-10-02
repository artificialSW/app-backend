package org.dcode.artificialswbackend.puzzle.dto;

public class PictureData {
    private String imageBase64;
    private String comment;
    private String category;

    public PictureData() {
    }
    public PictureData(String imageBase64, String comment, String category) {}

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

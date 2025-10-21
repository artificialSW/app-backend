package org.dcode.artificialswbackend.archive.dto;

public class FruitResponse {
    private String imageUrl;
    private String category;
    private String message;

    public FruitResponse() {}

    public FruitResponse(String imageUrl, String category, String message) {
        this.imageUrl = imageUrl;
        this.category = category;
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

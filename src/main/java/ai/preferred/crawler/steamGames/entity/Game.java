package ai.preferred.crawler.steamGames.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Game {
    private String category;
    private String name;
    private String tags;
    private String price;
    private String discount;
    private String imageUrl;

    // all the getters
    public String getCategory() {
        return category;
    }
    public String getName() {
        return name;
    }
    public String getTags() {
        return tags;
    }
    public String getPrice() {
        return price;
    }
    public String getDiscount() {
        return discount;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    // all setters
    public void setCategory(String category) {
        this.category = category;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setTags (String tags) {
        this.tags = tags;
    }

    public void setPrice (String price) {
        this.price = price;
    }

    public void setDiscount (String discount) {
        this.discount = discount;
    }

    public void setImageUrl (String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
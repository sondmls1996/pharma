package app.pharma.com.pharma.Model.Constructor.Object;

import java.io.Serializable;
import java.util.ArrayList;

public class Pill_obj implements Serializable{
    String id="";
    String name="";
    int price=0;
    int quality=0;
    ArrayList<String> images;
    String usage="";
    String recoment="";
    String interactIn="";
    String interaction="";
    String storage="";
    int like=0;
    int comment=0;
    Double star=0.0;
    String linkShare="";
    int likeStt=0;
    boolean binding=false;

    public String getInteraction() {
        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public boolean isBinding() {
        return binding;
    }

    public void setBinding(boolean binding) {
        this.binding = binding;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getRecoment() {
        return recoment;
    }

    public void setRecoment(String recoment) {
        this.recoment = recoment;
    }

    public String getInteractIn() {
        return interactIn;
    }

    public void setInteractIn(String interactIn) {
        this.interactIn = interactIn;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getLinkShare() {
        return linkShare;
    }

    public void setLinkShare(String linkShare) {
        this.linkShare = linkShare;
    }

    public int getLikeStt() {
        return likeStt;
    }

    public void setLikeStt(int likeStt) {
        this.likeStt = likeStt;
    }
}

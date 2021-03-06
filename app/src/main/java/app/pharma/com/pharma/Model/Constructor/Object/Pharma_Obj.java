package app.pharma.com.pharma.Model.Constructor.Object;

import java.util.ArrayList;

public class Pharma_Obj {

    public String name = "";
    public String adr = "";
    public ArrayList<String> image;
    public String phone = "";
    public double lat = 0;
    public double lng = 0;
    public String id = "";
    public String linkShare = "";
    public int likeStt = 0;
    public Double star = 0.0;
    public int like = 0;
    public int comment =0;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

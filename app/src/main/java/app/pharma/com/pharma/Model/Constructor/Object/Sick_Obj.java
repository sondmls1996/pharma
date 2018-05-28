package app.pharma.com.pharma.Model.Constructor.Object;

import java.util.ArrayList;

public class Sick_Obj {
    String id;
    String name;
    int like;
    int cmt;
    int like_stt;
    String link_share;
    ArrayList<String> images;
    Double star;
    String descri;


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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getCmt() {
        return cmt;
    }

    public void setCmt(int cmt) {
        this.cmt = cmt;
    }

    public int getLike_stt() {
        return like_stt;
    }

    public void setLike_stt(int like_stt) {
        this.like_stt = like_stt;
    }

    public String getLink_share() {
        return link_share;
    }

    public void setLink_share(String link_share) {
        this.link_share = link_share;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }
}

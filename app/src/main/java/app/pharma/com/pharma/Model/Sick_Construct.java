package app.pharma.com.pharma.Model;

/**
 * Created by Vi on 3/19/2018.
 */

public class Sick_Construct {
    String id;
    String name;
    String image;
    String catalo;
    long date;
    int like;
    int cmt;
    String descri;
    public Sick_Construct() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
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

    public String getCatalo() {
        return catalo;
    }

    public void setCatalo(String catalo) {
        this.catalo = catalo;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
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
}

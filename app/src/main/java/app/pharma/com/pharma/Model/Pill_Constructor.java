package app.pharma.com.pharma.Model;

/**
 * Created by Vi on 3/18/2018.
 */

public class Pill_Constructor {
    String id;
    String name;
    String link;
    String othername;
    String htuse;
    int like;
    int cmt;
    float star;
    float price;

    public Pill_Constructor() {
    }

    public Pill_Constructor(String id, String name, String link, String othername, String htuse, int like, int cmt, float star, float price) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.othername = othername;
        this.htuse = htuse;
        this.like = like;
        this.cmt = cmt;
        this.star = star;
        this.price = price;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    public String getHtuse() {
        return htuse;
    }

    public void setHtuse(String htuse) {
        this.htuse = htuse;
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

    public float getStar() {
        return star;
    }

    public void setStar(float star) {
        this.star = star;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

package app.pharma.com.pharma.Model;

/**
 * Created by Vi on 3/19/2018.
 */

public class Sick_Construct {
    String id;
    String name;
    String link;
    String catalo;
    String date;
    int like;
    int cmt;

    public Sick_Construct() {
    }

    public Sick_Construct(String id, String name, String link, String catalo, String date, int like, int cmt) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.catalo = catalo;
        this.date = date;
        this.like = like;
        this.cmt = cmt;
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

    public String getCatalo() {
        return catalo;
    }

    public void setCatalo(String catalo) {
        this.catalo = catalo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

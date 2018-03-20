package app.pharma.com.pharma.Model;

/**
 * Created by Vi on 3/19/2018.
 */

public class Meo_Constructor {
    String id;
    String title;
    String descrep;
    String date;
    String like;
    String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescrep() {
        return descrep;
    }

    public void setDescrep(String descrep) {
        this.descrep = descrep;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Meo_Constructor() {
    }

    public Meo_Constructor(String id, String title, String descrep, String date, String like, String comment) {
        this.id = id;
        this.title = title;
        this.descrep = descrep;
        this.date = date;
        this.like = like;
        this.comment = comment;
    }
}

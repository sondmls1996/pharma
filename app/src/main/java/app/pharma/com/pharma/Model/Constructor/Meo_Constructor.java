package app.pharma.com.pharma.Model.Constructor;

/**
 * Created by Vi on 3/19/2018.
 */

public class Meo_Constructor {
    String id;
    String title;
    String descrep;
    long date;
    String like;
    String comment;
    String image;
    String link;

    public void setDate(long date) {
        this.date = date;
    }

    public long getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

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
}

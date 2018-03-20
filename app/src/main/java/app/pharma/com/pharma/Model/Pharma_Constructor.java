package app.pharma.com.pharma.Model;

/**
 * Created by Vi on 3/19/2018.
 */

public class Pharma_Constructor {
    String id;
    String link;
    String name;
    String adr;
    String distance;
    String like;
    String comment;
    String rate;

    public Pharma_Constructor() {
    }

    public Pharma_Constructor(String id, String link, String name, String adr, String distance, String like, String comment, String rate) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.adr = adr;
        this.distance = distance;
        this.like = like;
        this.comment = comment;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}

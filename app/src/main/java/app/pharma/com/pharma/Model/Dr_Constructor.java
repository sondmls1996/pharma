package app.pharma.com.pharma.Model;

/**
 * Created by Vi on 3/19/2018.
 */

public class Dr_Constructor {
    String id;
    String link;
    String name;
    String age;
    String work_year;
    String special;
    String hospital;
    String rate;
    String like;
    String comment;

    public Dr_Constructor() {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWork_year() {
        return work_year;
    }

    public void setWork_year(String work_year) {
        this.work_year = work_year;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public Dr_Constructor(String id, String link, String name, String age, String work_year, String special, String hospital, String rate, String like, String comment) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.age = age;
        this.work_year = work_year;
        this.special = special;
        this.hospital = hospital;
        this.rate = rate;
        this.like = like;
        this.comment = comment;
    }
}

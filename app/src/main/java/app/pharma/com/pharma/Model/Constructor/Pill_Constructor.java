package app.pharma.com.pharma.Model.Constructor;

/**
 * Created by Vi on 3/18/2018.
 */

public class Pill_Constructor {
    String id;
    String name;
    String image;
    String othername;
    String htuse;
    int like;
    int cmt;
    double star;
    int price;

    public Pill_Constructor() {
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

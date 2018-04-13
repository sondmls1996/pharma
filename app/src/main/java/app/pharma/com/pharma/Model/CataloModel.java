package app.pharma.com.pharma.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CataloModel extends RealmObject{
    @PrimaryKey
    String id;
    String name;


    public CataloModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package app.pharma.com.pharma.Model.Database;

import java.util.List;

import app.pharma.com.pharma.Model.CataloModel;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Catalo extends RealmObject{
    @PrimaryKey
    String type;
    RealmList<CataloModel> listCatalo;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public RealmList<CataloModel> getListCatalo() {
        return listCatalo;
    }

    public void setListCatalo(RealmList<CataloModel> listCatalo) {
        this.listCatalo = listCatalo;
    }

    public Catalo() {

    }
}

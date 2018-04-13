package app.pharma.com.pharma.Model.Database;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Vi on 4/12/2018.
 */

public class DatabaseHandle {

    private Realm realm;

    public static DatabaseHandle instance;
    private RealmList<User> userList;

    public void beginTransation(){
        realm.beginTransaction();
    }

    public void commitTransation(){
        realm.commitTransaction();
    }

    public boolean isTransation(){
        if(realm.isInTransaction()){
            return true;
        }else{
            return false;
        }
    }

    public DatabaseHandle() {
        realm = Realm.getDefaultInstance();
    }
    ///USER//

    public User getAllUserInfor(){
        realm.beginTransaction();
        User user2 = null;
        try {
            user2 = realm.where(User.class).findFirst();

        }catch (NullPointerException n){
            n.printStackTrace();
        }

        realm.commitTransaction();
        return  user2;
    }

    public void clearUserData(){
        realm.beginTransaction();
        realm.delete(User.class);
        realm.commitTransaction();
    }

    public boolean isEmpty(){
        User user = realm.where(User.class).findFirst();
        if(user==null){
            return true;
        }else{
            RealmResults<User> results = realm.where(User.class).findAll();
            if(results.size()>0){
                return false;
            }else{
                return true;
            }

        }

    }

    public void updateOrInstall(RealmObject obj){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();

    }

    //CATALO

    public Catalo getListCataloById(String type){
        realm.beginTransaction();
        Catalo catalo = null;
        try {
            catalo = realm.where(Catalo.class).equalTo("type",type).findFirst();

        }catch (NullPointerException n){
            n.printStackTrace();
        }

        realm.commitTransaction();
        return  catalo;
    }

    public void clearCataloData(){
        realm.beginTransaction();
        realm.delete(Catalo.class);
        realm.commitTransaction();
    }
    public boolean isCataloSickEmpty(){

        Catalo user = realm.where(Catalo.class).findFirst();
        if(user==null){
            return true;
        }else{
            RealmResults<Catalo> results = realm.where(Catalo.class).equalTo("type","disease").findAll();
            if(results.size()>0){
                return false;
            }else{
                return true;
            }

        }

    }
    public boolean isCataloPillIntroEmpty(){
        Catalo user = realm.where(Catalo.class).findFirst();
        if(user==null){
            return true;
        }else{
            RealmResults<Catalo> results = realm.where(Catalo.class).equalTo("type","ingredient").findAll();
            if(results.size()>0){
                return false;
            }else{
                return true;
            }

        }
    }

    public boolean isCataloPillEmpty(){
        Catalo user = realm.where(Catalo.class).findFirst();
        if(user==null){
            return true;
        }else{
            RealmResults<Catalo> results = realm.where(Catalo.class).equalTo("type","product").findAll();
            if(results.size()>0){
                return false;
            }else{
                return true;
            }

        }
    }
    public boolean isCataloEmpty(){
        Catalo user = realm.where(Catalo.class).findFirst();
        if(user==null){
            return true;
        }else{
            RealmResults<Catalo> results = realm.where(Catalo.class).findAll();
            if(results.size()>0){
                return false;
            }else{
                return true;
            }

        }

    }




}

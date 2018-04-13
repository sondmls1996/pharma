package app.pharma.com.pharma.Model.Database;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Vi on 4/12/2018.
 */

public class DatabaseHandle {

    private Realm realm;
    private User user;
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

    public User getAllUserInforArr(){
        realm.beginTransaction();
        User user = realm.where(User.class).findFirst();
        if(user == null) {
           return null;
        }
        realm.commitTransaction();
        return  user;
    }

    public void clearData(){
        realm.beginTransaction();
        realm.delete(User.class);
        realm.commitTransaction();
    }

    public boolean isEmpty(){
        User user = realm.where(User.class).findFirst();
        if(user==null){
            return true;
        }else{

            return false;
        }

    }

    public void updateOrInstall(User user){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(user);
        realm.commitTransaction();

    }



}

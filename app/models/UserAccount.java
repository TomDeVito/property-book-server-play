package models;

import com.avaje.ebean.annotation.EnumMapping;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserAccount extends Model {

    @Id
    public String name;

    @Constraints.Email
    public String email;

    @OneToMany(mappedBy = "assignedTo")
    public List<Item> items;

    public String verifiedStatus;  //TODO refactor - C - confirmed, NC- not-confirmed, WC - waiting confirmation


    public UserAccount(String name, String email, String verifiedStatus) {
        this.name = name;
        this.email = email;
        this.verifiedStatus = verifiedStatus;
    }

    public UserAccount(String name) {
        this.name = name;
    }

    public static Finder<String, UserAccount> find =
            new Finder<String, UserAccount>(String.class, UserAccount.class);


    public static List<UserAccount> getUsers() {
        return UserAccount.find.all();
    }

    public static UserAccount getUser(String name) {
        return UserAccount.find
                .byId(name);
    }

    public static List<String> getUsernames() {
        List<String> usernames = new ArrayList<String>();

        for(UserAccount user : UserAccount.find.select("name").findList()) {
            usernames.add(user.name);
        }

        return usernames;
    }

}

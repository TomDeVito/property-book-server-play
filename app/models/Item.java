package models;

import play.Logger;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


// database relationships
// http://stackoverflow.com/questions/7296846/how-to-implement-one-to-one-one-to-many-and-many-to-many-relationships-while-de

//TODO anytime an item is changed that belongs to a user turn user verified flag to false
@Entity
public class Item extends Model {

    @Constraints.Required
    @Constraints.MaxLength(value = 50, message = "max lengths")
    @Constraints.MinLength(value = 1)
    public String name;

    public String make;

    public String model;

    @Id
    @Constraints.Required
    public String serial;

    public String upc;

    @ManyToOne
    public UserAccount assignedTo;

    @OneToMany(cascade = CascadeType.PERSIST)
    public List<ItemHistory> itemHistoryList;

    private static Logger.ALogger log = play.Logger.of(Item.class);

    public Item(String name, String make, String model, String serial, String upc, UserAccount user) {
        this.name = name;
        this.make = make;
        this.model = model;
        this.serial = serial;
        this.upc = upc;
        this.assignedTo = user;
    }

    public static Model.Finder<String, Item> find = new Model.Finder<String, Item>(
            String.class, Item.class);

    public static List<Item> findAllItemsWithAssociatedUser() {
       return find
               .fetch("assignedTo", "name")
               .findList();
    }

   // public static Item updateItem()
    public static Item findItem(String serial) {
        return find.byId(serial);
    }

    public static Item saveItem(Item item) {
        item.save();
        return item;
    }

    public static boolean updateItem(Item item) {
        Item found = find.byId(item.serial);

        if(found != null) {

            String oldName = null;
            if(found.assignedTo != null){
                oldName = found.assignedTo.name.trim().toLowerCase();
            }

            String newName = item.assignedTo.name.trim().toLowerCase();

            if(oldName == null || !oldName.equals(newName)) {
                ItemHistory itemHistoryPoint = new ItemHistory (
                        item.assignedTo.name,
                        new Date(System.currentTimeMillis()),
                        item);

                item.itemHistoryList.add(itemHistoryPoint);
            }
            item.update();
            return true;

        } else {
            return false;
        }
    }

    public static boolean deleteItem(String serial) {
        Item found = find.byId(serial);

        if(found != null) {
            found.delete();
            return true;
        } else {
            return false;
        }
    }

    public static List<Item> getItemsFor(String user) {
        return Item.find
                .select("serial,name,make,model,upc")
                .fetch("assignedTo", "name")
                .where()
                .eq("assignedTo.name", user)
                .findList();
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", serial='" + serial + '\'' +
                ", upc='" + upc + '\'' +
                ", assignedTo=" + assignedTo +
                '}';
    }
}

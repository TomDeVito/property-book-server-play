package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class ItemHistory extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "item_history_id_seq")
    public long id;

    public String user;

    public Date changedDate;

    @ManyToOne
    public Item item;

    public ItemHistory (String user, Date date, Item item) {
        this.user = user;
        this.changedDate = date;
        this.item = item;
    }

    public static Model.Finder<Long, ItemHistory> find = new Model.Finder<Long, ItemHistory>(
            Long.class, ItemHistory.class);

      public static List<ItemHistory> getHistoryForItem(String serial) {
          return ItemHistory.find.select("user,changedDate")
                  .where()
                  .eq("item.serial", serial)
                  .orderBy("changedDate desc")
                  .findList();
      }
}

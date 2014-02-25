package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.text.json.JsonContext;
import com.fasterxml.jackson.databind.JsonNode;
import models.Item;
import models.ItemHistory;
import models.UserAccount;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

//TODO: Validation -update user, serial unqiue
//TODO: cascading saves, deletes, modifies, etc
//TODO: unit tests
//TODO: Find out what the standard return types are for CRUD operations
//TODO: find out

// future features
// TODO: Create service layer?

//TODO: what happens if an item gets removed
/*
http://stackoverflow.com/questions/12148124/best-practices-on-return-for-crud
create - created object. I know, I know it's typically the same object, but it's both convenient and elegant.

update - also return the same object. In some frameworks like JPA updated object can be a different instance then the one provided.

restore - obviously the object you want to restore. If you are restoring a list, consider implementing paging - taking offset/max and returning list wrapped in a Page object with some extra metadata like the total number of entries.

delete - there is nothing to return here, void.
 */
public class Items extends Controller {

    private static Logger.ALogger log = play.Logger.of(Item.class);

    private static JsonContext jsonContext = Ebean.createJsonContext();

    public static Result getItems() {

        return ok(jsonContext.toJsonString(
                Item.findAllItemsWithAssociatedUser())).as("application/json");
    }

    public static Result getItem(String serial) {
        Item resultItem = Item.findItem(serial);

        if(resultItem != null) {
            return ok(jsonContext.toJsonString(resultItem)).as("application/json");
        } else {
            return badRequest("Item with that serial number does not exist");
        }
    }

    public static Result createItem() {
        JsonNode jsonBody = request().body().asJson();
        Form<Item> itemForm = Form.form(Item.class).bind(jsonBody);

        if(itemForm.hasErrors()) {
            return badRequest(itemForm.errorsAsJson()).as("application/json");

        } else {
            Item item = itemForm.get();
            UserAccount user = null;

            if(item.assignedTo != null)  {
                user = UserAccount.getUser(item.assignedTo.name);
            }

            if(user != null) {
                item.assignedTo = user;
            }else{
                item.assignedTo = new UserAccount("nobody", "nobody@nobody.com", "NC");
            }


            Item.saveItem(item);
            return ok(jsonContext.toJsonString(item)).as("application/json");
        }
    }

    public static Result updateItem(String serial) {
        JsonNode jsonBody = request().body().asJson();
        Form<Item> itemForm = Form.form(Item.class).bind(jsonBody);

        if(itemForm.hasErrors()) {
            return badRequest(itemForm.errorsAsJson()).as("application/json");

        } else {
            Item item = itemForm.get();

            if(Item.updateItem(item)) {
                return ok("Updated name :" + item);
            } else {
                return notFound("Item with that serial number does not exist");
            }
        }
    }

    public static Result deleteItem(String serial) {
        if(Item.deleteItem(serial)) {
            return ok("Delete name:" + serial);
        } else {
            return notFound("Item with that serial number does not exist");
        }
    }


    public static Result getItemHistory(String serial) {
        log.info("getItemHistory");
        return ok(jsonContext.toJsonString(ItemHistory.getHistoryForItem(serial))).as("application/json");
    }
}


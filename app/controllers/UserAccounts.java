package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.text.json.JsonContext;
import models.Item;
import models.UserAccount;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

//TODO:  what happens if a user gets removed?
    // - want item assignedTo to be changed to null

// on update restrict : RESTRICT prevents the action from
// happening if there's any foreign keys that rely on the field that's being changed.
public class UserAccounts extends Controller {

    private static JsonContext jsonContext = Ebean.createJsonContext();

    public static Result getUsers(boolean userNamesOnly) {

        if(userNamesOnly){
           return Results.ok(Json.toJson(UserAccount.getUsernames()));
        }

        return Results.ok(jsonContext.toJsonString(UserAccount.getUsers()));
    }

    public static Result getUser(String user) {
        return Results.ok(jsonContext.toJsonString(UserAccount.getUser(user)));
    }

    public static Result getUserItems(String user) {
        return ok(jsonContext.toJsonString(Item.getItemsFor(user))).as("application/json");
    }

    public static Result handlePatchRequest(String user) {

        return ok("Hello");
    }
}

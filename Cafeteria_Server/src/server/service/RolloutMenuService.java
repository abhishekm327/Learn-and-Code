package server.service;

import org.json.JSONArray;
import server.database.DatabaseException;
import server.database.RolloutMenuDBOperation;
import server.model.Profile;
import server.model.RolloutMenu;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class RolloutMenuService {
     RolloutMenuDBOperation rolloutMenuDBOperation = new RolloutMenuDBOperation();
     ProfileService profileService = new ProfileService();
     NotificationService notificationService = new NotificationService();

     public List<RolloutMenu> viewAllRolloutItems() throws DatabaseException {
         return rolloutMenuDBOperation.fetchRolloutItems();
     }
     
     public List<RolloutMenu> viewRolloutItemsforSpecificUserProfile(String userId) throws DatabaseException {
         Profile profile = profileService.getProfileByUserId(userId);
         List<RolloutMenu> allItems = rolloutMenuDBOperation.fetchRolloutItems();
         List<RolloutMenu> filteredItems = new ArrayList<>();
         
         for (RolloutMenu item : allItems) {
             if (item.getFoodType().equals(profile.getFoodType()) &&
                 item.getFoodStyle().equals(profile.getFoodStyle()) &&
                 item.getSpiceLevel().equals(profile.getSpiceLevel()) &&
                 item.getSweet().equals(profile.getSweet())) {
                 filteredItems.add(item);
             }
         }

         return filteredItems;
    }

    public boolean addItemsToRolloutMenu(JSONArray items, String cookingDateString) throws DatabaseException {
        java.sql.Date cookingDate;
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Date parsedDate = format.parse(cookingDateString);
            cookingDate = new java.sql.Date(parsedDate.getTime());
        } catch (ParseException e) {
            throw new DatabaseException("Error occur from cooking date");
        }

        rolloutMenuDBOperation.insertRolloutMenuItems(items, cookingDate);
        String message = "New rollout menu has been generated. Please vote for your favorite items!";
        notificationService.sendNotification(message);
        return true;
    }

    public void voteForRolloutMenuItem(String foodId) throws DatabaseException {
    	rolloutMenuDBOperation.updateRolloutMenuItemVotes(foodId);
    }
}

package server.service;

import org.json.JSONArray;
import server.database.DatabaseException;
import server.database.RolloutMenuDBOperation;
import server.model.RolloutMenu;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RolloutMenuService {
     RolloutMenuDBOperation rolloutMenuDBOperation = new RolloutMenuDBOperation();
     NotificationService notificationService = new NotificationService();

    public List<RolloutMenu> fetchRolloutItems() throws DatabaseException {
        return rolloutMenuDBOperation.fetchRolloutItems();
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

package server.service;

import org.json.JSONArray;
import server.database.RolloutMenuDBOperation;
import server.database.exception.DatabaseException;
import server.model.Profile;
import server.model.RolloutMenu;
import server.model.ScoredRolloutMenu;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		List<ScoredRolloutMenu> scoredItems = new ArrayList<>();

		for (RolloutMenu item : allItems) {
			int score = calculateScoresForItems(item, profile);
			scoredItems.add(new ScoredRolloutMenu(item, score));
		}

		List<ScoredRolloutMenu> sortedScoredItems = sortScoredItems(scoredItems);

		List<RolloutMenu> sortedItems = new ArrayList<>();
		for (ScoredRolloutMenu scoredItem : sortedScoredItems) {
			sortedItems.add(scoredItem.getRolloutMenu());
		}
		return sortedItems;
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

	private int calculateScoresForItems(RolloutMenu item, Profile profile) {
		int score = 0;
		if (item.getFoodStyle().equals(profile.getFoodStyle())) {
			score++;
		}
		if (item.getFoodType().equals(profile.getFoodType())) {
			score++;
		}
		if (item.getSpiceLevel().equals(profile.getSpiceLevel())) {
			score++;
		}
		if (item.getSweet().equals(profile.getSweet())) {
			score++;
		}
		return score;
	}

	private List<ScoredRolloutMenu> sortScoredItems(List<ScoredRolloutMenu> scoredItems) {
		Collections.sort(scoredItems, new Comparator<ScoredRolloutMenu>() {
			@Override
			public int compare(ScoredRolloutMenu item1, ScoredRolloutMenu item2) {
				return Integer.compare(item2.getScore(), item1.getScore());
			}
		});
		return scoredItems;
	}
}

package server.service;

import server.database.DatabaseException;
import server.database.ProfileDBOperation;
import server.model.Profile;

public class ProfileService {
	
	ProfileDBOperation profileDBOperation = new ProfileDBOperation();
	
	 public void updateProfile(String userId, String foodType, String foodStyle, String spiceLevel, String sweet) throws DatabaseException {
	    	profileDBOperation.updateProfile(userId, foodType, foodStyle, spiceLevel, sweet);
	    }
	 
	 public Profile getProfileByUserId(String userId) throws DatabaseException {
	        return profileDBOperation.fetchProfileByUserId(userId);
	    }

}

package server.service;

import org.json.JSONObject;

import server.database.ProfileDBOperation;
import server.database.exception.DatabaseException;
import server.model.Profile;

public class ProfileService {

	ProfileDBOperation profileDBOperation = new ProfileDBOperation();

	public void updateProfile(JSONObject jsonRequest) throws DatabaseException {
		profileDBOperation.updateProfile(jsonRequest);
	}

	public Profile getProfileByUserId(String userId) throws DatabaseException {
		return profileDBOperation.fetchProfileByUserId(userId);
	}
}

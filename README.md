# Cafeteria Recommendation Engine

Table of Contents
	1) Introduction
	2) Project Structure
	3) Entry points
	4) Technologies Used

1) Introduction
The "Cafeteria Recommendation Engine" repository contains separate client and server applications. This system helps employees provide their recommendations and assists the Chef in understanding employees food preferences.

2) Project Structure

a. Client Side Project
	Cafeteria Client
		1) controller (User, Admin, Chef, Employee)
		2) enums (RoleType)
		3) utils (consoleUtils)

b. Server Side Project
	Cafeteria Server
 		1) controller(User, Admin, Chef, Employee)
 		2) database (User, Profile, FoodMenu, Feedback, Notification, RecommendedMenu, RolloutMenu)
 		3) model (User, Profile, FoodMenu, Feedback, Notification, RecommendedMenu, RolloutMenu)
 		4) service (User, Profile, FoodMenu, Feedback, Notification, RecommendedEngine, SentimentAnalysis, RolloutMenu)
 		5) enums (ActionType)
					
3) Entry points
	a. Server Side - ServerMain.java
	b. Client Side - ClientMain.java

4) Technologies Used
		a. Programming language - Java
		b. Database - MySQL


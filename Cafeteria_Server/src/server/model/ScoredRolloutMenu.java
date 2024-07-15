package server.model;

public class ScoredRolloutMenu {
	private RolloutMenu rolloutMenu;
	private int score;

	public ScoredRolloutMenu(RolloutMenu rolloutMenu, int score) {
		this.rolloutMenu = rolloutMenu;
		this.score = score;
	}

	public RolloutMenu getRolloutMenu() {
		return rolloutMenu;
	}

	public int getScore() {
		return score;
	}
}

package server.service;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class SentimentAnalysis {
	
	private static final Set<String> positiveWords = new HashSet<>();
    private static final Set<String> negativeWords = new HashSet<>();

    static {
    	
    	positiveWords.add("tasty");
    	positiveWords.add("delicious");
        positiveWords.add("good");
        positiveWords.add("best");
        positiveWords.add("great");
        positiveWords.add("happy");
        positiveWords.add("wonderful");
        positiveWords.add("excellent");
        positiveWords.add("fantastic");

        negativeWords.add("tasteless");
        negativeWords.add("bad");
        negativeWords.add("dirty");
        negativeWords.add("expensive");
        negativeWords.add("awful");
        negativeWords.add("sad");
        negativeWords.add("terrible");
        negativeWords.add("horrible");

    }

    public static String analyzeSentiment(String text) {
        int score = 0;

        StringTokenizer tokenizer = new StringTokenizer(text.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();

            if (positiveWords.contains(word)) {
                score += 1;
            } else if (negativeWords.contains(word)) {
                score -= 1;
            }
        }

        if (score > 0) {
            return "Positive";
        } else if (score < 0) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }

}

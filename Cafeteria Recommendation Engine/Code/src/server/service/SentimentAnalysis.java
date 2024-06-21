package server.service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SentimentAnalysis {

    private static final Map<String, Double> positiveWords = new HashMap<>();
    private static final Map<String, Double> negativeWords = new HashMap<>();
    private static final Map<String, Double> negationWords = new HashMap<>();

    static {       
        positiveWords.put("good", 3.5);
        positiveWords.put("happy", 4.0);
        positiveWords.put("wonderful", 5.0);
        positiveWords.put("excellent", 5.0);
        positiveWords.put("tasty", 4.0);
        positiveWords.put("delicious", 4.0);
        positiveWords.put("best", 4.0);
        positiveWords.put("great", 4.0);
        positiveWords.put("fantastic", 5.0);
        positiveWords.put("ok", 3.0);
        positiveWords.put("average", 3.0);

        negativeWords.put("bad", 2.0);
        negativeWords.put("sad", 2.0);
        negativeWords.put("terrible", 1.0);
        negativeWords.put("horrible", 1.0);
        negativeWords.put("poor", 2.0);
        negativeWords.put("tasteless", 1.0);
        negativeWords.put("dirty", 1.0);
        negativeWords.put("expensive", 2.0);
        negativeWords.put("awful", 1.0);  

        negationWords.put("not", 0.0);
        negationWords.put("never", 0.0);
        negationWords.put("no", 0.0);
    }

    public static double analyzeSentiment(String text) {
        int scoreSum = 0;
        int wordCount = 0;
        boolean negation = false;

        StringTokenizer tokenizer = new StringTokenizer(text.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();

            if (negationWords.containsKey(word)) {
                negation = !negation;
                continue;
            }

            if (positiveWords.containsKey(word)) {
                scoreSum += (negation ? -positiveWords.get(word) : positiveWords.get(word));
                negation = false;
                wordCount++;
            } else if (negativeWords.containsKey(word)) {
                scoreSum += (negation ? -negativeWords.get(word) : negativeWords.get(word));
                negation = false;
                wordCount++;
            }
        }

        if (wordCount == 0) {
            return 3.0; 
        }

        double averageScore = (double) scoreSum / wordCount;

        averageScore = Math.max(1, Math.min(5, averageScore));

        return averageScore;
    }
}
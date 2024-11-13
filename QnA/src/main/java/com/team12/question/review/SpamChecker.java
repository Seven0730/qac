package com.team12.question.review;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SpamChecker implements ContentReviewHandler {
    private static final List<String> spamKeywords = Arrays.asList("free money", "click here", "win big", "visit this site");
    private static final Pattern repeatedCharsPattern = Pattern.compile("(.)\\1{3,}");

    @Override
    public boolean handle(String content) {
        for (String keyword : spamKeywords) {
            if (content.toLowerCase().contains(keyword)) {
                return false;
            }
        }
        return !repeatedCharsPattern.matcher(content).find();
    }
}

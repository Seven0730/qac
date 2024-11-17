package com.team12.question.review;

import java.util.Arrays;
import java.util.List;

public class ProfanityFilter implements ContentReviewHandler{
    private final List<String> forbiddenWords = Arrays.asList("fuck", "bitch");

    @Override
    public boolean handle(String content) {
        for (String word : forbiddenWords) {
            if (content.toLowerCase().contains(word)) {
                return false;
            }
        }
        return true;
    }
}

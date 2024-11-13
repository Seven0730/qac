package com.team12.question.review;

import java.util.ArrayList;
import java.util.List;

public class ContentReviewChain {
    private List<ContentReviewHandler> handlers = new ArrayList<>();

    public void addHandler(ContentReviewHandler handler) {
        handlers.add(handler);
    }

    public boolean review(String content) {
        for (ContentReviewHandler handler : handlers) {
            if (!handler.handle(content)) {
                return false;
            }
        }
        return true;
    }
}

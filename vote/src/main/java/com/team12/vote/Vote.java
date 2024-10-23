package com.team12.vote;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collection = "vote")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vote {

    @Id
    private ObjectId id;

    private UUID userId;

    private UUID postId;

    private PostType postType;

    private int voteValue;  // Value of the vote: 1 for upvote, -1 for downvote
}

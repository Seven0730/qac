package com.team12.question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findByOwnerId(UUID ownerId);

    // 使用 JPA 查询注解进行模糊查询
    @Query("SELECT q FROM Question q WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT q FROM Question q WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :content, '%'))")
    List<Question> searchByContent(@Param("content") String content);
}

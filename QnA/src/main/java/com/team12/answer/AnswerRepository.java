package com.team12.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
}

package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.models.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TapcashCardRepository extends JpaRepository<TapcashCard, String> {
}

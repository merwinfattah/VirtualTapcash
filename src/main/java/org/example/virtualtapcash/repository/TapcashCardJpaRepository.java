package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.models.ExternalSystemCard;
import org.example.virtualtapcash.models.TapcashCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TapcashCardJpaRepository extends JpaRepository<TapcashCard, String> {

}

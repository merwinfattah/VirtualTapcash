package org.example.virtualtapcash.repositories;

import org.example.virtualtapcash.models.ExternalSystemCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalSystemCardJpaRepository extends JpaRepository<ExternalSystemCard, String> {
}

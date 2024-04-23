package org.example.virtualtapcash.repository;

import org.example.virtualtapcash.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
}

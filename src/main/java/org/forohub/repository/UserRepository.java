package org.forohub.repository;

import org.forohub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByCorreoElectronico(String correoElectronico);
}

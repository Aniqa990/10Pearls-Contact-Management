package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findById(String s);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}

//JpaRepository<User, Long> → User is the entity, Long is the primary key type.
//
//Optional<User> → avoids null checks (modern Java practice).
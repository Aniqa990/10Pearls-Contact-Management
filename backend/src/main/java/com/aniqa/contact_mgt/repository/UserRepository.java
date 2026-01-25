package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Override
    Optional<User> findById(String s);

}

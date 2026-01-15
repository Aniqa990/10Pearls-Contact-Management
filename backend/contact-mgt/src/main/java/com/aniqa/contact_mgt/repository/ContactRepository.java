package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    @Override
    Optional<Contact> findById(String s);
}

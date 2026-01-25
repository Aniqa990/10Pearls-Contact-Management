package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    Page<Contact> findByUserId(
            String userId,
            PageRequest pageRequest
    );

    Optional<Contact> findByIdAndUserId(
            String contactId,
            String userId
    );

    int deleteByIdAndUserId(
            String contactId,
            String userId
    );
}

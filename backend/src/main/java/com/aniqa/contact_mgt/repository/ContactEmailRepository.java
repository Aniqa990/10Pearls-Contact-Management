package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.model.ContactEmails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactEmailRepository extends JpaRepository<ContactEmails, String> {

    List<ContactEmails> findByContact(Contact contact);
}

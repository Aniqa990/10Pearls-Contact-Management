package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.model.ContactPhones;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPhoneRepository extends JpaRepository<ContactPhones, String> {

    List<ContactPhones> findByContact(Contact contact);
}

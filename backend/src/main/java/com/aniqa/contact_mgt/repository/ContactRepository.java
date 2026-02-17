package com.aniqa.contact_mgt.repository;

import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    Page<Contact> findByUserId(String userId, Pageable pageable);

    Optional<Contact> findByIdAndUserId(String contactId, String userId);

    int deleteByIdAndUserId(String contactId, String userId);

    @Query("""
   SELECT c FROM Contact c
   WHERE c.user.id = :userId
   AND (LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
""")
    Page<Contact> search(
            @Param("userId") String userId,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}

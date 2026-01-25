package com.aniqa.contact_mgt.model;

import com.aniqa.contact_mgt.model.enums.EmailType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Table(name="Contact_Emails")
public class ContactEmails {
    @UuidGenerator
    @Id
    private String id;

    private String email;

    @Enumerated(EnumType.STRING)
    private EmailType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;
}

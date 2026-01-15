package com.aniqa.contact_mgt.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT) //if for eg id by default is null, and if id is null then that object not passed to frontend
@Table(name="User")
public class User {
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true)
    private String id;
    private String first_name;
    private String Last_name;
    @Column(unique = true)
    private String email;
    private String password_hash;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}

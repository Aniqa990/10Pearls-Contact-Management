package com.aniqa.contact_mgt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class ContactDTO {
    private String first_name;
    private String last_name;
    private String title;
    private String photoUrl;

    private List<EmailDTO> emails;
    private List<PhoneDTO> phones;
}

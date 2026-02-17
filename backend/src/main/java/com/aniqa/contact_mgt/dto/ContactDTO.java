package com.aniqa.contact_mgt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ContactDTO {
    
    @NotBlank(message = "First name is required")
    private String first_name;
    
    @NotBlank(message = "Last name is required")
    private String last_name;
    
    private String title;
    private String photoUrl;

    @Valid  // Validates each email in the list
    private List<EmailDTO> emails;
    
    @Valid  // Validates each phone in the list
    private List<PhoneDTO> phones;
}


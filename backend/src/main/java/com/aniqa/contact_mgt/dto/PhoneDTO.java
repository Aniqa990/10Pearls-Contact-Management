package com.aniqa.contact_mgt.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PhoneDTO {
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9\\-\\+\\(\\)\\s]+$", message = "Phone number format is invalid")
    private String number;
    
    @NotBlank(message = "Phone type is required")
    private String type;  // WORK, HOME, PERSONAL, OTHER
}


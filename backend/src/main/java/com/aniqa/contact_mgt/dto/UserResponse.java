package com.aniqa.contact_mgt.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String token; 
    private LocalDateTime createdAt;
}


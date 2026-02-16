package com.aniqa.contact_mgt.controller;

import com.aniqa.contact_mgt.dto.ContactDTO;
import com.aniqa.contact_mgt.dto.EmailDTO;
import com.aniqa.contact_mgt.dto.PhoneDTO;
import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.model.User;
import com.aniqa.contact_mgt.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Integration Tests for ContactController

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ContactController Tests")
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Contact testContact;
    private ContactDTO testContactDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        //test user
        testUser = new User();
        testUser.setId("user123");
        testUser.setEmail("john@example.com");

        // test contact
        testContact = new Contact();
        testContact.setId("contact123");
        testContact.setFirst_name("John");
        testContact.setLast_name("Doe");
        testContact.setTitle("Engineer");
        testContact.setUser(testUser);

        // test DTO
        testContactDTO = new ContactDTO();
        testContactDTO.setFirst_name("John");
        testContactDTO.setLast_name("Doe");
        testContactDTO.setTitle("Engineer");

        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("john@work.com");
        emailDTO.setType("WORK");
        testContactDTO.setEmails(List.of(emailDTO));

        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber("555-1234");
        phoneDTO.setType("WORK");
        testContactDTO.setPhones(List.of(phoneDTO));
    }

    @Test
    @DisplayName("Should create contact successfully")
    @WithMockUser(username = "user123")
    void testCreateContactSuccess() throws Exception {
        doNothing().when(contactService).createContact(anyString(), any(ContactDTO.class));

        mockMvc.perform(post("/api/contacts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testContactDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));

        verify(contactService, times(1)).createContact(anyString(), any(ContactDTO.class));
    }

    @Test
    @DisplayName("Should get all contacts with pagination")
    @WithMockUser(username = "user123")
    void testGetAllContactsSuccess() throws Exception {
        Page<Contact> page = new PageImpl<>(List.of(testContact), PageRequest.of(0, 10), 1);
        when(contactService.getAllContactsForUser(anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/contacts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    @DisplayName("Should search contacts successfully")
    @WithMockUser(username = "user123")
    void testSearchContactsSuccess() throws Exception {
        Page<Contact> page = new PageImpl<>(List.of(testContact), PageRequest.of(0, 10), 1);
        when(contactService.searchContacts(anyString(), anyString(), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/contacts/search?keyword=John&page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(contactService, times(1)).searchContacts(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Should get single contact successfully")
    @WithMockUser(username = "user123")
    void testGetContactSuccess() throws Exception {
        when(contactService.getContact(anyString(), anyString())).thenReturn(testContact);

        mockMvc.perform(get("/api/contacts/contact123")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value("contact123"));
    }

    @Test
    @DisplayName("Should update contact successfully")
    @WithMockUser(username = "user123")
    void testUpdateContactSuccess() throws Exception {
        when(contactService.updateContact(anyString(), anyString(), any(ContactDTO.class))).thenReturn(testContactDTO);

        mockMvc.perform(put("/api/contacts/contact123")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testContactDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(contactService, times(1)).updateContact(anyString(), anyString(), any(ContactDTO.class));
    }

    @Test
    @DisplayName("Should delete contact successfully")
    @WithMockUser(username = "user123")
    void testDeleteContactSuccess() throws Exception {
        doNothing().when(contactService).deleteContact(anyString(), anyString());

        mockMvc.perform(delete("/api/contacts/contact123")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(contactService, times(1)).deleteContact(anyString(), anyString());
    }

    @Test
    @DisplayName("Should reject contact creation with missing required fields")
    @WithMockUser(username = "user123")
    void testCreateContactInvalidData() throws Exception {
        ContactDTO invalidDTO = new ContactDTO();
        invalidDTO.setLast_name("Doe");

        mockMvc.perform(post("/api/contacts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}

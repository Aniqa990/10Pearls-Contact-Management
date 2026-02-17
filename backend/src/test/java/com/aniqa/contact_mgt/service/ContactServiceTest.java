package com.aniqa.contact_mgt.service;

import com.aniqa.contact_mgt.dto.ContactDTO;
import com.aniqa.contact_mgt.dto.EmailDTO;
import com.aniqa.contact_mgt.dto.PhoneDTO;
import com.aniqa.contact_mgt.exception.ResourceNotFoundException;
import com.aniqa.contact_mgt.mapper.ContactMapper;
import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.model.User;
import com.aniqa.contact_mgt.repository.ContactRepository;
import com.aniqa.contact_mgt.repository.UserRepository;
import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//Unit Tests for ContactService

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactService Tests")
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private ContactMapper contactMapper;

    @InjectMocks
    private ContactService contactService;

    private User testUser;
    private Contact testContact;
    private ContactDTO testContactDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setEmail("john@example.com");

        testContact = new Contact();
        testContact.setId("contact123");
        testContact.setFirst_name("John");
        testContact.setLast_name("Doe");
        testContact.setUser(testUser);

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

    // create contact tests

    @Test
    @DisplayName("Should create contact successfully")
    void testCreateContactSuccess() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(contactMapper.contactDTOToContact(testContactDTO)).thenReturn(testContact);
        when(contactRepository.save(any(Contact.class))).thenReturn(testContact);

        assertDoesNotThrow(() -> contactService.createContact("user123", testContactDTO));

        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during contact creation")
    void testCreateContactUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.createContact("nonExistentUser", testContactDTO);
        });
    }

    // get contact tets

    @Test
    @DisplayName("Should retrieve all contacts for user with pagination")
    void testGetAllContactsForUserSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Contact> contacts = List.of(testContact);
        Page<Contact> page = new PageImpl<>(contacts, pageable, 1);

        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(contactRepository.findByUserId("user123", pageable)).thenReturn(page);

        Page<Contact> result = contactService.getAllContactsForUser("user123", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testContact, result.getContent().get(0));
    }

    @Test
    @DisplayName("Should throw exception when user not found during get all contacts")
    void testGetAllContactsUserNotFound() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.getAllContactsForUser("nonExistentUser", 0, 10);
        });
    }

    // search contact tests

    @Test
    @DisplayName("Should search contacts successfully")
    void testSearchContactsSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Contact> searchResults = List.of(testContact);
        Page<Contact> page = new PageImpl<>(searchResults, pageable, 1);

        when(userRepository.findById("user123")).thenReturn(Optional.of(testUser));
        when(contactRepository.search("user123", "John", pageable)).thenReturn(page);

        Page<Contact> result = contactService.searchContacts("user123", "John", 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    // get contact test

    @Test
    @DisplayName("Should get single contact successfully")
    void testGetContactSuccess() {
         
        when(contactRepository.findByIdAndUserId("contact123", "user123")).thenReturn(Optional.of(testContact));

         
        Contact result = contactService.getContact("contact123", "user123");

         
        assertNotNull(result);
        assertEquals("contact123", result.getId());
        assertEquals("John", result.getFirst_name());
    }

    @Test
    @DisplayName("Should throw exception when contact not found")
    void testGetContactNotFound() {
         
        when(contactRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.getContact("nonExistent", "user123");
        });
    }

    // update contact tets

    @Test
    @DisplayName("Should update contact successfully")
    void testUpdateContactSuccess() {
         
        when(contactRepository.findByIdAndUserId("contact123", "user123")).thenReturn(Optional.of(testContact));
        when(contactRepository.save(any(Contact.class))).thenReturn(testContact);
        when(contactMapper.contactToContactDTO(any(Contact.class))).thenReturn(testContactDTO);

         
        ContactDTO result = contactService.updateContact("contact123", "user123", testContactDTO);

         
        assertNotNull(result);
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should throw exception when contact not found during update")
    void testUpdateContactNotFound() {
         
        when(contactRepository.findByIdAndUserId(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.updateContact("nonExistent", "user123", testContactDTO);
        });
    }

    // delete contact tests

    @Test
    @DisplayName("Should delete contact successfully")
    void testDeleteContactSuccess() {
         
        when(contactRepository.deleteByIdAndUserId("contact123", "user123")).thenReturn(1);

         
        assertDoesNotThrow(() -> contactService.deleteContact("contact123", "user123"));

         
        verify(contactRepository, times(1)).deleteByIdAndUserId("contact123", "user123");
    }

    @Test
    @DisplayName("Should throw exception when contact not found during delete")
    void testDeleteContactNotFound() {
         
        when(contactRepository.deleteByIdAndUserId(anyString(), anyString())).thenReturn(0);

        assertThrows(ResourceNotFoundException.class, () -> {
            contactService.deleteContact("nonExistent", "user123");
        });
    }
}

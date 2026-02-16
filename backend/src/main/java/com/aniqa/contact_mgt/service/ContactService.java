package com.aniqa.contact_mgt.service;

import com.aniqa.contact_mgt.dto.ContactDTO;
import com.aniqa.contact_mgt.dto.EmailDTO;
import com.aniqa.contact_mgt.dto.PhoneDTO;
import com.aniqa.contact_mgt.exception.ResourceNotFoundException;
import com.aniqa.contact_mgt.mapper.ContactMapper;
import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.model.ContactEmails;
import com.aniqa.contact_mgt.model.ContactPhones;
import com.aniqa.contact_mgt.model.User;
import com.aniqa.contact_mgt.model.enums.EmailType;
import com.aniqa.contact_mgt.model.enums.PhoneType;
import com.aniqa.contact_mgt.repository.ContactRepository;
import com.aniqa.contact_mgt.repository.UserRepository;
import com.cloudinary.Cloudinary;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.aniqa.contact_mgt.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class) //to roll back on exceptions
@RequiredArgsConstructor //for dependency injection

public class ContactService {
    private final ContactRepository contactrepo;
    private final UserRepository userrepo;
    private final Cloudinary cloudinary;
    private final ContactMapper contactMapper = Mappers.getMapper(ContactMapper.class);


    public Page<Contact> getAllContactsForUser(
            String userId,
            int page,
            int size
    ) {
        log.info("Fetching contacts for user: {}, page: {}, size: {}", userId, page, size);
        
        // Verify user exists
        userrepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("first_name"));
        return contactrepo.findByUserId(userId, pageable);
    }


    public Page<Contact> searchContacts(String userId, String keyword, int page, int size) {
        log.info("Searching contacts for user: {}, keyword: {}, page: {}, size: {}", userId, keyword, page, size);
        
        // Verify user exists
        userrepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Pageable pageable = PageRequest.of(page, size);
        return contactrepo.search(userId, keyword, pageable);
    }

    public Contact getContact(String contactId, String userId) {
        log.info("Fetching contact: {} for user: {}", contactId, userId);
        
        return contactrepo.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found or access denied"));
    }


    public void createContact(String userId, ContactDTO request) {
        log.info("Creating new contact for user: {}", userId);
        
        // Verify user exists
        User user = userrepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Contact contact = contactMapper.contactDTOToContact(request);
        contact.setUser(user);

        if (request.getEmails() != null) {
            List<ContactEmails> emails = request.getEmails().stream()
                    .map(emaildto -> {
                        ContactEmails email = new ContactEmails();
                        email.setEmail(emaildto.getEmail());
                        email.setType(EmailType.valueOf(emaildto.getType()));
                        email.setContact(contact);
                        return email;
                    })
                    .toList();
            contact.setEmailAddresses(emails);
        }

        if (request.getPhones() != null) {
            List<ContactPhones> phones = request.getPhones().stream()
                    .map(phonedto -> {
                        ContactPhones phone = new ContactPhones();
                        phone.setNumber(phonedto.getNumber());
                        phone.setType(PhoneType.valueOf(phonedto.getType()));
                        phone.setContact(contact);
                        return phone;
                    })
                    .toList();
            contact.setPhoneNumbers(phones);
        }

        // // Add emails
        // if (request.getEmails() != null && !request.getEmails().isEmpty()) {
        //     List<ContactEmails> emails = new ArrayList<>();
        //     for (EmailDTO emailDTO : request.getEmails()) {
        //         ContactEmails email = new ContactEmails();
        //         email.setEmail(emailDTO.getEmail());
        //         email.setType(EmailType.valueOf(emailDTO.getType().toUpperCase()));
        //         email.setContact(contact);
        //         emails.add(email);
        //     }

        // // Add phones
        // if (request.getPhones() != null && !request.getPhones().isEmpty()) {
        //     List<ContactPhones> phones = new ArrayList<>();
        //     for (PhoneDTO phoneDTO : request.getPhones()) {
        //         ContactPhones phone = new ContactPhones();
        //         phone.setNumber(phoneDTO.getNumber());
        //         phone.setType(PhoneType.valueOf(phoneDTO.getType().toUpperCase()));
        //         phone.setContact(contact);
        //         phones.add(phone);
        //     }
        //     contact.setPhoneNumbers(phones);
        // }

        contactrepo.save(contact);
        log.info("Contact created successfully with id: {}", contact.getId());
    }


    public ContactDTO updateContact(
            String contactId,
            String userId,
            ContactDTO dto
    ) {
        log.info("Updating contact: {} for user: {}", contactId, userId);
        
        Contact c = contactrepo.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found or access denied"));

        c.setFirst_name(dto.getFirst_name());
        c.setLast_name(dto.getLast_name());
        c.setTitle(dto.getTitle());
        c.setPhotoUrl(dto.getPhotoUrl());

        if (dto.getEmails() != null) {
            List<ContactEmails> emails = dto.getEmails().stream()
                    .map(emaildto -> {
                        ContactEmails email = new ContactEmails();
                        email.setEmail(emaildto.getEmail());
                        email.setType(EmailType.valueOf(emaildto.getType()));
                        email.setContact(c);
                        return email;
                    })
                    .toList();
            c.setEmailAddresses(emails);
        }

        // c.getEmailAddresses().clear();
        //     List<ContactEmails> emails = new ArrayList<>();
        //     for (EmailDTO emailDTO : dto.getEmails()) {
        //         ContactEmails email = new ContactEmails();
        //         email.setEmail(emailDTO.getEmail());
        //         email.setType(EmailType.valueOf(emailDTO.getType().toUpperCase()));
        //         email.setContact(c);
        //         emails.add(email);
        //     }

        // PHONES (optional)
        if (dto.getPhones() != null) {            List<ContactPhones> phones = dto.getPhones().stream()
                    .map(phonedto -> {
                        ContactPhones phone = new ContactPhones();
                        phone.setNumber(phonedto.getNumber());
                        phone.setType(PhoneType.valueOf(phonedto.getType()));
                        phone.setContact(c);
                        return phone;
                    })
                    .toList();
            
                    // Update phones
        // if (dto.getPhones() != null) {
        //     c.getPhoneNumbers().clear();
        //     List<ContactPhones> phones = new ArrayList<>();
        //     for (PhoneDTO phoneDTO : dto.getPhones()) {
        //         ContactPhones phone = new ContactPhones();
        //         phone.setNumber(phoneDTO.getNumber());
        //         phone.setType(PhoneType.valueOf(phoneDTO.getType().toUpperCase()));
        //         phone.setContact(c);
        //         phones.add(phone);
        //     }
             c.setPhoneNumbers(phones);
         }

        //return contactMapper.contactToContactDTO(contactrepo.save(c));
        Contact updated = contactrepo.save(c);
        log.info("Contact updated successfully with id: {}", updated.getId());
        
        return contactMapper.contactToContactDTO(updated);
    }


    public void deleteContact(String contactId, String userId) {
        log.info("Deleting contact: {} for user: {}", contactId, userId);
        
        int deleted = contactrepo.deleteByIdAndUserId(contactId, userId);
        if (deleted == 0) {
            throw new ResourceNotFoundException("Contact not found or access denied");
        }
        
        log.info("Contact deleted successfully");
    }

    public String uploadPhoto(String contactId, String userId, MultipartFile file) {
        log.info("Uploading photo for contact: {} for user: {}", contactId, userId);
        
        Contact contact = contactrepo.findByIdAndUserId(contactId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found or access denied"));

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "contacts")
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            contact.setPhotoUrl(imageUrl);
            contactrepo.save(contact);

            log.info("Photo uploaded successfully for contact: {}", contactId);
            return imageUrl;

        } catch (Exception e) {
            log.error("Image upload failed: ", e);
            throw new RuntimeException("Image upload failed: " + e.getMessage());
        }
    }


//    public String uploadPhoto(String contactId, String userId, MultipartFile file) {
//        log.info("Saving picture for contact ID: {}", contactId);
//        Contact contact = getContact(contactId, userId);
//
//        String photoUrl = photoFunction.apply(contactId, file);
//        contact.setPhotoUrl(photoUrl);
//
//        contactrepo.save(contact);
//        return photoUrl;
//    }
//
//
//    //this function is for extracting the file extension type
//    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
//            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");
//
//    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
//        String filename = id + fileExtension.apply(image.getOriginalFilename());
//        try {
//            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize(); //get location where we can store file
//            if(!Files.exists(fileStorageLocation)) { Files.createDirectories(fileStorageLocation); }
//            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
//            return ServletUriComponentsBuilder
//                    .fromCurrentContextPath()
//                    .path("/contacts/image/" + filename).toUriString();
//        }catch (Exception exception) {
//            throw new RuntimeException("Unable to save image");
//        }
//    };
}


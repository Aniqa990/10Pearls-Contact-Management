package com.aniqa.contact_mgt.controller;

import com.aniqa.contact_mgt.dto.ContactDTO;
import com.aniqa.contact_mgt.model.Contact;
import com.aniqa.contact_mgt.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.aniqa.contact_mgt.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;


@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestParam String userId,
            @RequestBody ContactDTO dto
    ) {
        contactService.createContact(userId, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<Contact>> getContacts(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(contactService.getAllContactsForUser(userId, page, size));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Contact>> search(
            @RequestParam String userId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                contactService.searchContacts(userId, keyword, page, size)
        );
    }

    @GetMapping("/{userId}/{contactId}")
    public ResponseEntity<Contact> getContact(@PathVariable String userId, @PathVariable String contactId) {
        return ResponseEntity.ok().body(contactService.getContact(contactId, userId));
    }

    @PutMapping("/{contactId}")
    public ResponseEntity<ContactDTO> update(
            @PathVariable String contactId,
            @RequestParam String userId,
            @RequestBody ContactDTO dto
    ) {
        return ResponseEntity.ok(
                contactService.updateContact(contactId, userId, dto)
        );
    }

    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> delete(
            @PathVariable String contactId,
            @RequestParam String userId
    ) {
        contactService.deleteContact(contactId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{contactId}/photo")
    public ResponseEntity<String> uploadPhoto(
            @PathVariable String contactId,
            @RequestParam String userId,
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                contactService.uploadPhoto(contactId, userId, file)
        );
    }

//    @PutMapping("/photo")
//    public ResponseEntity<String> uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile file) {
//        return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
//    }
//
//
//
//    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
//    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
//        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
//    }
}
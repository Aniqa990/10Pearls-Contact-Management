package com.aniqa.contact_mgt.mapper;

import com.aniqa.contact_mgt.dto.ContactDTO;
import com.aniqa.contact_mgt.model.Contact;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactMapper {
    ContactDTO contactToContactDTO (Contact contact);
}

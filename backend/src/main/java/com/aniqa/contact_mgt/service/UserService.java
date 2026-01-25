package com.aniqa.contact_mgt.service;

import com.aniqa.contact_mgt.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class) //to roll back on exceptions
@RequiredArgsConstructor //for dependency injection

public class UserService {
    private final UserRepository userrepo;

}

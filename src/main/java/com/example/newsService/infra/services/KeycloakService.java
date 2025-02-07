package com.example.newsService.infra.services;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class KeycloakService {

    private static final String REALM_NAME = "eselpo";

    private final Keycloak keycloak;
    Logger logger = LoggerFactory.getLogger(KeycloakService.class);

    public String getUserIdByUsername(String username) {
        List<UserRepresentation> users = keycloak.realm(REALM_NAME)
            .users()
            .searchByUsername(username, true);

        if (users.isEmpty()) {
            logger.error("User with username '{}' not found", username);
            return null;
        }
        return users.get(0).getId();
    }
}
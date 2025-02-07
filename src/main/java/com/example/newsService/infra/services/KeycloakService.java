package com.example.newsService.infra.services;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class KeycloakService {

    private static final String REALM_NAME = "eselpo";

    @Autowired
    private Keycloak keycloak;
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
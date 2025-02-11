package com.example.newsService.unit.services.external;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;

import com.example.newsService.infra.services.KeycloakService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KeycloakServiceTest {

    @InjectMocks
    private KeycloakService keycloakService;

    @Mock
    private Keycloak keycloak;

    @Mock
    private Logger logger;

    @Mock
    private org.keycloak.admin.client.resource.UsersResource usersResource;

    @Mock
    private UserRepresentation userRepresentation;

    @Test
    public void testGetUserIdByUsername_UserFound() {
        // Arrange
        String username = "testuser";
        String userId = "12345";
        when(keycloak.realm("eselpo")).thenReturn(mock(org.keycloak.admin.client.resource.RealmResource.class));
        when(keycloak.realm("eselpo").users()).thenReturn(usersResource);
        when(usersResource.searchByUsername(username, true)).thenReturn(List.of(userRepresentation));
        when(userRepresentation.getId()).thenReturn(userId);

        // Act
        String result = keycloakService.getUserIdByUsername(username);

        // Assert
        assertEquals(userId, result);

    }

    @Test
    public void testGetUserIdByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(keycloak.realm("eselpo")).thenReturn(mock(org.keycloak.admin.client.resource.RealmResource.class));
        when(keycloak.realm("eselpo").users()).thenReturn(usersResource);
        when(usersResource.searchByUsername(username, true)).thenReturn(Collections.emptyList());

        // Act
        String result = keycloakService.getUserIdByUsername(username);

        // Assert
        assertNull(result);
        verify(logger).error("User with username '{}' not found", username);
    }
}

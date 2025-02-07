package com.example.newsService;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class NewsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsServiceApplication.class, args);
	}
	@Value("${keycloak.username}")
	private String username;
	@Value("${keycloak.password}")
	private String password;
	@Value("${keycloak.server.url}")
	private String keycloakUrl;
	@Bean
	public Keycloak keycloak() {
		return KeycloakBuilder.builder()
		.serverUrl(keycloakUrl)
		.realm("master")
		.clientId("admin-cli")
		.grantType(OAuth2Constants.PASSWORD)
		.username(username)
		.password(password)
		.build();
	}
}

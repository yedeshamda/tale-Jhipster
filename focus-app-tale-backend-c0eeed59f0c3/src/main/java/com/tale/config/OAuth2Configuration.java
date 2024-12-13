package com.tale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.time.Duration;
import java.util.List;

@Configuration
public class OAuth2Configuration {

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository
    ) {
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
            clientRegistrationRepository,
            authorizedClientRepository
        );

        authorizedClientManager.setAuthorizedClientProvider(
            OAuth2AuthorizedClientProviderBuilder
                .builder()
                .authorizationCode()
                .refreshToken(builder -> builder.clockSkew(Duration.ofMinutes(1)))
                .clientCredentials()
                .build()
        );

        return authorizedClientManager;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration clientRegistration = ClientRegistration
            .withRegistrationId("auth0")
            .clientId("P5ePbyjmq6xLc09ZxshMaqkwcNmVbKNX")
            .clientSecret("Z_-IVcH2g8uu1Euz63EbDtT6ZHlJ5P681-3wOwQHMyq-M_X9WuHqZzihKFX_hmt1")
            .scope("openid", "profile", "email")
            .redirectUri("http://localhost:8080/login/oauth2/code/auth0")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationUri("https://dev-8ja5z27gacw183vf.eu.auth0.com/authorize")
            .tokenUri("https://dev-8ja5z27gacw183vf.eu.auth0.com/oauth/token")
            .userInfoUri("https://dev-8ja5z27gacw183vf.eu.auth0.com/userinfo")
            .jwkSetUri("https://dev-8ja5z27gacw183vf.eu.auth0.com/.well-known/jwks.json")
            .build();

        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}

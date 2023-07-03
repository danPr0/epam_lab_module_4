package com.epam.esm.security.oauth2;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.ProviderName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;

    @Autowired
    public CustomOidcUserService(UserService userService) {

        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);
        processOAuth2User(oidcUser, ProviderName.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()));

        return oidcUser;
    }

    private void processOAuth2User(OidcUser oidcUser, ProviderName providerName) {

        if (oidcUser.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("400"),
                    String.format("Email not found from OAuth2 %s provider", providerName.name()));
        }

        Optional<UserDTO> user = userService.getUser(oidcUser.getEmail());
        if (user.isEmpty()) {
            registerNewUser(oidcUser, providerName);
        } else if (user.get().getProvider().equals(providerName)) {
            updateExistingUser(oidcUser);
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("400"),
                    String.format("You're signed up with %s provider. Please use the same provider to sign in.",
                            user.get().getProvider()));
        }
    }

    private void registerNewUser(OidcUser oidcUser, ProviderName providerName) {

        UserDTO user = UserDTO.builder().email(oidcUser.getEmail()).firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName()).build();
        userService.registerUser(user, null, providerName);
    }

    private void updateExistingUser(OidcUser oidcUser) {

        UserDTO user = UserDTO.builder().email(oidcUser.getEmail()).firstName(oidcUser.getGivenName())
                .lastName(oidcUser.getFamilyName()).build();
        userService.updateUser(user);
    }
}

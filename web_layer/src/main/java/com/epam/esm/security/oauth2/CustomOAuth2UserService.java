package com.epam.esm.security.oauth2;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.security.oauth2.user_info.OAuth2UserInfo;
import com.epam.esm.security.oauth2.user_info.OAuth2UserInfoFactory;
import com.epam.esm.service.UserService;
import com.epam.esm.util_service.ProviderName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Autowired
    public CustomOAuth2UserService(UserService userService) {

        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        processOAuth2User(oAuth2User, ProviderName.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase()));

        return oAuth2User;
    }

    private void processOAuth2User(OAuth2User oAuth2User, ProviderName providerName) {

        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfoFactory().getOAuth2UserInfo(oAuth2User, providerName);

        if (oAuth2UserInfo.getEmail() == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("400"), String.format(
                    "Email not found from OAuth2 %s provider. (If you're using gitHub provider, please make your email public.)",
                    providerName.name()));
        }

        Optional<UserDTO> user = userService.getUser(oAuth2UserInfo.getEmail());
        if (user.isEmpty()) {
            registerNewUser(oAuth2UserInfo, providerName);
        } else if (user.get().getProvider().equals(providerName)) {
            updateExistingUser(oAuth2UserInfo);
        } else {
            throw new OAuth2AuthenticationException(new OAuth2Error("400"),
                    String.format("You're signed up with %s provider. Please use the same provider to sign in.",
                            user.get().getProvider()));
        }
    }

    private void registerNewUser(OAuth2UserInfo oAuth2UserInfo, ProviderName providerName) {

        UserDTO user = UserDTO.builder().email(oAuth2UserInfo.getEmail()).username(oAuth2UserInfo.getUsername())
                .firstName(oAuth2UserInfo.getFirstName()).lastName(oAuth2UserInfo.getLastName()).build();
        userService.registerUser(user, null, providerName);
    }

    private void updateExistingUser(OAuth2UserInfo oAuth2UserInfo) {

        UserDTO user = UserDTO.builder().email(oAuth2UserInfo.getEmail()).username(oAuth2UserInfo.getUsername())
                .firstName(oAuth2UserInfo.getFirstName()).lastName(oAuth2UserInfo.getLastName()).build();
        userService.updateUser(user);
    }
}

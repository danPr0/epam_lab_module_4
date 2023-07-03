package com.epam.esm.security.oauth2.user_info;

import com.epam.esm.util_service.ProviderName;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserInfoFactory {

    public OAuth2UserInfo getOAuth2UserInfo(OAuth2User oAuth2User, ProviderName providerName) {

        return switch (providerName) {
            case GOOGLE -> new GoogleOAuth2UserInfo(oAuth2User);
            case GITHUB -> new GitHubOAuth2UserInfo(oAuth2User);
            case LOCAL -> throw new ProviderNotFoundException("Local provider is not OAuth2 provider.");
        };
    }
}

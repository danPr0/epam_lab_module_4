package com.epam.esm.security.oauth2.user_info;

import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public abstract class OAuth2UserInfo {

    protected OAuth2User oAuth2User;

    public OAuth2UserInfo(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    public abstract String getEmail();

    public abstract String getUsername();

    public abstract String getFirstName();

    public abstract String getLastName();
}

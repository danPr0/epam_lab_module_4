package com.epam.esm.security.oauth2.user_info;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(OAuth2User oAuth2User) {

        super(oAuth2User);
    }

    @Override
    public String getEmail() {

        return oAuth2User.getAttribute("email");
    }

    @Override
    public String getUsername() {

        return null;
    }

    @Override
    public String getFirstName() {

        return oAuth2User.getAttribute("given_name");
    }

    @Override
    public String getLastName() {

        return oAuth2User.getAttribute("family_name");
    }
}

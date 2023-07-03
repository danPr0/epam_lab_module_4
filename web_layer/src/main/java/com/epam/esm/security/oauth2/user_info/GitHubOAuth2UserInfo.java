package com.epam.esm.security.oauth2.user_info;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class GitHubOAuth2UserInfo extends OAuth2UserInfo {

    public GitHubOAuth2UserInfo(OAuth2User oAuth2User) {

        super(oAuth2User);
    }

    @Override
    public String getEmail() {

        return oAuth2User.getAttribute("email");
    }

    @Override
    public String getUsername() {

        return oAuth2User.getAttribute("login");
    }

    @Override
    public String getFirstName() {

        return null;
    }

    @Override
    public String getLastName() {

        return null;
    }
}

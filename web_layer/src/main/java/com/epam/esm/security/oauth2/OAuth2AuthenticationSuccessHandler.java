package com.epam.esm.security.oauth2;

import com.epam.esm.security.AuthTokenProvider;
import com.epam.esm.security.oauth2.user_info.OAuth2UserInfo;
import com.epam.esm.security.oauth2.user_info.OAuth2UserInfoFactory;
import com.epam.esm.util_service.ProviderName;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider authTokenProvider;

    @Autowired
    OAuth2AuthenticationSuccessHandler(AuthTokenProvider authTokenProvider) {

        this.authTokenProvider = authTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        OAuth2AuthenticationToken oAuth2Token = ((OAuth2AuthenticationToken) authentication);
        OAuth2UserInfo oAuth2UserInfo = new OAuth2UserInfoFactory().getOAuth2UserInfo(oAuth2Token.getPrincipal(),
                ProviderName.valueOf((oAuth2Token.getAuthorizedClientRegistrationId().toUpperCase())));

        String accessToken  = authTokenProvider.generateAccessToken(oAuth2UserInfo.getEmail());
        String refreshToken = authTokenProvider.generateRefreshToken(oAuth2UserInfo.getEmail());

        authTokenProvider.setAccessTokenCookie(accessToken, response);
        authTokenProvider.setRefreshTokenCookie(refreshToken, response);
    }
}

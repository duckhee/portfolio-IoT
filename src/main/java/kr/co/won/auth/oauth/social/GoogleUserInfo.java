package kr.co.won.auth.oauth.social;

import kr.co.won.auth.oauth.OauthUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/**
 * Google User Oauth Information
 */
public class GoogleUserInfo extends OauthUser {

    public GoogleUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getUserId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getUserName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getUserEmail() {
        return (String) attributes.get("email");
    }
}

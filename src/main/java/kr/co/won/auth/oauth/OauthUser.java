package kr.co.won.auth.oauth;

import java.util.Map;

public abstract class OauthUser {

    protected Map<String , Object> attributes;

    public OauthUser(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes(){
        return this.attributes;
    }

    public abstract String getUserId();

    public abstract String getUserName();

    public abstract String getUserEmail();

}

package org.inmediart.api.configuration;

import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

public class InmediartBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    public static String REALM="INMEDIART_REST_API";

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(REALM);
        super.afterPropertiesSet();
    }
}

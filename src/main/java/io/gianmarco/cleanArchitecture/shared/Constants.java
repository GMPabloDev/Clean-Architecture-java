package io.gianmarco.cleanArchitecture.shared;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.refresh.key}")
    private String jwtRefreshKey;

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public String getJwtRefreshKey() {
        return jwtRefreshKey;
    }
}

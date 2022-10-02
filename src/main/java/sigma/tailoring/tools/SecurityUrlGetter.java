package sigma.tailoring.tools;

import org.springframework.stereotype.Component;

@Component
public class SecurityUrlGetter {
    private final String[] securityUrl;

    public SecurityUrlGetter() {
        this.securityUrl = buildMap();
    }

    public String[] getUrlWithoutAuthority() {
        return this.securityUrl;
    }

    private String[] buildMap() {
        return new String[]{
                "/",
                "/templates/show",
                "/authorization/login",
                "/authorization/sign-up",
                "/authorization/check-code",
                "/authorization/number",
                "/authorization/confirm-code",
                "/templates/public"
        };
    }
}

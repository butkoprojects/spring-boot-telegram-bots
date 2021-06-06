package io.github.butkoprojects.bots;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties("telegram")
@Data
class TelegramProperties {
    private String externalUrl;
    private String internalUrl;
    private String keyStore;
    private String keyStorePassword;
    private String pathToCertificate;

    /**
     *
     * @return
     */
    boolean hasKeyStore() {
        return !StringUtils.isEmpty(keyStore) && !StringUtils.isEmpty(keyStorePassword);
    }

    /**
     *
     * @return
     */
    boolean hasInternalUrl() {
        return StringUtils.hasText(internalUrl);
    }

    /**
     *
     * @return
     */
    public boolean hasPathToCertificate() {
        return StringUtils.hasText(pathToCertificate);
    }

    /**
     *
     * @return
     */
    public boolean hasExternalUrl() {
        return StringUtils.hasText(externalUrl);
    }
}

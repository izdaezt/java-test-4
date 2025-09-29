package com.namnguyen.javatest4.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.namnguyen.javatest4.domain.AppConfig} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppConfigDTO implements Serializable {

    private Long id;

    private String key;

    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppConfigDTO)) {
            return false;
        }

        AppConfigDTO appConfigDTO = (AppConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppConfigDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}

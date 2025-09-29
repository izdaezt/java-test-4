package com.namnguyen.javatest4.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A AppConfig.
 */
@Entity
@Table(name = "app_config")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppConfig id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public AppConfig key(String key) {
        this.setKey(key);
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public AppConfig value(String value) {
        this.setValue(value);
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppConfig)) {
            return false;
        }
        return getId() != null && getId().equals(((AppConfig) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppConfig{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}

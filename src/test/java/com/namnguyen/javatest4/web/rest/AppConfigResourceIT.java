package com.namnguyen.javatest4.web.rest;

import static com.namnguyen.javatest4.domain.AppConfigAsserts.*;
import static com.namnguyen.javatest4.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namnguyen.javatest4.IntegrationTest;
import com.namnguyen.javatest4.domain.AppConfig;
import com.namnguyen.javatest4.repository.AppConfigRepository;
import com.namnguyen.javatest4.service.dto.AppConfigDTO;
import com.namnguyen.javatest4.service.mapper.AppConfigMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AppConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppConfigResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private AppConfigMapper appConfigMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppConfigMockMvc;

    private AppConfig appConfig;

    private AppConfig insertedAppConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppConfig createEntity() {
        return new AppConfig().key(DEFAULT_KEY).value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppConfig createUpdatedEntity() {
        return new AppConfig().key(UPDATED_KEY).value(UPDATED_VALUE);
    }

    @BeforeEach
    void initTest() {
        appConfig = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAppConfig != null) {
            appConfigRepository.delete(insertedAppConfig);
            insertedAppConfig = null;
        }
    }

    @Test
    @Transactional
    void createAppConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);
        var returnedAppConfigDTO = om.readValue(
            restAppConfigMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppConfigDTO.class
        );

        // Validate the AppConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAppConfig = appConfigMapper.toEntity(returnedAppConfigDTO);
        assertAppConfigUpdatableFieldsEquals(returnedAppConfig, getPersistedAppConfig(returnedAppConfig));

        insertedAppConfig = returnedAppConfig;
    }

    @Test
    @Transactional
    void createAppConfigWithExistingId() throws Exception {
        // Create the AppConfig with an existing ID
        appConfig.setId(1L);
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppConfigs() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        // Get all the appConfigList
        restAppConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getAppConfig() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        // Get the appConfig
        restAppConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, appConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingAppConfig() throws Exception {
        // Get the appConfig
        restAppConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppConfig() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appConfig
        AppConfig updatedAppConfig = appConfigRepository.findById(appConfig.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppConfig are not directly saved in db
        em.detach(updatedAppConfig);
        updatedAppConfig.key(UPDATED_KEY).value(UPDATED_VALUE);
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(updatedAppConfig);

        restAppConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppConfigToMatchAllProperties(updatedAppConfig);
    }

    @Test
    @Transactional
    void putNonExistingAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, appConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppConfigWithPatch() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appConfig using partial update
        AppConfig partialUpdatedAppConfig = new AppConfig();
        partialUpdatedAppConfig.setId(appConfig.getId());

        partialUpdatedAppConfig.key(UPDATED_KEY).value(UPDATED_VALUE);

        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppConfig))
            )
            .andExpect(status().isOk());

        // Validate the AppConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAppConfig, appConfig),
            getPersistedAppConfig(appConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateAppConfigWithPatch() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appConfig using partial update
        AppConfig partialUpdatedAppConfig = new AppConfig();
        partialUpdatedAppConfig.setId(appConfig.getId());

        partialUpdatedAppConfig.key(UPDATED_KEY).value(UPDATED_VALUE);

        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppConfig))
            )
            .andExpect(status().isOk());

        // Validate the AppConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppConfigUpdatableFieldsEquals(partialUpdatedAppConfig, getPersistedAppConfig(partialUpdatedAppConfig));
    }

    @Test
    @Transactional
    void patchNonExistingAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appConfig.setId(longCount.incrementAndGet());

        // Create the AppConfig
        AppConfigDTO appConfigDTO = appConfigMapper.toDto(appConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppConfigMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppConfig() throws Exception {
        // Initialize the database
        insertedAppConfig = appConfigRepository.saveAndFlush(appConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appConfig
        restAppConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, appConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appConfigRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AppConfig getPersistedAppConfig(AppConfig appConfig) {
        return appConfigRepository.findById(appConfig.getId()).orElseThrow();
    }

    protected void assertPersistedAppConfigToMatchAllProperties(AppConfig expectedAppConfig) {
        assertAppConfigAllPropertiesEquals(expectedAppConfig, getPersistedAppConfig(expectedAppConfig));
    }

    protected void assertPersistedAppConfigToMatchUpdatableProperties(AppConfig expectedAppConfig) {
        assertAppConfigAllUpdatablePropertiesEquals(expectedAppConfig, getPersistedAppConfig(expectedAppConfig));
    }
}

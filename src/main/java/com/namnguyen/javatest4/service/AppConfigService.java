package com.namnguyen.javatest4.service;

import com.namnguyen.javatest4.service.dto.AppConfigDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.namnguyen.javatest4.domain.AppConfig}.
 */
public interface AppConfigService {
    /**
     * Save a appConfig.
     *
     * @param appConfigDTO the entity to save.
     * @return the persisted entity.
     */
    AppConfigDTO save(AppConfigDTO appConfigDTO);

    /**
     * Updates a appConfig.
     *
     * @param appConfigDTO the entity to update.
     * @return the persisted entity.
     */
    AppConfigDTO update(AppConfigDTO appConfigDTO);

    /**
     * Partially updates a appConfig.
     *
     * @param appConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AppConfigDTO> partialUpdate(AppConfigDTO appConfigDTO);

    /**
     * Get all the appConfigs.
     *
     * @return the list of entities.
     */
    List<AppConfigDTO> findAll();

    /**
     * Get the "id" appConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AppConfigDTO> findOne(Long id);

    /**
     * Delete the "id" appConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

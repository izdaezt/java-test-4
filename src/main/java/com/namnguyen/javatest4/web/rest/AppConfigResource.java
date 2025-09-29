package com.namnguyen.javatest4.web.rest;

import com.namnguyen.javatest4.repository.AppConfigRepository;
import com.namnguyen.javatest4.service.AppConfigService;
import com.namnguyen.javatest4.service.dto.AppConfigDTO;
import com.namnguyen.javatest4.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.namnguyen.javatest4.domain.AppConfig}.
 */
@RestController
@RequestMapping("/api/app-configs")
public class AppConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppConfigResource.class);

    private static final String ENTITY_NAME = "appConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppConfigService appConfigService;

    private final AppConfigRepository appConfigRepository;

    public AppConfigResource(AppConfigService appConfigService, AppConfigRepository appConfigRepository) {
        this.appConfigService = appConfigService;
        this.appConfigRepository = appConfigRepository;
    }

    /**
     * {@code POST  /app-configs} : Create a new appConfig.
     *
     * @param appConfigDTO the appConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appConfigDTO, or with status {@code 400 (Bad Request)} if the appConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppConfigDTO> createAppConfig(@RequestBody AppConfigDTO appConfigDTO) throws URISyntaxException {
        LOG.debug("REST request to save AppConfig : {}", appConfigDTO);
        if (appConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new appConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appConfigDTO = appConfigService.save(appConfigDTO);
        return ResponseEntity.created(new URI("/api/app-configs/" + appConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appConfigDTO.getId().toString()))
            .body(appConfigDTO);
    }

    /**
     * {@code PUT  /app-configs/:id} : Updates an existing appConfig.
     *
     * @param id the id of the appConfigDTO to save.
     * @param appConfigDTO the appConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appConfigDTO,
     * or with status {@code 400 (Bad Request)} if the appConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppConfigDTO> updateAppConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppConfigDTO appConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppConfig : {}, {}", id, appConfigDTO);
        if (appConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appConfigDTO = appConfigService.update(appConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appConfigDTO.getId().toString()))
            .body(appConfigDTO);
    }

    /**
     * {@code PATCH  /app-configs/:id} : Partial updates given fields of an existing appConfig, field will ignore if it is null
     *
     * @param id the id of the appConfigDTO to save.
     * @param appConfigDTO the appConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appConfigDTO,
     * or with status {@code 400 (Bad Request)} if the appConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppConfigDTO> partialUpdateAppConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AppConfigDTO appConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppConfig partially : {}, {}", id, appConfigDTO);
        if (appConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppConfigDTO> result = appConfigService.partialUpdate(appConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /app-configs} : get all the appConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appConfigs in body.
     */
    @GetMapping("")
    public List<AppConfigDTO> getAllAppConfigs() {
        LOG.debug("REST request to get all AppConfigs");
        return appConfigService.findAll();
    }

    /**
     * {@code GET  /app-configs/:id} : get the "id" appConfig.
     *
     * @param id the id of the appConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppConfigDTO> getAppConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AppConfig : {}", id);
        Optional<AppConfigDTO> appConfigDTO = appConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appConfigDTO);
    }

    /**
     * {@code DELETE  /app-configs/:id} : delete the "id" appConfig.
     *
     * @param id the id of the appConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AppConfig : {}", id);
        appConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

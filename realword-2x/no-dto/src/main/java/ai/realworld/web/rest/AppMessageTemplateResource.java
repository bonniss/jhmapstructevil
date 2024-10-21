package ai.realworld.web.rest;

import ai.realworld.domain.AppMessageTemplate;
import ai.realworld.repository.AppMessageTemplateRepository;
import ai.realworld.service.AppMessageTemplateQueryService;
import ai.realworld.service.AppMessageTemplateService;
import ai.realworld.service.criteria.AppMessageTemplateCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ai.realworld.domain.AppMessageTemplate}.
 */
@RestController
@RequestMapping("/api/app-message-templates")
public class AppMessageTemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppMessageTemplateResource.class);

    private static final String ENTITY_NAME = "appMessageTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppMessageTemplateService appMessageTemplateService;

    private final AppMessageTemplateRepository appMessageTemplateRepository;

    private final AppMessageTemplateQueryService appMessageTemplateQueryService;

    public AppMessageTemplateResource(
        AppMessageTemplateService appMessageTemplateService,
        AppMessageTemplateRepository appMessageTemplateRepository,
        AppMessageTemplateQueryService appMessageTemplateQueryService
    ) {
        this.appMessageTemplateService = appMessageTemplateService;
        this.appMessageTemplateRepository = appMessageTemplateRepository;
        this.appMessageTemplateQueryService = appMessageTemplateQueryService;
    }

    /**
     * {@code POST  /app-message-templates} : Create a new appMessageTemplate.
     *
     * @param appMessageTemplate the appMessageTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appMessageTemplate, or with status {@code 400 (Bad Request)} if the appMessageTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppMessageTemplate> createAppMessageTemplate(@Valid @RequestBody AppMessageTemplate appMessageTemplate)
        throws URISyntaxException {
        LOG.debug("REST request to save AppMessageTemplate : {}", appMessageTemplate);
        if (appMessageTemplate.getId() != null) {
            throw new BadRequestAlertException("A new appMessageTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appMessageTemplate = appMessageTemplateService.save(appMessageTemplate);
        return ResponseEntity.created(new URI("/api/app-message-templates/" + appMessageTemplate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appMessageTemplate.getId().toString()))
            .body(appMessageTemplate);
    }

    /**
     * {@code PUT  /app-message-templates/:id} : Updates an existing appMessageTemplate.
     *
     * @param id the id of the appMessageTemplate to save.
     * @param appMessageTemplate the appMessageTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appMessageTemplate,
     * or with status {@code 400 (Bad Request)} if the appMessageTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appMessageTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppMessageTemplate> updateAppMessageTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppMessageTemplate appMessageTemplate
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppMessageTemplate : {}, {}", id, appMessageTemplate);
        if (appMessageTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appMessageTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appMessageTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appMessageTemplate = appMessageTemplateService.update(appMessageTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appMessageTemplate.getId().toString()))
            .body(appMessageTemplate);
    }

    /**
     * {@code PATCH  /app-message-templates/:id} : Partial updates given fields of an existing appMessageTemplate, field will ignore if it is null
     *
     * @param id the id of the appMessageTemplate to save.
     * @param appMessageTemplate the appMessageTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appMessageTemplate,
     * or with status {@code 400 (Bad Request)} if the appMessageTemplate is not valid,
     * or with status {@code 404 (Not Found)} if the appMessageTemplate is not found,
     * or with status {@code 500 (Internal Server Error)} if the appMessageTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppMessageTemplate> partialUpdateAppMessageTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppMessageTemplate appMessageTemplate
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppMessageTemplate partially : {}, {}", id, appMessageTemplate);
        if (appMessageTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appMessageTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appMessageTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppMessageTemplate> result = appMessageTemplateService.partialUpdate(appMessageTemplate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appMessageTemplate.getId().toString())
        );
    }

    /**
     * {@code GET  /app-message-templates} : get all the appMessageTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appMessageTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppMessageTemplate>> getAllAppMessageTemplates(
        AppMessageTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AppMessageTemplates by criteria: {}", criteria);

        Page<AppMessageTemplate> page = appMessageTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-message-templates/count} : count all the appMessageTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAppMessageTemplates(AppMessageTemplateCriteria criteria) {
        LOG.debug("REST request to count AppMessageTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(appMessageTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-message-templates/:id} : get the "id" appMessageTemplate.
     *
     * @param id the id of the appMessageTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appMessageTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppMessageTemplate> getAppMessageTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AppMessageTemplate : {}", id);
        Optional<AppMessageTemplate> appMessageTemplate = appMessageTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appMessageTemplate);
    }

    /**
     * {@code DELETE  /app-message-templates/:id} : delete the "id" appMessageTemplate.
     *
     * @param id the id of the appMessageTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppMessageTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AppMessageTemplate : {}", id);
        appMessageTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

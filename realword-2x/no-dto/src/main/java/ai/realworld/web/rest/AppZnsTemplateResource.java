package ai.realworld.web.rest;

import ai.realworld.domain.AppZnsTemplate;
import ai.realworld.repository.AppZnsTemplateRepository;
import ai.realworld.service.AppZnsTemplateQueryService;
import ai.realworld.service.AppZnsTemplateService;
import ai.realworld.service.criteria.AppZnsTemplateCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AppZnsTemplate}.
 */
@RestController
@RequestMapping("/api/app-zns-templates")
public class AppZnsTemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(AppZnsTemplateResource.class);

    private static final String ENTITY_NAME = "appZnsTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppZnsTemplateService appZnsTemplateService;

    private final AppZnsTemplateRepository appZnsTemplateRepository;

    private final AppZnsTemplateQueryService appZnsTemplateQueryService;

    public AppZnsTemplateResource(
        AppZnsTemplateService appZnsTemplateService,
        AppZnsTemplateRepository appZnsTemplateRepository,
        AppZnsTemplateQueryService appZnsTemplateQueryService
    ) {
        this.appZnsTemplateService = appZnsTemplateService;
        this.appZnsTemplateRepository = appZnsTemplateRepository;
        this.appZnsTemplateQueryService = appZnsTemplateQueryService;
    }

    /**
     * {@code POST  /app-zns-templates} : Create a new appZnsTemplate.
     *
     * @param appZnsTemplate the appZnsTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appZnsTemplate, or with status {@code 400 (Bad Request)} if the appZnsTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppZnsTemplate> createAppZnsTemplate(@Valid @RequestBody AppZnsTemplate appZnsTemplate)
        throws URISyntaxException {
        LOG.debug("REST request to save AppZnsTemplate : {}", appZnsTemplate);
        if (appZnsTemplate.getId() != null) {
            throw new BadRequestAlertException("A new appZnsTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appZnsTemplate = appZnsTemplateService.save(appZnsTemplate);
        return ResponseEntity.created(new URI("/api/app-zns-templates/" + appZnsTemplate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appZnsTemplate.getId().toString()))
            .body(appZnsTemplate);
    }

    /**
     * {@code PUT  /app-zns-templates/:id} : Updates an existing appZnsTemplate.
     *
     * @param id the id of the appZnsTemplate to save.
     * @param appZnsTemplate the appZnsTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appZnsTemplate,
     * or with status {@code 400 (Bad Request)} if the appZnsTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appZnsTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppZnsTemplate> updateAppZnsTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppZnsTemplate appZnsTemplate
    ) throws URISyntaxException {
        LOG.debug("REST request to update AppZnsTemplate : {}, {}", id, appZnsTemplate);
        if (appZnsTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appZnsTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appZnsTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appZnsTemplate = appZnsTemplateService.update(appZnsTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appZnsTemplate.getId().toString()))
            .body(appZnsTemplate);
    }

    /**
     * {@code PATCH  /app-zns-templates/:id} : Partial updates given fields of an existing appZnsTemplate, field will ignore if it is null
     *
     * @param id the id of the appZnsTemplate to save.
     * @param appZnsTemplate the appZnsTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appZnsTemplate,
     * or with status {@code 400 (Bad Request)} if the appZnsTemplate is not valid,
     * or with status {@code 404 (Not Found)} if the appZnsTemplate is not found,
     * or with status {@code 500 (Internal Server Error)} if the appZnsTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppZnsTemplate> partialUpdateAppZnsTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppZnsTemplate appZnsTemplate
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AppZnsTemplate partially : {}, {}", id, appZnsTemplate);
        if (appZnsTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appZnsTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appZnsTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppZnsTemplate> result = appZnsTemplateService.partialUpdate(appZnsTemplate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appZnsTemplate.getId().toString())
        );
    }

    /**
     * {@code GET  /app-zns-templates} : get all the appZnsTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appZnsTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AppZnsTemplate>> getAllAppZnsTemplates(
        AppZnsTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AppZnsTemplates by criteria: {}", criteria);

        Page<AppZnsTemplate> page = appZnsTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-zns-templates/count} : count all the appZnsTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAppZnsTemplates(AppZnsTemplateCriteria criteria) {
        LOG.debug("REST request to count AppZnsTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(appZnsTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /app-zns-templates/:id} : get the "id" appZnsTemplate.
     *
     * @param id the id of the appZnsTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appZnsTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppZnsTemplate> getAppZnsTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AppZnsTemplate : {}", id);
        Optional<AppZnsTemplate> appZnsTemplate = appZnsTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appZnsTemplate);
    }

    /**
     * {@code DELETE  /app-zns-templates/:id} : delete the "id" appZnsTemplate.
     *
     * @param id the id of the appZnsTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppZnsTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AppZnsTemplate : {}", id);
        appZnsTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

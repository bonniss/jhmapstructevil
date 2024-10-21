package ai.realworld.web.rest;

import ai.realworld.domain.AlVueVueViUsage;
import ai.realworld.repository.AlVueVueViUsageRepository;
import ai.realworld.service.AlVueVueViUsageQueryService;
import ai.realworld.service.AlVueVueViUsageService;
import ai.realworld.service.criteria.AlVueVueViUsageCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link ai.realworld.domain.AlVueVueViUsage}.
 */
@RestController
@RequestMapping("/api/al-vue-vue-vi-usages")
public class AlVueVueViUsageResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViUsageResource.class);

    private static final String ENTITY_NAME = "alVueVueViUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueViUsageService alVueVueViUsageService;

    private final AlVueVueViUsageRepository alVueVueViUsageRepository;

    private final AlVueVueViUsageQueryService alVueVueViUsageQueryService;

    public AlVueVueViUsageResource(
        AlVueVueViUsageService alVueVueViUsageService,
        AlVueVueViUsageRepository alVueVueViUsageRepository,
        AlVueVueViUsageQueryService alVueVueViUsageQueryService
    ) {
        this.alVueVueViUsageService = alVueVueViUsageService;
        this.alVueVueViUsageRepository = alVueVueViUsageRepository;
        this.alVueVueViUsageQueryService = alVueVueViUsageQueryService;
    }

    /**
     * {@code POST  /al-vue-vue-vi-usages} : Create a new alVueVueViUsage.
     *
     * @param alVueVueViUsage the alVueVueViUsage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueViUsage, or with status {@code 400 (Bad Request)} if the alVueVueViUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueViUsage> createAlVueVueViUsage(@RequestBody AlVueVueViUsage alVueVueViUsage) throws URISyntaxException {
        LOG.debug("REST request to save AlVueVueViUsage : {}", alVueVueViUsage);
        if (alVueVueViUsage.getId() != null) {
            throw new BadRequestAlertException("A new alVueVueViUsage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueViUsage = alVueVueViUsageService.save(alVueVueViUsage);
        return ResponseEntity.created(new URI("/api/al-vue-vue-vi-usages/" + alVueVueViUsage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueViUsage.getId().toString()))
            .body(alVueVueViUsage);
    }

    /**
     * {@code PUT  /al-vue-vue-vi-usages/:id} : Updates an existing alVueVueViUsage.
     *
     * @param id the id of the alVueVueViUsage to save.
     * @param alVueVueViUsage the alVueVueViUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueViUsage,
     * or with status {@code 400 (Bad Request)} if the alVueVueViUsage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueViUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueViUsage> updateAlVueVueViUsage(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlVueVueViUsage alVueVueViUsage
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVueViUsage : {}, {}", id, alVueVueViUsage);
        if (alVueVueViUsage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueViUsage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueViUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueViUsage = alVueVueViUsageService.update(alVueVueViUsage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueViUsage.getId().toString()))
            .body(alVueVueViUsage);
    }

    /**
     * {@code PATCH  /al-vue-vue-vi-usages/:id} : Partial updates given fields of an existing alVueVueViUsage, field will ignore if it is null
     *
     * @param id the id of the alVueVueViUsage to save.
     * @param alVueVueViUsage the alVueVueViUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueViUsage,
     * or with status {@code 400 (Bad Request)} if the alVueVueViUsage is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueViUsage is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueViUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueViUsage> partialUpdateAlVueVueViUsage(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlVueVueViUsage alVueVueViUsage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVueViUsage partially : {}, {}", id, alVueVueViUsage);
        if (alVueVueViUsage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueViUsage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueViUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueViUsage> result = alVueVueViUsageService.partialUpdate(alVueVueViUsage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueViUsage.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vue-vi-usages} : get all the alVueVueViUsages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVueViUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueViUsage>> getAllAlVueVueViUsages(
        AlVueVueViUsageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVueViUsages by criteria: {}", criteria);

        Page<AlVueVueViUsage> page = alVueVueViUsageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vue-vi-usages/count} : count all the alVueVueViUsages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVueViUsages(AlVueVueViUsageCriteria criteria) {
        LOG.debug("REST request to count AlVueVueViUsages by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueViUsageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vue-vi-usages/:id} : get the "id" alVueVueViUsage.
     *
     * @param id the id of the alVueVueViUsage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueViUsage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueViUsage> getAlVueVueViUsage(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlVueVueViUsage : {}", id);
        Optional<AlVueVueViUsage> alVueVueViUsage = alVueVueViUsageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueViUsage);
    }

    /**
     * {@code DELETE  /al-vue-vue-vi-usages/:id} : delete the "id" alVueVueViUsage.
     *
     * @param id the id of the alVueVueViUsage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVueViUsage(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlVueVueViUsage : {}", id);
        alVueVueViUsageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

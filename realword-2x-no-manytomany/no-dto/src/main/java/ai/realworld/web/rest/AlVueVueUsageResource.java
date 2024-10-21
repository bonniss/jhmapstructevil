package ai.realworld.web.rest;

import ai.realworld.domain.AlVueVueUsage;
import ai.realworld.repository.AlVueVueUsageRepository;
import ai.realworld.service.AlVueVueUsageQueryService;
import ai.realworld.service.AlVueVueUsageService;
import ai.realworld.service.criteria.AlVueVueUsageCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlVueVueUsage}.
 */
@RestController
@RequestMapping("/api/al-vue-vue-usages")
public class AlVueVueUsageResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueUsageResource.class);

    private static final String ENTITY_NAME = "alVueVueUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueUsageService alVueVueUsageService;

    private final AlVueVueUsageRepository alVueVueUsageRepository;

    private final AlVueVueUsageQueryService alVueVueUsageQueryService;

    public AlVueVueUsageResource(
        AlVueVueUsageService alVueVueUsageService,
        AlVueVueUsageRepository alVueVueUsageRepository,
        AlVueVueUsageQueryService alVueVueUsageQueryService
    ) {
        this.alVueVueUsageService = alVueVueUsageService;
        this.alVueVueUsageRepository = alVueVueUsageRepository;
        this.alVueVueUsageQueryService = alVueVueUsageQueryService;
    }

    /**
     * {@code POST  /al-vue-vue-usages} : Create a new alVueVueUsage.
     *
     * @param alVueVueUsage the alVueVueUsage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueUsage, or with status {@code 400 (Bad Request)} if the alVueVueUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueUsage> createAlVueVueUsage(@RequestBody AlVueVueUsage alVueVueUsage) throws URISyntaxException {
        LOG.debug("REST request to save AlVueVueUsage : {}", alVueVueUsage);
        if (alVueVueUsage.getId() != null) {
            throw new BadRequestAlertException("A new alVueVueUsage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueUsage = alVueVueUsageService.save(alVueVueUsage);
        return ResponseEntity.created(new URI("/api/al-vue-vue-usages/" + alVueVueUsage.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueUsage.getId().toString()))
            .body(alVueVueUsage);
    }

    /**
     * {@code PUT  /al-vue-vue-usages/:id} : Updates an existing alVueVueUsage.
     *
     * @param id the id of the alVueVueUsage to save.
     * @param alVueVueUsage the alVueVueUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueUsage,
     * or with status {@code 400 (Bad Request)} if the alVueVueUsage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueUsage> updateAlVueVueUsage(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlVueVueUsage alVueVueUsage
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVueUsage : {}, {}", id, alVueVueUsage);
        if (alVueVueUsage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueUsage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueUsage = alVueVueUsageService.update(alVueVueUsage);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueUsage.getId().toString()))
            .body(alVueVueUsage);
    }

    /**
     * {@code PATCH  /al-vue-vue-usages/:id} : Partial updates given fields of an existing alVueVueUsage, field will ignore if it is null
     *
     * @param id the id of the alVueVueUsage to save.
     * @param alVueVueUsage the alVueVueUsage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueUsage,
     * or with status {@code 400 (Bad Request)} if the alVueVueUsage is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueUsage is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueUsage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueUsage> partialUpdateAlVueVueUsage(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlVueVueUsage alVueVueUsage
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVueUsage partially : {}, {}", id, alVueVueUsage);
        if (alVueVueUsage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueUsage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueUsage> result = alVueVueUsageService.partialUpdate(alVueVueUsage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueUsage.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vue-usages} : get all the alVueVueUsages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVueUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueUsage>> getAllAlVueVueUsages(
        AlVueVueUsageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVueUsages by criteria: {}", criteria);

        Page<AlVueVueUsage> page = alVueVueUsageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vue-usages/count} : count all the alVueVueUsages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVueUsages(AlVueVueUsageCriteria criteria) {
        LOG.debug("REST request to count AlVueVueUsages by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueUsageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vue-usages/:id} : get the "id" alVueVueUsage.
     *
     * @param id the id of the alVueVueUsage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueUsage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueUsage> getAlVueVueUsage(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlVueVueUsage : {}", id);
        Optional<AlVueVueUsage> alVueVueUsage = alVueVueUsageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueUsage);
    }

    /**
     * {@code DELETE  /al-vue-vue-usages/:id} : delete the "id" alVueVueUsage.
     *
     * @param id the id of the alVueVueUsage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVueUsage(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlVueVueUsage : {}", id);
        alVueVueUsageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

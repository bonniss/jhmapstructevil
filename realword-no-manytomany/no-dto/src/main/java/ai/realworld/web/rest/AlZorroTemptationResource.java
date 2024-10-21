package ai.realworld.web.rest;

import ai.realworld.domain.AlZorroTemptation;
import ai.realworld.repository.AlZorroTemptationRepository;
import ai.realworld.service.AlZorroTemptationQueryService;
import ai.realworld.service.AlZorroTemptationService;
import ai.realworld.service.criteria.AlZorroTemptationCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlZorroTemptation}.
 */
@RestController
@RequestMapping("/api/al-zorro-temptations")
public class AlZorroTemptationResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationResource.class);

    private static final String ENTITY_NAME = "alZorroTemptation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlZorroTemptationService alZorroTemptationService;

    private final AlZorroTemptationRepository alZorroTemptationRepository;

    private final AlZorroTemptationQueryService alZorroTemptationQueryService;

    public AlZorroTemptationResource(
        AlZorroTemptationService alZorroTemptationService,
        AlZorroTemptationRepository alZorroTemptationRepository,
        AlZorroTemptationQueryService alZorroTemptationQueryService
    ) {
        this.alZorroTemptationService = alZorroTemptationService;
        this.alZorroTemptationRepository = alZorroTemptationRepository;
        this.alZorroTemptationQueryService = alZorroTemptationQueryService;
    }

    /**
     * {@code POST  /al-zorro-temptations} : Create a new alZorroTemptation.
     *
     * @param alZorroTemptation the alZorroTemptation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alZorroTemptation, or with status {@code 400 (Bad Request)} if the alZorroTemptation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlZorroTemptation> createAlZorroTemptation(@Valid @RequestBody AlZorroTemptation alZorroTemptation)
        throws URISyntaxException {
        LOG.debug("REST request to save AlZorroTemptation : {}", alZorroTemptation);
        if (alZorroTemptation.getId() != null) {
            throw new BadRequestAlertException("A new alZorroTemptation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alZorroTemptation = alZorroTemptationService.save(alZorroTemptation);
        return ResponseEntity.created(new URI("/api/al-zorro-temptations/" + alZorroTemptation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alZorroTemptation.getId().toString()))
            .body(alZorroTemptation);
    }

    /**
     * {@code PUT  /al-zorro-temptations/:id} : Updates an existing alZorroTemptation.
     *
     * @param id the id of the alZorroTemptation to save.
     * @param alZorroTemptation the alZorroTemptation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptation,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlZorroTemptation> updateAlZorroTemptation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlZorroTemptation alZorroTemptation
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlZorroTemptation : {}, {}", id, alZorroTemptation);
        if (alZorroTemptation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alZorroTemptation = alZorroTemptationService.update(alZorroTemptation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptation.getId().toString()))
            .body(alZorroTemptation);
    }

    /**
     * {@code PATCH  /al-zorro-temptations/:id} : Partial updates given fields of an existing alZorroTemptation, field will ignore if it is null
     *
     * @param id the id of the alZorroTemptation to save.
     * @param alZorroTemptation the alZorroTemptation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptation,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptation is not valid,
     * or with status {@code 404 (Not Found)} if the alZorroTemptation is not found,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlZorroTemptation> partialUpdateAlZorroTemptation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlZorroTemptation alZorroTemptation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlZorroTemptation partially : {}, {}", id, alZorroTemptation);
        if (alZorroTemptation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlZorroTemptation> result = alZorroTemptationService.partialUpdate(alZorroTemptation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptation.getId().toString())
        );
    }

    /**
     * {@code GET  /al-zorro-temptations} : get all the alZorroTemptations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alZorroTemptations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlZorroTemptation>> getAllAlZorroTemptations(
        AlZorroTemptationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlZorroTemptations by criteria: {}", criteria);

        Page<AlZorroTemptation> page = alZorroTemptationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-zorro-temptations/count} : count all the alZorroTemptations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlZorroTemptations(AlZorroTemptationCriteria criteria) {
        LOG.debug("REST request to count AlZorroTemptations by criteria: {}", criteria);
        return ResponseEntity.ok().body(alZorroTemptationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-zorro-temptations/:id} : get the "id" alZorroTemptation.
     *
     * @param id the id of the alZorroTemptation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alZorroTemptation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlZorroTemptation> getAlZorroTemptation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlZorroTemptation : {}", id);
        Optional<AlZorroTemptation> alZorroTemptation = alZorroTemptationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alZorroTemptation);
    }

    /**
     * {@code DELETE  /al-zorro-temptations/:id} : delete the "id" alZorroTemptation.
     *
     * @param id the id of the alZorroTemptation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlZorroTemptation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlZorroTemptation : {}", id);
        alZorroTemptationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package ai.realworld.web.rest;

import ai.realworld.domain.AlBetonamuRelation;
import ai.realworld.repository.AlBetonamuRelationRepository;
import ai.realworld.service.AlBetonamuRelationQueryService;
import ai.realworld.service.AlBetonamuRelationService;
import ai.realworld.service.criteria.AlBetonamuRelationCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlBetonamuRelation}.
 */
@RestController
@RequestMapping("/api/al-betonamu-relations")
public class AlBetonamuRelationResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlBetonamuRelationResource.class);

    private static final String ENTITY_NAME = "alBetonamuRelation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlBetonamuRelationService alBetonamuRelationService;

    private final AlBetonamuRelationRepository alBetonamuRelationRepository;

    private final AlBetonamuRelationQueryService alBetonamuRelationQueryService;

    public AlBetonamuRelationResource(
        AlBetonamuRelationService alBetonamuRelationService,
        AlBetonamuRelationRepository alBetonamuRelationRepository,
        AlBetonamuRelationQueryService alBetonamuRelationQueryService
    ) {
        this.alBetonamuRelationService = alBetonamuRelationService;
        this.alBetonamuRelationRepository = alBetonamuRelationRepository;
        this.alBetonamuRelationQueryService = alBetonamuRelationQueryService;
    }

    /**
     * {@code POST  /al-betonamu-relations} : Create a new alBetonamuRelation.
     *
     * @param alBetonamuRelation the alBetonamuRelation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alBetonamuRelation, or with status {@code 400 (Bad Request)} if the alBetonamuRelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlBetonamuRelation> createAlBetonamuRelation(@Valid @RequestBody AlBetonamuRelation alBetonamuRelation)
        throws URISyntaxException {
        LOG.debug("REST request to save AlBetonamuRelation : {}", alBetonamuRelation);
        if (alBetonamuRelation.getId() != null) {
            throw new BadRequestAlertException("A new alBetonamuRelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alBetonamuRelation = alBetonamuRelationService.save(alBetonamuRelation);
        return ResponseEntity.created(new URI("/api/al-betonamu-relations/" + alBetonamuRelation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alBetonamuRelation.getId().toString()))
            .body(alBetonamuRelation);
    }

    /**
     * {@code PUT  /al-betonamu-relations/:id} : Updates an existing alBetonamuRelation.
     *
     * @param id the id of the alBetonamuRelation to save.
     * @param alBetonamuRelation the alBetonamuRelation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBetonamuRelation,
     * or with status {@code 400 (Bad Request)} if the alBetonamuRelation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alBetonamuRelation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlBetonamuRelation> updateAlBetonamuRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlBetonamuRelation alBetonamuRelation
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlBetonamuRelation : {}, {}", id, alBetonamuRelation);
        if (alBetonamuRelation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBetonamuRelation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBetonamuRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alBetonamuRelation = alBetonamuRelationService.update(alBetonamuRelation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBetonamuRelation.getId().toString()))
            .body(alBetonamuRelation);
    }

    /**
     * {@code PATCH  /al-betonamu-relations/:id} : Partial updates given fields of an existing alBetonamuRelation, field will ignore if it is null
     *
     * @param id the id of the alBetonamuRelation to save.
     * @param alBetonamuRelation the alBetonamuRelation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBetonamuRelation,
     * or with status {@code 400 (Bad Request)} if the alBetonamuRelation is not valid,
     * or with status {@code 404 (Not Found)} if the alBetonamuRelation is not found,
     * or with status {@code 500 (Internal Server Error)} if the alBetonamuRelation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlBetonamuRelation> partialUpdateAlBetonamuRelation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlBetonamuRelation alBetonamuRelation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlBetonamuRelation partially : {}, {}", id, alBetonamuRelation);
        if (alBetonamuRelation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBetonamuRelation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBetonamuRelationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlBetonamuRelation> result = alBetonamuRelationService.partialUpdate(alBetonamuRelation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBetonamuRelation.getId().toString())
        );
    }

    /**
     * {@code GET  /al-betonamu-relations} : get all the alBetonamuRelations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alBetonamuRelations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlBetonamuRelation>> getAllAlBetonamuRelations(
        AlBetonamuRelationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlBetonamuRelations by criteria: {}", criteria);

        Page<AlBetonamuRelation> page = alBetonamuRelationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-betonamu-relations/count} : count all the alBetonamuRelations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlBetonamuRelations(AlBetonamuRelationCriteria criteria) {
        LOG.debug("REST request to count AlBetonamuRelations by criteria: {}", criteria);
        return ResponseEntity.ok().body(alBetonamuRelationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-betonamu-relations/:id} : get the "id" alBetonamuRelation.
     *
     * @param id the id of the alBetonamuRelation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alBetonamuRelation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlBetonamuRelation> getAlBetonamuRelation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlBetonamuRelation : {}", id);
        Optional<AlBetonamuRelation> alBetonamuRelation = alBetonamuRelationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alBetonamuRelation);
    }

    /**
     * {@code DELETE  /al-betonamu-relations/:id} : delete the "id" alBetonamuRelation.
     *
     * @param id the id of the alBetonamuRelation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlBetonamuRelation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlBetonamuRelation : {}", id);
        alBetonamuRelationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

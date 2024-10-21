package ai.realworld.web.rest;

import ai.realworld.domain.AlGoreCondition;
import ai.realworld.repository.AlGoreConditionRepository;
import ai.realworld.service.AlGoreConditionQueryService;
import ai.realworld.service.AlGoreConditionService;
import ai.realworld.service.criteria.AlGoreConditionCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlGoreCondition}.
 */
@RestController
@RequestMapping("/api/al-gore-conditions")
public class AlGoreConditionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionResource.class);

    private static final String ENTITY_NAME = "alGoreCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoreConditionService alGoreConditionService;

    private final AlGoreConditionRepository alGoreConditionRepository;

    private final AlGoreConditionQueryService alGoreConditionQueryService;

    public AlGoreConditionResource(
        AlGoreConditionService alGoreConditionService,
        AlGoreConditionRepository alGoreConditionRepository,
        AlGoreConditionQueryService alGoreConditionQueryService
    ) {
        this.alGoreConditionService = alGoreConditionService;
        this.alGoreConditionRepository = alGoreConditionRepository;
        this.alGoreConditionQueryService = alGoreConditionQueryService;
    }

    /**
     * {@code POST  /al-gore-conditions} : Create a new alGoreCondition.
     *
     * @param alGoreCondition the alGoreCondition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoreCondition, or with status {@code 400 (Bad Request)} if the alGoreCondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoreCondition> createAlGoreCondition(@Valid @RequestBody AlGoreCondition alGoreCondition)
        throws URISyntaxException {
        LOG.debug("REST request to save AlGoreCondition : {}", alGoreCondition);
        if (alGoreCondition.getId() != null) {
            throw new BadRequestAlertException("A new alGoreCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoreCondition = alGoreConditionService.save(alGoreCondition);
        return ResponseEntity.created(new URI("/api/al-gore-conditions/" + alGoreCondition.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoreCondition.getId().toString()))
            .body(alGoreCondition);
    }

    /**
     * {@code PUT  /al-gore-conditions/:id} : Updates an existing alGoreCondition.
     *
     * @param id the id of the alGoreCondition to save.
     * @param alGoreCondition the alGoreCondition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreCondition,
     * or with status {@code 400 (Bad Request)} if the alGoreCondition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoreCondition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoreCondition> updateAlGoreCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlGoreCondition alGoreCondition
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGoreCondition : {}, {}", id, alGoreCondition);
        if (alGoreCondition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreCondition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoreCondition = alGoreConditionService.update(alGoreCondition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreCondition.getId().toString()))
            .body(alGoreCondition);
    }

    /**
     * {@code PATCH  /al-gore-conditions/:id} : Partial updates given fields of an existing alGoreCondition, field will ignore if it is null
     *
     * @param id the id of the alGoreCondition to save.
     * @param alGoreCondition the alGoreCondition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreCondition,
     * or with status {@code 400 (Bad Request)} if the alGoreCondition is not valid,
     * or with status {@code 404 (Not Found)} if the alGoreCondition is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoreCondition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoreCondition> partialUpdateAlGoreCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlGoreCondition alGoreCondition
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGoreCondition partially : {}, {}", id, alGoreCondition);
        if (alGoreCondition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreCondition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoreCondition> result = alGoreConditionService.partialUpdate(alGoreCondition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreCondition.getId().toString())
        );
    }

    /**
     * {@code GET  /al-gore-conditions} : get all the alGoreConditions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGoreConditions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoreCondition>> getAllAlGoreConditions(
        AlGoreConditionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGoreConditions by criteria: {}", criteria);

        Page<AlGoreCondition> page = alGoreConditionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-gore-conditions/count} : count all the alGoreConditions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGoreConditions(AlGoreConditionCriteria criteria) {
        LOG.debug("REST request to count AlGoreConditions by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoreConditionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-gore-conditions/:id} : get the "id" alGoreCondition.
     *
     * @param id the id of the alGoreCondition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoreCondition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoreCondition> getAlGoreCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlGoreCondition : {}", id);
        Optional<AlGoreCondition> alGoreCondition = alGoreConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoreCondition);
    }

    /**
     * {@code DELETE  /al-gore-conditions/:id} : delete the "id" alGoreCondition.
     *
     * @param id the id of the alGoreCondition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGoreCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlGoreCondition : {}", id);
        alGoreConditionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

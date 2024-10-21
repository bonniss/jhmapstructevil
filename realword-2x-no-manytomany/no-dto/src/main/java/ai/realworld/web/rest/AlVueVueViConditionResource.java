package ai.realworld.web.rest;

import ai.realworld.domain.AlVueVueViCondition;
import ai.realworld.repository.AlVueVueViConditionRepository;
import ai.realworld.service.AlVueVueViConditionQueryService;
import ai.realworld.service.AlVueVueViConditionService;
import ai.realworld.service.criteria.AlVueVueViConditionCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlVueVueViCondition}.
 */
@RestController
@RequestMapping("/api/al-vue-vue-vi-conditions")
public class AlVueVueViConditionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViConditionResource.class);

    private static final String ENTITY_NAME = "alVueVueViCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueViConditionService alVueVueViConditionService;

    private final AlVueVueViConditionRepository alVueVueViConditionRepository;

    private final AlVueVueViConditionQueryService alVueVueViConditionQueryService;

    public AlVueVueViConditionResource(
        AlVueVueViConditionService alVueVueViConditionService,
        AlVueVueViConditionRepository alVueVueViConditionRepository,
        AlVueVueViConditionQueryService alVueVueViConditionQueryService
    ) {
        this.alVueVueViConditionService = alVueVueViConditionService;
        this.alVueVueViConditionRepository = alVueVueViConditionRepository;
        this.alVueVueViConditionQueryService = alVueVueViConditionQueryService;
    }

    /**
     * {@code POST  /al-vue-vue-vi-conditions} : Create a new alVueVueViCondition.
     *
     * @param alVueVueViCondition the alVueVueViCondition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueViCondition, or with status {@code 400 (Bad Request)} if the alVueVueViCondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueViCondition> createAlVueVueViCondition(@Valid @RequestBody AlVueVueViCondition alVueVueViCondition)
        throws URISyntaxException {
        LOG.debug("REST request to save AlVueVueViCondition : {}", alVueVueViCondition);
        if (alVueVueViCondition.getId() != null) {
            throw new BadRequestAlertException("A new alVueVueViCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueViCondition = alVueVueViConditionService.save(alVueVueViCondition);
        return ResponseEntity.created(new URI("/api/al-vue-vue-vi-conditions/" + alVueVueViCondition.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueViCondition.getId().toString()))
            .body(alVueVueViCondition);
    }

    /**
     * {@code PUT  /al-vue-vue-vi-conditions/:id} : Updates an existing alVueVueViCondition.
     *
     * @param id the id of the alVueVueViCondition to save.
     * @param alVueVueViCondition the alVueVueViCondition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueViCondition,
     * or with status {@code 400 (Bad Request)} if the alVueVueViCondition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueViCondition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueViCondition> updateAlVueVueViCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlVueVueViCondition alVueVueViCondition
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVueViCondition : {}, {}", id, alVueVueViCondition);
        if (alVueVueViCondition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueViCondition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueViConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueViCondition = alVueVueViConditionService.update(alVueVueViCondition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueViCondition.getId().toString()))
            .body(alVueVueViCondition);
    }

    /**
     * {@code PATCH  /al-vue-vue-vi-conditions/:id} : Partial updates given fields of an existing alVueVueViCondition, field will ignore if it is null
     *
     * @param id the id of the alVueVueViCondition to save.
     * @param alVueVueViCondition the alVueVueViCondition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueViCondition,
     * or with status {@code 400 (Bad Request)} if the alVueVueViCondition is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueViCondition is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueViCondition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueViCondition> partialUpdateAlVueVueViCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlVueVueViCondition alVueVueViCondition
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVueViCondition partially : {}, {}", id, alVueVueViCondition);
        if (alVueVueViCondition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueViCondition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueViConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueViCondition> result = alVueVueViConditionService.partialUpdate(alVueVueViCondition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueViCondition.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vue-vi-conditions} : get all the alVueVueViConditions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVueViConditions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueViCondition>> getAllAlVueVueViConditions(
        AlVueVueViConditionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVueViConditions by criteria: {}", criteria);

        Page<AlVueVueViCondition> page = alVueVueViConditionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vue-vi-conditions/count} : count all the alVueVueViConditions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVueViConditions(AlVueVueViConditionCriteria criteria) {
        LOG.debug("REST request to count AlVueVueViConditions by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueViConditionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vue-vi-conditions/:id} : get the "id" alVueVueViCondition.
     *
     * @param id the id of the alVueVueViCondition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueViCondition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueViCondition> getAlVueVueViCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlVueVueViCondition : {}", id);
        Optional<AlVueVueViCondition> alVueVueViCondition = alVueVueViConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueViCondition);
    }

    /**
     * {@code DELETE  /al-vue-vue-vi-conditions/:id} : delete the "id" alVueVueViCondition.
     *
     * @param id the id of the alVueVueViCondition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVueViCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlVueVueViCondition : {}", id);
        alVueVueViConditionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

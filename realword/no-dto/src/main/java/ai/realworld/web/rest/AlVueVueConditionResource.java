package ai.realworld.web.rest;

import ai.realworld.domain.AlVueVueCondition;
import ai.realworld.repository.AlVueVueConditionRepository;
import ai.realworld.service.AlVueVueConditionQueryService;
import ai.realworld.service.AlVueVueConditionService;
import ai.realworld.service.criteria.AlVueVueConditionCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlVueVueCondition}.
 */
@RestController
@RequestMapping("/api/al-vue-vue-conditions")
public class AlVueVueConditionResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueConditionResource.class);

    private static final String ENTITY_NAME = "alVueVueCondition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueConditionService alVueVueConditionService;

    private final AlVueVueConditionRepository alVueVueConditionRepository;

    private final AlVueVueConditionQueryService alVueVueConditionQueryService;

    public AlVueVueConditionResource(
        AlVueVueConditionService alVueVueConditionService,
        AlVueVueConditionRepository alVueVueConditionRepository,
        AlVueVueConditionQueryService alVueVueConditionQueryService
    ) {
        this.alVueVueConditionService = alVueVueConditionService;
        this.alVueVueConditionRepository = alVueVueConditionRepository;
        this.alVueVueConditionQueryService = alVueVueConditionQueryService;
    }

    /**
     * {@code POST  /al-vue-vue-conditions} : Create a new alVueVueCondition.
     *
     * @param alVueVueCondition the alVueVueCondition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueCondition, or with status {@code 400 (Bad Request)} if the alVueVueCondition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueCondition> createAlVueVueCondition(@Valid @RequestBody AlVueVueCondition alVueVueCondition)
        throws URISyntaxException {
        LOG.debug("REST request to save AlVueVueCondition : {}", alVueVueCondition);
        if (alVueVueCondition.getId() != null) {
            throw new BadRequestAlertException("A new alVueVueCondition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueCondition = alVueVueConditionService.save(alVueVueCondition);
        return ResponseEntity.created(new URI("/api/al-vue-vue-conditions/" + alVueVueCondition.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueCondition.getId().toString()))
            .body(alVueVueCondition);
    }

    /**
     * {@code PUT  /al-vue-vue-conditions/:id} : Updates an existing alVueVueCondition.
     *
     * @param id the id of the alVueVueCondition to save.
     * @param alVueVueCondition the alVueVueCondition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueCondition,
     * or with status {@code 400 (Bad Request)} if the alVueVueCondition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueCondition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueCondition> updateAlVueVueCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlVueVueCondition alVueVueCondition
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVueCondition : {}, {}", id, alVueVueCondition);
        if (alVueVueCondition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueCondition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueCondition = alVueVueConditionService.update(alVueVueCondition);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueCondition.getId().toString()))
            .body(alVueVueCondition);
    }

    /**
     * {@code PATCH  /al-vue-vue-conditions/:id} : Partial updates given fields of an existing alVueVueCondition, field will ignore if it is null
     *
     * @param id the id of the alVueVueCondition to save.
     * @param alVueVueCondition the alVueVueCondition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueCondition,
     * or with status {@code 400 (Bad Request)} if the alVueVueCondition is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueCondition is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueCondition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueCondition> partialUpdateAlVueVueCondition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlVueVueCondition alVueVueCondition
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVueCondition partially : {}, {}", id, alVueVueCondition);
        if (alVueVueCondition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueCondition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueConditionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueCondition> result = alVueVueConditionService.partialUpdate(alVueVueCondition);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueCondition.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vue-conditions} : get all the alVueVueConditions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVueConditions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueCondition>> getAllAlVueVueConditions(
        AlVueVueConditionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVueConditions by criteria: {}", criteria);

        Page<AlVueVueCondition> page = alVueVueConditionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vue-conditions/count} : count all the alVueVueConditions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVueConditions(AlVueVueConditionCriteria criteria) {
        LOG.debug("REST request to count AlVueVueConditions by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueConditionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vue-conditions/:id} : get the "id" alVueVueCondition.
     *
     * @param id the id of the alVueVueCondition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueCondition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueCondition> getAlVueVueCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlVueVueCondition : {}", id);
        Optional<AlVueVueCondition> alVueVueCondition = alVueVueConditionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueCondition);
    }

    /**
     * {@code DELETE  /al-vue-vue-conditions/:id} : delete the "id" alVueVueCondition.
     *
     * @param id the id of the alVueVueCondition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVueCondition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlVueVueCondition : {}", id);
        alVueVueConditionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

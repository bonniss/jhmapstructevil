package ai.realworld.web.rest;

import ai.realworld.domain.AlMemTierVi;
import ai.realworld.repository.AlMemTierViRepository;
import ai.realworld.service.AlMemTierViQueryService;
import ai.realworld.service.AlMemTierViService;
import ai.realworld.service.criteria.AlMemTierViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlMemTierVi}.
 */
@RestController
@RequestMapping("/api/al-mem-tier-vis")
public class AlMemTierViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlMemTierViResource.class);

    private static final String ENTITY_NAME = "alMemTierVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlMemTierViService alMemTierViService;

    private final AlMemTierViRepository alMemTierViRepository;

    private final AlMemTierViQueryService alMemTierViQueryService;

    public AlMemTierViResource(
        AlMemTierViService alMemTierViService,
        AlMemTierViRepository alMemTierViRepository,
        AlMemTierViQueryService alMemTierViQueryService
    ) {
        this.alMemTierViService = alMemTierViService;
        this.alMemTierViRepository = alMemTierViRepository;
        this.alMemTierViQueryService = alMemTierViQueryService;
    }

    /**
     * {@code POST  /al-mem-tier-vis} : Create a new alMemTierVi.
     *
     * @param alMemTierVi the alMemTierVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alMemTierVi, or with status {@code 400 (Bad Request)} if the alMemTierVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlMemTierVi> createAlMemTierVi(@Valid @RequestBody AlMemTierVi alMemTierVi) throws URISyntaxException {
        LOG.debug("REST request to save AlMemTierVi : {}", alMemTierVi);
        if (alMemTierVi.getId() != null) {
            throw new BadRequestAlertException("A new alMemTierVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alMemTierVi = alMemTierViService.save(alMemTierVi);
        return ResponseEntity.created(new URI("/api/al-mem-tier-vis/" + alMemTierVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alMemTierVi.getId().toString()))
            .body(alMemTierVi);
    }

    /**
     * {@code PUT  /al-mem-tier-vis/:id} : Updates an existing alMemTierVi.
     *
     * @param id the id of the alMemTierVi to save.
     * @param alMemTierVi the alMemTierVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMemTierVi,
     * or with status {@code 400 (Bad Request)} if the alMemTierVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alMemTierVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlMemTierVi> updateAlMemTierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlMemTierVi alMemTierVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlMemTierVi : {}, {}", id, alMemTierVi);
        if (alMemTierVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMemTierVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMemTierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alMemTierVi = alMemTierViService.update(alMemTierVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMemTierVi.getId().toString()))
            .body(alMemTierVi);
    }

    /**
     * {@code PATCH  /al-mem-tier-vis/:id} : Partial updates given fields of an existing alMemTierVi, field will ignore if it is null
     *
     * @param id the id of the alMemTierVi to save.
     * @param alMemTierVi the alMemTierVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMemTierVi,
     * or with status {@code 400 (Bad Request)} if the alMemTierVi is not valid,
     * or with status {@code 404 (Not Found)} if the alMemTierVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alMemTierVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlMemTierVi> partialUpdateAlMemTierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlMemTierVi alMemTierVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlMemTierVi partially : {}, {}", id, alMemTierVi);
        if (alMemTierVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMemTierVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMemTierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlMemTierVi> result = alMemTierViService.partialUpdate(alMemTierVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMemTierVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-mem-tier-vis} : get all the alMemTierVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alMemTierVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlMemTierVi>> getAllAlMemTierVis(
        AlMemTierViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlMemTierVis by criteria: {}", criteria);

        Page<AlMemTierVi> page = alMemTierViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-mem-tier-vis/count} : count all the alMemTierVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlMemTierVis(AlMemTierViCriteria criteria) {
        LOG.debug("REST request to count AlMemTierVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alMemTierViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-mem-tier-vis/:id} : get the "id" alMemTierVi.
     *
     * @param id the id of the alMemTierVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alMemTierVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlMemTierVi> getAlMemTierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlMemTierVi : {}", id);
        Optional<AlMemTierVi> alMemTierVi = alMemTierViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alMemTierVi);
    }

    /**
     * {@code DELETE  /al-mem-tier-vis/:id} : delete the "id" alMemTierVi.
     *
     * @param id the id of the alMemTierVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlMemTierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlMemTierVi : {}", id);
        alMemTierViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

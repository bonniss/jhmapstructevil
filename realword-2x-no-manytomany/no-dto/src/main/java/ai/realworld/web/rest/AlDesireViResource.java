package ai.realworld.web.rest;

import ai.realworld.domain.AlDesireVi;
import ai.realworld.repository.AlDesireViRepository;
import ai.realworld.service.AlDesireViQueryService;
import ai.realworld.service.AlDesireViService;
import ai.realworld.service.criteria.AlDesireViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link ai.realworld.domain.AlDesireVi}.
 */
@RestController
@RequestMapping("/api/al-desire-vis")
public class AlDesireViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireViResource.class);

    private static final String ENTITY_NAME = "alDesireVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlDesireViService alDesireViService;

    private final AlDesireViRepository alDesireViRepository;

    private final AlDesireViQueryService alDesireViQueryService;

    public AlDesireViResource(
        AlDesireViService alDesireViService,
        AlDesireViRepository alDesireViRepository,
        AlDesireViQueryService alDesireViQueryService
    ) {
        this.alDesireViService = alDesireViService;
        this.alDesireViRepository = alDesireViRepository;
        this.alDesireViQueryService = alDesireViQueryService;
    }

    /**
     * {@code POST  /al-desire-vis} : Create a new alDesireVi.
     *
     * @param alDesireVi the alDesireVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alDesireVi, or with status {@code 400 (Bad Request)} if the alDesireVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlDesireVi> createAlDesireVi(@Valid @RequestBody AlDesireVi alDesireVi) throws URISyntaxException {
        LOG.debug("REST request to save AlDesireVi : {}", alDesireVi);
        if (alDesireVi.getId() != null) {
            throw new BadRequestAlertException("A new alDesireVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alDesireVi = alDesireViService.save(alDesireVi);
        return ResponseEntity.created(new URI("/api/al-desire-vis/" + alDesireVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alDesireVi.getId().toString()))
            .body(alDesireVi);
    }

    /**
     * {@code PUT  /al-desire-vis/:id} : Updates an existing alDesireVi.
     *
     * @param id the id of the alDesireVi to save.
     * @param alDesireVi the alDesireVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alDesireVi,
     * or with status {@code 400 (Bad Request)} if the alDesireVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alDesireVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlDesireVi> updateAlDesireVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlDesireVi alDesireVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlDesireVi : {}, {}", id, alDesireVi);
        if (alDesireVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alDesireVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alDesireViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alDesireVi = alDesireViService.update(alDesireVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alDesireVi.getId().toString()))
            .body(alDesireVi);
    }

    /**
     * {@code PATCH  /al-desire-vis/:id} : Partial updates given fields of an existing alDesireVi, field will ignore if it is null
     *
     * @param id the id of the alDesireVi to save.
     * @param alDesireVi the alDesireVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alDesireVi,
     * or with status {@code 400 (Bad Request)} if the alDesireVi is not valid,
     * or with status {@code 404 (Not Found)} if the alDesireVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alDesireVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlDesireVi> partialUpdateAlDesireVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlDesireVi alDesireVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlDesireVi partially : {}, {}", id, alDesireVi);
        if (alDesireVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alDesireVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alDesireViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlDesireVi> result = alDesireViService.partialUpdate(alDesireVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alDesireVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-desire-vis} : get all the alDesireVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alDesireVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlDesireVi>> getAllAlDesireVis(
        AlDesireViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlDesireVis by criteria: {}", criteria);

        Page<AlDesireVi> page = alDesireViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-desire-vis/count} : count all the alDesireVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlDesireVis(AlDesireViCriteria criteria) {
        LOG.debug("REST request to count AlDesireVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alDesireViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-desire-vis/:id} : get the "id" alDesireVi.
     *
     * @param id the id of the alDesireVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alDesireVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlDesireVi> getAlDesireVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlDesireVi : {}", id);
        Optional<AlDesireVi> alDesireVi = alDesireViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alDesireVi);
    }

    /**
     * {@code DELETE  /al-desire-vis/:id} : delete the "id" alDesireVi.
     *
     * @param id the id of the alDesireVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlDesireVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlDesireVi : {}", id);
        alDesireViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

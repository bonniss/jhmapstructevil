package ai.realworld.web.rest;

import ai.realworld.domain.PamelaLouisVi;
import ai.realworld.repository.PamelaLouisViRepository;
import ai.realworld.service.PamelaLouisViQueryService;
import ai.realworld.service.PamelaLouisViService;
import ai.realworld.service.criteria.PamelaLouisViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.PamelaLouisVi}.
 */
@RestController
@RequestMapping("/api/pamela-louis-vis")
public class PamelaLouisViResource {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisViResource.class);

    private static final String ENTITY_NAME = "pamelaLouisVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PamelaLouisViService pamelaLouisViService;

    private final PamelaLouisViRepository pamelaLouisViRepository;

    private final PamelaLouisViQueryService pamelaLouisViQueryService;

    public PamelaLouisViResource(
        PamelaLouisViService pamelaLouisViService,
        PamelaLouisViRepository pamelaLouisViRepository,
        PamelaLouisViQueryService pamelaLouisViQueryService
    ) {
        this.pamelaLouisViService = pamelaLouisViService;
        this.pamelaLouisViRepository = pamelaLouisViRepository;
        this.pamelaLouisViQueryService = pamelaLouisViQueryService;
    }

    /**
     * {@code POST  /pamela-louis-vis} : Create a new pamelaLouisVi.
     *
     * @param pamelaLouisVi the pamelaLouisVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pamelaLouisVi, or with status {@code 400 (Bad Request)} if the pamelaLouisVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PamelaLouisVi> createPamelaLouisVi(@Valid @RequestBody PamelaLouisVi pamelaLouisVi) throws URISyntaxException {
        LOG.debug("REST request to save PamelaLouisVi : {}", pamelaLouisVi);
        if (pamelaLouisVi.getId() != null) {
            throw new BadRequestAlertException("A new pamelaLouisVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pamelaLouisVi = pamelaLouisViService.save(pamelaLouisVi);
        return ResponseEntity.created(new URI("/api/pamela-louis-vis/" + pamelaLouisVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pamelaLouisVi.getId().toString()))
            .body(pamelaLouisVi);
    }

    /**
     * {@code PUT  /pamela-louis-vis/:id} : Updates an existing pamelaLouisVi.
     *
     * @param id the id of the pamelaLouisVi to save.
     * @param pamelaLouisVi the pamelaLouisVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pamelaLouisVi,
     * or with status {@code 400 (Bad Request)} if the pamelaLouisVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pamelaLouisVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PamelaLouisVi> updatePamelaLouisVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PamelaLouisVi pamelaLouisVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update PamelaLouisVi : {}, {}", id, pamelaLouisVi);
        if (pamelaLouisVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pamelaLouisVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pamelaLouisViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pamelaLouisVi = pamelaLouisViService.update(pamelaLouisVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pamelaLouisVi.getId().toString()))
            .body(pamelaLouisVi);
    }

    /**
     * {@code PATCH  /pamela-louis-vis/:id} : Partial updates given fields of an existing pamelaLouisVi, field will ignore if it is null
     *
     * @param id the id of the pamelaLouisVi to save.
     * @param pamelaLouisVi the pamelaLouisVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pamelaLouisVi,
     * or with status {@code 400 (Bad Request)} if the pamelaLouisVi is not valid,
     * or with status {@code 404 (Not Found)} if the pamelaLouisVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the pamelaLouisVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PamelaLouisVi> partialUpdatePamelaLouisVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PamelaLouisVi pamelaLouisVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PamelaLouisVi partially : {}, {}", id, pamelaLouisVi);
        if (pamelaLouisVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pamelaLouisVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pamelaLouisViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PamelaLouisVi> result = pamelaLouisViService.partialUpdate(pamelaLouisVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pamelaLouisVi.getId().toString())
        );
    }

    /**
     * {@code GET  /pamela-louis-vis} : get all the pamelaLouisVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pamelaLouisVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PamelaLouisVi>> getAllPamelaLouisVis(
        PamelaLouisViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PamelaLouisVis by criteria: {}", criteria);

        Page<PamelaLouisVi> page = pamelaLouisViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pamela-louis-vis/count} : count all the pamelaLouisVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPamelaLouisVis(PamelaLouisViCriteria criteria) {
        LOG.debug("REST request to count PamelaLouisVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(pamelaLouisViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pamela-louis-vis/:id} : get the "id" pamelaLouisVi.
     *
     * @param id the id of the pamelaLouisVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pamelaLouisVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PamelaLouisVi> getPamelaLouisVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PamelaLouisVi : {}", id);
        Optional<PamelaLouisVi> pamelaLouisVi = pamelaLouisViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pamelaLouisVi);
    }

    /**
     * {@code DELETE  /pamela-louis-vis/:id} : delete the "id" pamelaLouisVi.
     *
     * @param id the id of the pamelaLouisVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePamelaLouisVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PamelaLouisVi : {}", id);
        pamelaLouisViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

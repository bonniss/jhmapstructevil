package ai.realworld.web.rest;

import ai.realworld.domain.MagharettiVi;
import ai.realworld.repository.MagharettiViRepository;
import ai.realworld.service.MagharettiViQueryService;
import ai.realworld.service.MagharettiViService;
import ai.realworld.service.criteria.MagharettiViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.MagharettiVi}.
 */
@RestController
@RequestMapping("/api/magharetti-vis")
public class MagharettiViResource {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiViResource.class);

    private static final String ENTITY_NAME = "magharettiVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MagharettiViService magharettiViService;

    private final MagharettiViRepository magharettiViRepository;

    private final MagharettiViQueryService magharettiViQueryService;

    public MagharettiViResource(
        MagharettiViService magharettiViService,
        MagharettiViRepository magharettiViRepository,
        MagharettiViQueryService magharettiViQueryService
    ) {
        this.magharettiViService = magharettiViService;
        this.magharettiViRepository = magharettiViRepository;
        this.magharettiViQueryService = magharettiViQueryService;
    }

    /**
     * {@code POST  /magharetti-vis} : Create a new magharettiVi.
     *
     * @param magharettiVi the magharettiVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new magharettiVi, or with status {@code 400 (Bad Request)} if the magharettiVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MagharettiVi> createMagharettiVi(@Valid @RequestBody MagharettiVi magharettiVi) throws URISyntaxException {
        LOG.debug("REST request to save MagharettiVi : {}", magharettiVi);
        if (magharettiVi.getId() != null) {
            throw new BadRequestAlertException("A new magharettiVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        magharettiVi = magharettiViService.save(magharettiVi);
        return ResponseEntity.created(new URI("/api/magharetti-vis/" + magharettiVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, magharettiVi.getId().toString()))
            .body(magharettiVi);
    }

    /**
     * {@code PUT  /magharetti-vis/:id} : Updates an existing magharettiVi.
     *
     * @param id the id of the magharettiVi to save.
     * @param magharettiVi the magharettiVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magharettiVi,
     * or with status {@code 400 (Bad Request)} if the magharettiVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the magharettiVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MagharettiVi> updateMagharettiVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MagharettiVi magharettiVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update MagharettiVi : {}, {}", id, magharettiVi);
        if (magharettiVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magharettiVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magharettiViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        magharettiVi = magharettiViService.update(magharettiVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magharettiVi.getId().toString()))
            .body(magharettiVi);
    }

    /**
     * {@code PATCH  /magharetti-vis/:id} : Partial updates given fields of an existing magharettiVi, field will ignore if it is null
     *
     * @param id the id of the magharettiVi to save.
     * @param magharettiVi the magharettiVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magharettiVi,
     * or with status {@code 400 (Bad Request)} if the magharettiVi is not valid,
     * or with status {@code 404 (Not Found)} if the magharettiVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the magharettiVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MagharettiVi> partialUpdateMagharettiVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MagharettiVi magharettiVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MagharettiVi partially : {}, {}", id, magharettiVi);
        if (magharettiVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magharettiVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magharettiViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MagharettiVi> result = magharettiViService.partialUpdate(magharettiVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magharettiVi.getId().toString())
        );
    }

    /**
     * {@code GET  /magharetti-vis} : get all the magharettiVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of magharettiVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MagharettiVi>> getAllMagharettiVis(
        MagharettiViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MagharettiVis by criteria: {}", criteria);

        Page<MagharettiVi> page = magharettiViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /magharetti-vis/count} : count all the magharettiVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMagharettiVis(MagharettiViCriteria criteria) {
        LOG.debug("REST request to count MagharettiVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(magharettiViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /magharetti-vis/:id} : get the "id" magharettiVi.
     *
     * @param id the id of the magharettiVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the magharettiVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MagharettiVi> getMagharettiVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MagharettiVi : {}", id);
        Optional<MagharettiVi> magharettiVi = magharettiViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(magharettiVi);
    }

    /**
     * {@code DELETE  /magharetti-vis/:id} : delete the "id" magharettiVi.
     *
     * @param id the id of the magharettiVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagharettiVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MagharettiVi : {}", id);
        magharettiViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

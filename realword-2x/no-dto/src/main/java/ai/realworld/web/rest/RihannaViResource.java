package ai.realworld.web.rest;

import ai.realworld.domain.RihannaVi;
import ai.realworld.repository.RihannaViRepository;
import ai.realworld.service.RihannaViQueryService;
import ai.realworld.service.RihannaViService;
import ai.realworld.service.criteria.RihannaViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.RihannaVi}.
 */
@RestController
@RequestMapping("/api/rihanna-vis")
public class RihannaViResource {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaViResource.class);

    private static final String ENTITY_NAME = "rihannaVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RihannaViService rihannaViService;

    private final RihannaViRepository rihannaViRepository;

    private final RihannaViQueryService rihannaViQueryService;

    public RihannaViResource(
        RihannaViService rihannaViService,
        RihannaViRepository rihannaViRepository,
        RihannaViQueryService rihannaViQueryService
    ) {
        this.rihannaViService = rihannaViService;
        this.rihannaViRepository = rihannaViRepository;
        this.rihannaViQueryService = rihannaViQueryService;
    }

    /**
     * {@code POST  /rihanna-vis} : Create a new rihannaVi.
     *
     * @param rihannaVi the rihannaVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rihannaVi, or with status {@code 400 (Bad Request)} if the rihannaVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RihannaVi> createRihannaVi(@Valid @RequestBody RihannaVi rihannaVi) throws URISyntaxException {
        LOG.debug("REST request to save RihannaVi : {}", rihannaVi);
        if (rihannaVi.getId() != null) {
            throw new BadRequestAlertException("A new rihannaVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rihannaVi = rihannaViService.save(rihannaVi);
        return ResponseEntity.created(new URI("/api/rihanna-vis/" + rihannaVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rihannaVi.getId().toString()))
            .body(rihannaVi);
    }

    /**
     * {@code PUT  /rihanna-vis/:id} : Updates an existing rihannaVi.
     *
     * @param id the id of the rihannaVi to save.
     * @param rihannaVi the rihannaVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rihannaVi,
     * or with status {@code 400 (Bad Request)} if the rihannaVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rihannaVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RihannaVi> updateRihannaVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RihannaVi rihannaVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update RihannaVi : {}, {}", id, rihannaVi);
        if (rihannaVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rihannaVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rihannaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rihannaVi = rihannaViService.update(rihannaVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rihannaVi.getId().toString()))
            .body(rihannaVi);
    }

    /**
     * {@code PATCH  /rihanna-vis/:id} : Partial updates given fields of an existing rihannaVi, field will ignore if it is null
     *
     * @param id the id of the rihannaVi to save.
     * @param rihannaVi the rihannaVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rihannaVi,
     * or with status {@code 400 (Bad Request)} if the rihannaVi is not valid,
     * or with status {@code 404 (Not Found)} if the rihannaVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the rihannaVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RihannaVi> partialUpdateRihannaVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RihannaVi rihannaVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RihannaVi partially : {}, {}", id, rihannaVi);
        if (rihannaVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rihannaVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rihannaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RihannaVi> result = rihannaViService.partialUpdate(rihannaVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rihannaVi.getId().toString())
        );
    }

    /**
     * {@code GET  /rihanna-vis} : get all the rihannaVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rihannaVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RihannaVi>> getAllRihannaVis(
        RihannaViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RihannaVis by criteria: {}", criteria);

        Page<RihannaVi> page = rihannaViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rihanna-vis/count} : count all the rihannaVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRihannaVis(RihannaViCriteria criteria) {
        LOG.debug("REST request to count RihannaVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(rihannaViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rihanna-vis/:id} : get the "id" rihannaVi.
     *
     * @param id the id of the rihannaVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rihannaVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RihannaVi> getRihannaVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RihannaVi : {}", id);
        Optional<RihannaVi> rihannaVi = rihannaViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rihannaVi);
    }

    /**
     * {@code DELETE  /rihanna-vis/:id} : delete the "id" rihannaVi.
     *
     * @param id the id of the rihannaVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRihannaVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RihannaVi : {}", id);
        rihannaViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

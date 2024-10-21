package ai.realworld.web.rest;

import ai.realworld.domain.EdSheeranVi;
import ai.realworld.repository.EdSheeranViRepository;
import ai.realworld.service.EdSheeranViQueryService;
import ai.realworld.service.EdSheeranViService;
import ai.realworld.service.criteria.EdSheeranViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.EdSheeranVi}.
 */
@RestController
@RequestMapping("/api/ed-sheeran-vis")
public class EdSheeranViResource {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranViResource.class);

    private static final String ENTITY_NAME = "edSheeranVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EdSheeranViService edSheeranViService;

    private final EdSheeranViRepository edSheeranViRepository;

    private final EdSheeranViQueryService edSheeranViQueryService;

    public EdSheeranViResource(
        EdSheeranViService edSheeranViService,
        EdSheeranViRepository edSheeranViRepository,
        EdSheeranViQueryService edSheeranViQueryService
    ) {
        this.edSheeranViService = edSheeranViService;
        this.edSheeranViRepository = edSheeranViRepository;
        this.edSheeranViQueryService = edSheeranViQueryService;
    }

    /**
     * {@code POST  /ed-sheeran-vis} : Create a new edSheeranVi.
     *
     * @param edSheeranVi the edSheeranVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new edSheeranVi, or with status {@code 400 (Bad Request)} if the edSheeranVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EdSheeranVi> createEdSheeranVi(@Valid @RequestBody EdSheeranVi edSheeranVi) throws URISyntaxException {
        LOG.debug("REST request to save EdSheeranVi : {}", edSheeranVi);
        if (edSheeranVi.getId() != null) {
            throw new BadRequestAlertException("A new edSheeranVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        edSheeranVi = edSheeranViService.save(edSheeranVi);
        return ResponseEntity.created(new URI("/api/ed-sheeran-vis/" + edSheeranVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, edSheeranVi.getId().toString()))
            .body(edSheeranVi);
    }

    /**
     * {@code PUT  /ed-sheeran-vis/:id} : Updates an existing edSheeranVi.
     *
     * @param id the id of the edSheeranVi to save.
     * @param edSheeranVi the edSheeranVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edSheeranVi,
     * or with status {@code 400 (Bad Request)} if the edSheeranVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the edSheeranVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EdSheeranVi> updateEdSheeranVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EdSheeranVi edSheeranVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update EdSheeranVi : {}, {}", id, edSheeranVi);
        if (edSheeranVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edSheeranVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edSheeranViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        edSheeranVi = edSheeranViService.update(edSheeranVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edSheeranVi.getId().toString()))
            .body(edSheeranVi);
    }

    /**
     * {@code PATCH  /ed-sheeran-vis/:id} : Partial updates given fields of an existing edSheeranVi, field will ignore if it is null
     *
     * @param id the id of the edSheeranVi to save.
     * @param edSheeranVi the edSheeranVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated edSheeranVi,
     * or with status {@code 400 (Bad Request)} if the edSheeranVi is not valid,
     * or with status {@code 404 (Not Found)} if the edSheeranVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the edSheeranVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EdSheeranVi> partialUpdateEdSheeranVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EdSheeranVi edSheeranVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EdSheeranVi partially : {}, {}", id, edSheeranVi);
        if (edSheeranVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, edSheeranVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!edSheeranViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EdSheeranVi> result = edSheeranViService.partialUpdate(edSheeranVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, edSheeranVi.getId().toString())
        );
    }

    /**
     * {@code GET  /ed-sheeran-vis} : get all the edSheeranVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of edSheeranVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EdSheeranVi>> getAllEdSheeranVis(
        EdSheeranViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EdSheeranVis by criteria: {}", criteria);

        Page<EdSheeranVi> page = edSheeranViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ed-sheeran-vis/count} : count all the edSheeranVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEdSheeranVis(EdSheeranViCriteria criteria) {
        LOG.debug("REST request to count EdSheeranVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(edSheeranViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ed-sheeran-vis/:id} : get the "id" edSheeranVi.
     *
     * @param id the id of the edSheeranVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the edSheeranVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EdSheeranVi> getEdSheeranVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EdSheeranVi : {}", id);
        Optional<EdSheeranVi> edSheeranVi = edSheeranViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(edSheeranVi);
    }

    /**
     * {@code DELETE  /ed-sheeran-vis/:id} : delete the "id" edSheeranVi.
     *
     * @param id the id of the edSheeranVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEdSheeranVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EdSheeranVi : {}", id);
        edSheeranViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

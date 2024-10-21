package ai.realworld.web.rest;

import ai.realworld.domain.AlPyuDjibrilVi;
import ai.realworld.repository.AlPyuDjibrilViRepository;
import ai.realworld.service.AlPyuDjibrilViQueryService;
import ai.realworld.service.AlPyuDjibrilViService;
import ai.realworld.service.criteria.AlPyuDjibrilViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlPyuDjibrilVi}.
 */
@RestController
@RequestMapping("/api/al-pyu-djibril-vis")
public class AlPyuDjibrilViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuDjibrilViResource.class);

    private static final String ENTITY_NAME = "alPyuDjibrilVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuDjibrilViService alPyuDjibrilViService;

    private final AlPyuDjibrilViRepository alPyuDjibrilViRepository;

    private final AlPyuDjibrilViQueryService alPyuDjibrilViQueryService;

    public AlPyuDjibrilViResource(
        AlPyuDjibrilViService alPyuDjibrilViService,
        AlPyuDjibrilViRepository alPyuDjibrilViRepository,
        AlPyuDjibrilViQueryService alPyuDjibrilViQueryService
    ) {
        this.alPyuDjibrilViService = alPyuDjibrilViService;
        this.alPyuDjibrilViRepository = alPyuDjibrilViRepository;
        this.alPyuDjibrilViQueryService = alPyuDjibrilViQueryService;
    }

    /**
     * {@code POST  /al-pyu-djibril-vis} : Create a new alPyuDjibrilVi.
     *
     * @param alPyuDjibrilVi the alPyuDjibrilVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuDjibrilVi, or with status {@code 400 (Bad Request)} if the alPyuDjibrilVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuDjibrilVi> createAlPyuDjibrilVi(@RequestBody AlPyuDjibrilVi alPyuDjibrilVi) throws URISyntaxException {
        LOG.debug("REST request to save AlPyuDjibrilVi : {}", alPyuDjibrilVi);
        if (alPyuDjibrilVi.getId() != null) {
            throw new BadRequestAlertException("A new alPyuDjibrilVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuDjibrilVi = alPyuDjibrilViService.save(alPyuDjibrilVi);
        return ResponseEntity.created(new URI("/api/al-pyu-djibril-vis/" + alPyuDjibrilVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilVi.getId().toString()))
            .body(alPyuDjibrilVi);
    }

    /**
     * {@code PUT  /al-pyu-djibril-vis/:id} : Updates an existing alPyuDjibrilVi.
     *
     * @param id the id of the alPyuDjibrilVi to save.
     * @param alPyuDjibrilVi the alPyuDjibrilVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuDjibrilVi,
     * or with status {@code 400 (Bad Request)} if the alPyuDjibrilVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuDjibrilVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuDjibrilVi> updateAlPyuDjibrilVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPyuDjibrilVi alPyuDjibrilVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuDjibrilVi : {}, {}", id, alPyuDjibrilVi);
        if (alPyuDjibrilVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuDjibrilVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuDjibrilViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuDjibrilVi = alPyuDjibrilViService.update(alPyuDjibrilVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilVi.getId().toString()))
            .body(alPyuDjibrilVi);
    }

    /**
     * {@code PATCH  /al-pyu-djibril-vis/:id} : Partial updates given fields of an existing alPyuDjibrilVi, field will ignore if it is null
     *
     * @param id the id of the alPyuDjibrilVi to save.
     * @param alPyuDjibrilVi the alPyuDjibrilVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuDjibrilVi,
     * or with status {@code 400 (Bad Request)} if the alPyuDjibrilVi is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuDjibrilVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuDjibrilVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuDjibrilVi> partialUpdateAlPyuDjibrilVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPyuDjibrilVi alPyuDjibrilVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuDjibrilVi partially : {}, {}", id, alPyuDjibrilVi);
        if (alPyuDjibrilVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuDjibrilVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuDjibrilViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuDjibrilVi> result = alPyuDjibrilViService.partialUpdate(alPyuDjibrilVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuDjibrilVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-djibril-vis} : get all the alPyuDjibrilVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuDjibrilVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuDjibrilVi>> getAllAlPyuDjibrilVis(
        AlPyuDjibrilViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuDjibrilVis by criteria: {}", criteria);

        Page<AlPyuDjibrilVi> page = alPyuDjibrilViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-djibril-vis/count} : count all the alPyuDjibrilVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuDjibrilVis(AlPyuDjibrilViCriteria criteria) {
        LOG.debug("REST request to count AlPyuDjibrilVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuDjibrilViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-djibril-vis/:id} : get the "id" alPyuDjibrilVi.
     *
     * @param id the id of the alPyuDjibrilVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuDjibrilVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuDjibrilVi> getAlPyuDjibrilVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPyuDjibrilVi : {}", id);
        Optional<AlPyuDjibrilVi> alPyuDjibrilVi = alPyuDjibrilViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuDjibrilVi);
    }

    /**
     * {@code DELETE  /al-pyu-djibril-vis/:id} : delete the "id" alPyuDjibrilVi.
     *
     * @param id the id of the alPyuDjibrilVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuDjibrilVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPyuDjibrilVi : {}", id);
        alPyuDjibrilViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

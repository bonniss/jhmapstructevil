package ai.realworld.web.rest;

import ai.realworld.domain.AlSherMaleVi;
import ai.realworld.repository.AlSherMaleViRepository;
import ai.realworld.service.AlSherMaleViQueryService;
import ai.realworld.service.AlSherMaleViService;
import ai.realworld.service.criteria.AlSherMaleViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlSherMaleVi}.
 */
@RestController
@RequestMapping("/api/al-sher-male-vis")
public class AlSherMaleViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleViResource.class);

    private static final String ENTITY_NAME = "alSherMaleVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlSherMaleViService alSherMaleViService;

    private final AlSherMaleViRepository alSherMaleViRepository;

    private final AlSherMaleViQueryService alSherMaleViQueryService;

    public AlSherMaleViResource(
        AlSherMaleViService alSherMaleViService,
        AlSherMaleViRepository alSherMaleViRepository,
        AlSherMaleViQueryService alSherMaleViQueryService
    ) {
        this.alSherMaleViService = alSherMaleViService;
        this.alSherMaleViRepository = alSherMaleViRepository;
        this.alSherMaleViQueryService = alSherMaleViQueryService;
    }

    /**
     * {@code POST  /al-sher-male-vis} : Create a new alSherMaleVi.
     *
     * @param alSherMaleVi the alSherMaleVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alSherMaleVi, or with status {@code 400 (Bad Request)} if the alSherMaleVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlSherMaleVi> createAlSherMaleVi(@RequestBody AlSherMaleVi alSherMaleVi) throws URISyntaxException {
        LOG.debug("REST request to save AlSherMaleVi : {}", alSherMaleVi);
        if (alSherMaleVi.getId() != null) {
            throw new BadRequestAlertException("A new alSherMaleVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alSherMaleVi = alSherMaleViService.save(alSherMaleVi);
        return ResponseEntity.created(new URI("/api/al-sher-male-vis/" + alSherMaleVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alSherMaleVi.getId().toString()))
            .body(alSherMaleVi);
    }

    /**
     * {@code PUT  /al-sher-male-vis/:id} : Updates an existing alSherMaleVi.
     *
     * @param id the id of the alSherMaleVi to save.
     * @param alSherMaleVi the alSherMaleVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alSherMaleVi,
     * or with status {@code 400 (Bad Request)} if the alSherMaleVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alSherMaleVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlSherMaleVi> updateAlSherMaleVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlSherMaleVi alSherMaleVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlSherMaleVi : {}, {}", id, alSherMaleVi);
        if (alSherMaleVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alSherMaleVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alSherMaleViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alSherMaleVi = alSherMaleViService.update(alSherMaleVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alSherMaleVi.getId().toString()))
            .body(alSherMaleVi);
    }

    /**
     * {@code PATCH  /al-sher-male-vis/:id} : Partial updates given fields of an existing alSherMaleVi, field will ignore if it is null
     *
     * @param id the id of the alSherMaleVi to save.
     * @param alSherMaleVi the alSherMaleVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alSherMaleVi,
     * or with status {@code 400 (Bad Request)} if the alSherMaleVi is not valid,
     * or with status {@code 404 (Not Found)} if the alSherMaleVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alSherMaleVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlSherMaleVi> partialUpdateAlSherMaleVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlSherMaleVi alSherMaleVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlSherMaleVi partially : {}, {}", id, alSherMaleVi);
        if (alSherMaleVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alSherMaleVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alSherMaleViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlSherMaleVi> result = alSherMaleViService.partialUpdate(alSherMaleVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alSherMaleVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-sher-male-vis} : get all the alSherMaleVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alSherMaleVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlSherMaleVi>> getAllAlSherMaleVis(
        AlSherMaleViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlSherMaleVis by criteria: {}", criteria);

        Page<AlSherMaleVi> page = alSherMaleViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-sher-male-vis/count} : count all the alSherMaleVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlSherMaleVis(AlSherMaleViCriteria criteria) {
        LOG.debug("REST request to count AlSherMaleVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alSherMaleViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-sher-male-vis/:id} : get the "id" alSherMaleVi.
     *
     * @param id the id of the alSherMaleVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alSherMaleVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlSherMaleVi> getAlSherMaleVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlSherMaleVi : {}", id);
        Optional<AlSherMaleVi> alSherMaleVi = alSherMaleViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alSherMaleVi);
    }

    /**
     * {@code DELETE  /al-sher-male-vis/:id} : delete the "id" alSherMaleVi.
     *
     * @param id the id of the alSherMaleVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlSherMaleVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlSherMaleVi : {}", id);
        alSherMaleViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

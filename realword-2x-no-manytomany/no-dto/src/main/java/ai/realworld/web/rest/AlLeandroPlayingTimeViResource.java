package ai.realworld.web.rest;

import ai.realworld.domain.AlLeandroPlayingTimeVi;
import ai.realworld.repository.AlLeandroPlayingTimeViRepository;
import ai.realworld.service.AlLeandroPlayingTimeViQueryService;
import ai.realworld.service.AlLeandroPlayingTimeViService;
import ai.realworld.service.criteria.AlLeandroPlayingTimeViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlLeandroPlayingTimeVi}.
 */
@RestController
@RequestMapping("/api/al-leandro-playing-time-vis")
public class AlLeandroPlayingTimeViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLeandroPlayingTimeViResource.class);

    private static final String ENTITY_NAME = "alLeandroPlayingTimeVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLeandroPlayingTimeViService alLeandroPlayingTimeViService;

    private final AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository;

    private final AlLeandroPlayingTimeViQueryService alLeandroPlayingTimeViQueryService;

    public AlLeandroPlayingTimeViResource(
        AlLeandroPlayingTimeViService alLeandroPlayingTimeViService,
        AlLeandroPlayingTimeViRepository alLeandroPlayingTimeViRepository,
        AlLeandroPlayingTimeViQueryService alLeandroPlayingTimeViQueryService
    ) {
        this.alLeandroPlayingTimeViService = alLeandroPlayingTimeViService;
        this.alLeandroPlayingTimeViRepository = alLeandroPlayingTimeViRepository;
        this.alLeandroPlayingTimeViQueryService = alLeandroPlayingTimeViQueryService;
    }

    /**
     * {@code POST  /al-leandro-playing-time-vis} : Create a new alLeandroPlayingTimeVi.
     *
     * @param alLeandroPlayingTimeVi the alLeandroPlayingTimeVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLeandroPlayingTimeVi, or with status {@code 400 (Bad Request)} if the alLeandroPlayingTimeVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLeandroPlayingTimeVi> createAlLeandroPlayingTimeVi(@RequestBody AlLeandroPlayingTimeVi alLeandroPlayingTimeVi)
        throws URISyntaxException {
        LOG.debug("REST request to save AlLeandroPlayingTimeVi : {}", alLeandroPlayingTimeVi);
        if (alLeandroPlayingTimeVi.getId() != null) {
            throw new BadRequestAlertException("A new alLeandroPlayingTimeVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLeandroPlayingTimeVi = alLeandroPlayingTimeViService.save(alLeandroPlayingTimeVi);
        return ResponseEntity.created(new URI("/api/al-leandro-playing-time-vis/" + alLeandroPlayingTimeVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTimeVi.getId().toString()))
            .body(alLeandroPlayingTimeVi);
    }

    /**
     * {@code PUT  /al-leandro-playing-time-vis/:id} : Updates an existing alLeandroPlayingTimeVi.
     *
     * @param id the id of the alLeandroPlayingTimeVi to save.
     * @param alLeandroPlayingTimeVi the alLeandroPlayingTimeVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandroPlayingTimeVi,
     * or with status {@code 400 (Bad Request)} if the alLeandroPlayingTimeVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLeandroPlayingTimeVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLeandroPlayingTimeVi> updateAlLeandroPlayingTimeVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlLeandroPlayingTimeVi alLeandroPlayingTimeVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLeandroPlayingTimeVi : {}, {}", id, alLeandroPlayingTimeVi);
        if (alLeandroPlayingTimeVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandroPlayingTimeVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroPlayingTimeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLeandroPlayingTimeVi = alLeandroPlayingTimeViService.update(alLeandroPlayingTimeVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTimeVi.getId().toString()))
            .body(alLeandroPlayingTimeVi);
    }

    /**
     * {@code PATCH  /al-leandro-playing-time-vis/:id} : Partial updates given fields of an existing alLeandroPlayingTimeVi, field will ignore if it is null
     *
     * @param id the id of the alLeandroPlayingTimeVi to save.
     * @param alLeandroPlayingTimeVi the alLeandroPlayingTimeVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLeandroPlayingTimeVi,
     * or with status {@code 400 (Bad Request)} if the alLeandroPlayingTimeVi is not valid,
     * or with status {@code 404 (Not Found)} if the alLeandroPlayingTimeVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLeandroPlayingTimeVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLeandroPlayingTimeVi> partialUpdateAlLeandroPlayingTimeVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlLeandroPlayingTimeVi alLeandroPlayingTimeVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLeandroPlayingTimeVi partially : {}, {}", id, alLeandroPlayingTimeVi);
        if (alLeandroPlayingTimeVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLeandroPlayingTimeVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLeandroPlayingTimeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLeandroPlayingTimeVi> result = alLeandroPlayingTimeViService.partialUpdate(alLeandroPlayingTimeVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLeandroPlayingTimeVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-leandro-playing-time-vis} : get all the alLeandroPlayingTimeVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLeandroPlayingTimeVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLeandroPlayingTimeVi>> getAllAlLeandroPlayingTimeVis(
        AlLeandroPlayingTimeViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLeandroPlayingTimeVis by criteria: {}", criteria);

        Page<AlLeandroPlayingTimeVi> page = alLeandroPlayingTimeViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-leandro-playing-time-vis/count} : count all the alLeandroPlayingTimeVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLeandroPlayingTimeVis(AlLeandroPlayingTimeViCriteria criteria) {
        LOG.debug("REST request to count AlLeandroPlayingTimeVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLeandroPlayingTimeViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-leandro-playing-time-vis/:id} : get the "id" alLeandroPlayingTimeVi.
     *
     * @param id the id of the alLeandroPlayingTimeVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLeandroPlayingTimeVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLeandroPlayingTimeVi> getAlLeandroPlayingTimeVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLeandroPlayingTimeVi : {}", id);
        Optional<AlLeandroPlayingTimeVi> alLeandroPlayingTimeVi = alLeandroPlayingTimeViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLeandroPlayingTimeVi);
    }

    /**
     * {@code DELETE  /al-leandro-playing-time-vis/:id} : delete the "id" alLeandroPlayingTimeVi.
     *
     * @param id the id of the alLeandroPlayingTimeVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLeandroPlayingTimeVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLeandroPlayingTimeVi : {}", id);
        alLeandroPlayingTimeViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

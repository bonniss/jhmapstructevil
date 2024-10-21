package ai.realworld.web.rest;

import ai.realworld.domain.AlBestTooth;
import ai.realworld.repository.AlBestToothRepository;
import ai.realworld.service.AlBestToothQueryService;
import ai.realworld.service.AlBestToothService;
import ai.realworld.service.criteria.AlBestToothCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlBestTooth}.
 */
@RestController
@RequestMapping("/api/al-best-tooths")
public class AlBestToothResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlBestToothResource.class);

    private static final String ENTITY_NAME = "alBestTooth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlBestToothService alBestToothService;

    private final AlBestToothRepository alBestToothRepository;

    private final AlBestToothQueryService alBestToothQueryService;

    public AlBestToothResource(
        AlBestToothService alBestToothService,
        AlBestToothRepository alBestToothRepository,
        AlBestToothQueryService alBestToothQueryService
    ) {
        this.alBestToothService = alBestToothService;
        this.alBestToothRepository = alBestToothRepository;
        this.alBestToothQueryService = alBestToothQueryService;
    }

    /**
     * {@code POST  /al-best-tooths} : Create a new alBestTooth.
     *
     * @param alBestTooth the alBestTooth to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alBestTooth, or with status {@code 400 (Bad Request)} if the alBestTooth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlBestTooth> createAlBestTooth(@Valid @RequestBody AlBestTooth alBestTooth) throws URISyntaxException {
        LOG.debug("REST request to save AlBestTooth : {}", alBestTooth);
        if (alBestTooth.getId() != null) {
            throw new BadRequestAlertException("A new alBestTooth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alBestTooth = alBestToothService.save(alBestTooth);
        return ResponseEntity.created(new URI("/api/al-best-tooths/" + alBestTooth.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alBestTooth.getId().toString()))
            .body(alBestTooth);
    }

    /**
     * {@code PUT  /al-best-tooths/:id} : Updates an existing alBestTooth.
     *
     * @param id the id of the alBestTooth to save.
     * @param alBestTooth the alBestTooth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBestTooth,
     * or with status {@code 400 (Bad Request)} if the alBestTooth is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alBestTooth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlBestTooth> updateAlBestTooth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlBestTooth alBestTooth
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlBestTooth : {}, {}", id, alBestTooth);
        if (alBestTooth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBestTooth.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBestToothRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alBestTooth = alBestToothService.update(alBestTooth);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBestTooth.getId().toString()))
            .body(alBestTooth);
    }

    /**
     * {@code PATCH  /al-best-tooths/:id} : Partial updates given fields of an existing alBestTooth, field will ignore if it is null
     *
     * @param id the id of the alBestTooth to save.
     * @param alBestTooth the alBestTooth to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alBestTooth,
     * or with status {@code 400 (Bad Request)} if the alBestTooth is not valid,
     * or with status {@code 404 (Not Found)} if the alBestTooth is not found,
     * or with status {@code 500 (Internal Server Error)} if the alBestTooth couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlBestTooth> partialUpdateAlBestTooth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlBestTooth alBestTooth
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlBestTooth partially : {}, {}", id, alBestTooth);
        if (alBestTooth.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alBestTooth.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alBestToothRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlBestTooth> result = alBestToothService.partialUpdate(alBestTooth);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alBestTooth.getId().toString())
        );
    }

    /**
     * {@code GET  /al-best-tooths} : get all the alBestTooths.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alBestTooths in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlBestTooth>> getAllAlBestTooths(
        AlBestToothCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlBestTooths by criteria: {}", criteria);

        Page<AlBestTooth> page = alBestToothQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-best-tooths/count} : count all the alBestTooths.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlBestTooths(AlBestToothCriteria criteria) {
        LOG.debug("REST request to count AlBestTooths by criteria: {}", criteria);
        return ResponseEntity.ok().body(alBestToothQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-best-tooths/:id} : get the "id" alBestTooth.
     *
     * @param id the id of the alBestTooth to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alBestTooth, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlBestTooth> getAlBestTooth(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlBestTooth : {}", id);
        Optional<AlBestTooth> alBestTooth = alBestToothService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alBestTooth);
    }

    /**
     * {@code DELETE  /al-best-tooths/:id} : delete the "id" alBestTooth.
     *
     * @param id the id of the alBestTooth to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlBestTooth(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlBestTooth : {}", id);
        alBestToothService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

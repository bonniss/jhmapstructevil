package ai.realworld.web.rest;

import ai.realworld.domain.AlPounderVi;
import ai.realworld.repository.AlPounderViRepository;
import ai.realworld.service.AlPounderViQueryService;
import ai.realworld.service.AlPounderViService;
import ai.realworld.service.criteria.AlPounderViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlPounderVi}.
 */
@RestController
@RequestMapping("/api/al-pounder-vis")
public class AlPounderViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderViResource.class);

    private static final String ENTITY_NAME = "alPounderVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPounderViService alPounderViService;

    private final AlPounderViRepository alPounderViRepository;

    private final AlPounderViQueryService alPounderViQueryService;

    public AlPounderViResource(
        AlPounderViService alPounderViService,
        AlPounderViRepository alPounderViRepository,
        AlPounderViQueryService alPounderViQueryService
    ) {
        this.alPounderViService = alPounderViService;
        this.alPounderViRepository = alPounderViRepository;
        this.alPounderViQueryService = alPounderViQueryService;
    }

    /**
     * {@code POST  /al-pounder-vis} : Create a new alPounderVi.
     *
     * @param alPounderVi the alPounderVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPounderVi, or with status {@code 400 (Bad Request)} if the alPounderVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPounderVi> createAlPounderVi(@Valid @RequestBody AlPounderVi alPounderVi) throws URISyntaxException {
        LOG.debug("REST request to save AlPounderVi : {}", alPounderVi);
        if (alPounderVi.getId() != null) {
            throw new BadRequestAlertException("A new alPounderVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPounderVi = alPounderViService.save(alPounderVi);
        return ResponseEntity.created(new URI("/api/al-pounder-vis/" + alPounderVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPounderVi.getId().toString()))
            .body(alPounderVi);
    }

    /**
     * {@code PUT  /al-pounder-vis/:id} : Updates an existing alPounderVi.
     *
     * @param id the id of the alPounderVi to save.
     * @param alPounderVi the alPounderVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPounderVi,
     * or with status {@code 400 (Bad Request)} if the alPounderVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPounderVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPounderVi> updateAlPounderVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPounderVi alPounderVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPounderVi : {}, {}", id, alPounderVi);
        if (alPounderVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPounderVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPounderViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPounderVi = alPounderViService.update(alPounderVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPounderVi.getId().toString()))
            .body(alPounderVi);
    }

    /**
     * {@code PATCH  /al-pounder-vis/:id} : Partial updates given fields of an existing alPounderVi, field will ignore if it is null
     *
     * @param id the id of the alPounderVi to save.
     * @param alPounderVi the alPounderVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPounderVi,
     * or with status {@code 400 (Bad Request)} if the alPounderVi is not valid,
     * or with status {@code 404 (Not Found)} if the alPounderVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPounderVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPounderVi> partialUpdateAlPounderVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPounderVi alPounderVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPounderVi partially : {}, {}", id, alPounderVi);
        if (alPounderVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPounderVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPounderViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPounderVi> result = alPounderViService.partialUpdate(alPounderVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPounderVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pounder-vis} : get all the alPounderVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPounderVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPounderVi>> getAllAlPounderVis(
        AlPounderViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPounderVis by criteria: {}", criteria);

        Page<AlPounderVi> page = alPounderViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pounder-vis/count} : count all the alPounderVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPounderVis(AlPounderViCriteria criteria) {
        LOG.debug("REST request to count AlPounderVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPounderViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pounder-vis/:id} : get the "id" alPounderVi.
     *
     * @param id the id of the alPounderVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPounderVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPounderVi> getAlPounderVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPounderVi : {}", id);
        Optional<AlPounderVi> alPounderVi = alPounderViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPounderVi);
    }

    /**
     * {@code DELETE  /al-pounder-vis/:id} : delete the "id" alPounderVi.
     *
     * @param id the id of the alPounderVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPounderVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPounderVi : {}", id);
        alPounderViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

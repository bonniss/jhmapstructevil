package ai.realworld.web.rest;

import ai.realworld.domain.AlMenityVi;
import ai.realworld.repository.AlMenityViRepository;
import ai.realworld.service.AlMenityViQueryService;
import ai.realworld.service.AlMenityViService;
import ai.realworld.service.criteria.AlMenityViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlMenityVi}.
 */
@RestController
@RequestMapping("/api/al-menity-vis")
public class AlMenityViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityViResource.class);

    private static final String ENTITY_NAME = "alMenityVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlMenityViService alMenityViService;

    private final AlMenityViRepository alMenityViRepository;

    private final AlMenityViQueryService alMenityViQueryService;

    public AlMenityViResource(
        AlMenityViService alMenityViService,
        AlMenityViRepository alMenityViRepository,
        AlMenityViQueryService alMenityViQueryService
    ) {
        this.alMenityViService = alMenityViService;
        this.alMenityViRepository = alMenityViRepository;
        this.alMenityViQueryService = alMenityViQueryService;
    }

    /**
     * {@code POST  /al-menity-vis} : Create a new alMenityVi.
     *
     * @param alMenityVi the alMenityVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alMenityVi, or with status {@code 400 (Bad Request)} if the alMenityVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlMenityVi> createAlMenityVi(@Valid @RequestBody AlMenityVi alMenityVi) throws URISyntaxException {
        LOG.debug("REST request to save AlMenityVi : {}", alMenityVi);
        if (alMenityVi.getId() != null) {
            throw new BadRequestAlertException("A new alMenityVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alMenityVi = alMenityViService.save(alMenityVi);
        return ResponseEntity.created(new URI("/api/al-menity-vis/" + alMenityVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alMenityVi.getId().toString()))
            .body(alMenityVi);
    }

    /**
     * {@code PUT  /al-menity-vis/:id} : Updates an existing alMenityVi.
     *
     * @param id the id of the alMenityVi to save.
     * @param alMenityVi the alMenityVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMenityVi,
     * or with status {@code 400 (Bad Request)} if the alMenityVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alMenityVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlMenityVi> updateAlMenityVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlMenityVi alMenityVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlMenityVi : {}, {}", id, alMenityVi);
        if (alMenityVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMenityVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMenityViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alMenityVi = alMenityViService.update(alMenityVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMenityVi.getId().toString()))
            .body(alMenityVi);
    }

    /**
     * {@code PATCH  /al-menity-vis/:id} : Partial updates given fields of an existing alMenityVi, field will ignore if it is null
     *
     * @param id the id of the alMenityVi to save.
     * @param alMenityVi the alMenityVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alMenityVi,
     * or with status {@code 400 (Bad Request)} if the alMenityVi is not valid,
     * or with status {@code 404 (Not Found)} if the alMenityVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alMenityVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlMenityVi> partialUpdateAlMenityVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlMenityVi alMenityVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlMenityVi partially : {}, {}", id, alMenityVi);
        if (alMenityVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alMenityVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alMenityViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlMenityVi> result = alMenityViService.partialUpdate(alMenityVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alMenityVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-menity-vis} : get all the alMenityVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alMenityVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlMenityVi>> getAllAlMenityVis(
        AlMenityViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlMenityVis by criteria: {}", criteria);

        Page<AlMenityVi> page = alMenityViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-menity-vis/count} : count all the alMenityVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlMenityVis(AlMenityViCriteria criteria) {
        LOG.debug("REST request to count AlMenityVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alMenityViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-menity-vis/:id} : get the "id" alMenityVi.
     *
     * @param id the id of the alMenityVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alMenityVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlMenityVi> getAlMenityVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlMenityVi : {}", id);
        Optional<AlMenityVi> alMenityVi = alMenityViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alMenityVi);
    }

    /**
     * {@code DELETE  /al-menity-vis/:id} : delete the "id" alMenityVi.
     *
     * @param id the id of the alMenityVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlMenityVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlMenityVi : {}", id);
        alMenityViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

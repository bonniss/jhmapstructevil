package ai.realworld.web.rest;

import ai.realworld.domain.AlGoreVi;
import ai.realworld.repository.AlGoreViRepository;
import ai.realworld.service.AlGoreViQueryService;
import ai.realworld.service.AlGoreViService;
import ai.realworld.service.criteria.AlGoreViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlGoreVi}.
 */
@RestController
@RequestMapping("/api/al-gore-vis")
public class AlGoreViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreViResource.class);

    private static final String ENTITY_NAME = "alGoreVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoreViService alGoreViService;

    private final AlGoreViRepository alGoreViRepository;

    private final AlGoreViQueryService alGoreViQueryService;

    public AlGoreViResource(
        AlGoreViService alGoreViService,
        AlGoreViRepository alGoreViRepository,
        AlGoreViQueryService alGoreViQueryService
    ) {
        this.alGoreViService = alGoreViService;
        this.alGoreViRepository = alGoreViRepository;
        this.alGoreViQueryService = alGoreViQueryService;
    }

    /**
     * {@code POST  /al-gore-vis} : Create a new alGoreVi.
     *
     * @param alGoreVi the alGoreVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoreVi, or with status {@code 400 (Bad Request)} if the alGoreVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoreVi> createAlGoreVi(@Valid @RequestBody AlGoreVi alGoreVi) throws URISyntaxException {
        LOG.debug("REST request to save AlGoreVi : {}", alGoreVi);
        if (alGoreVi.getId() != null) {
            throw new BadRequestAlertException("A new alGoreVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoreVi = alGoreViService.save(alGoreVi);
        return ResponseEntity.created(new URI("/api/al-gore-vis/" + alGoreVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoreVi.getId().toString()))
            .body(alGoreVi);
    }

    /**
     * {@code PUT  /al-gore-vis/:id} : Updates an existing alGoreVi.
     *
     * @param id the id of the alGoreVi to save.
     * @param alGoreVi the alGoreVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreVi,
     * or with status {@code 400 (Bad Request)} if the alGoreVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoreVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoreVi> updateAlGoreVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlGoreVi alGoreVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGoreVi : {}, {}", id, alGoreVi);
        if (alGoreVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoreVi = alGoreViService.update(alGoreVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreVi.getId().toString()))
            .body(alGoreVi);
    }

    /**
     * {@code PATCH  /al-gore-vis/:id} : Partial updates given fields of an existing alGoreVi, field will ignore if it is null
     *
     * @param id the id of the alGoreVi to save.
     * @param alGoreVi the alGoreVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreVi,
     * or with status {@code 400 (Bad Request)} if the alGoreVi is not valid,
     * or with status {@code 404 (Not Found)} if the alGoreVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoreVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoreVi> partialUpdateAlGoreVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlGoreVi alGoreVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGoreVi partially : {}, {}", id, alGoreVi);
        if (alGoreVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoreVi> result = alGoreViService.partialUpdate(alGoreVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-gore-vis} : get all the alGoreVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGoreVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoreVi>> getAllAlGoreVis(
        AlGoreViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGoreVis by criteria: {}", criteria);

        Page<AlGoreVi> page = alGoreViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-gore-vis/count} : count all the alGoreVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGoreVis(AlGoreViCriteria criteria) {
        LOG.debug("REST request to count AlGoreVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoreViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-gore-vis/:id} : get the "id" alGoreVi.
     *
     * @param id the id of the alGoreVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoreVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoreVi> getAlGoreVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlGoreVi : {}", id);
        Optional<AlGoreVi> alGoreVi = alGoreViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoreVi);
    }

    /**
     * {@code DELETE  /al-gore-vis/:id} : delete the "id" alGoreVi.
     *
     * @param id the id of the alGoreVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGoreVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlGoreVi : {}", id);
        alGoreViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

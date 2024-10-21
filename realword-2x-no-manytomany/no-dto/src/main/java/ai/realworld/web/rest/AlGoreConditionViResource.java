package ai.realworld.web.rest;

import ai.realworld.domain.AlGoreConditionVi;
import ai.realworld.repository.AlGoreConditionViRepository;
import ai.realworld.service.AlGoreConditionViQueryService;
import ai.realworld.service.AlGoreConditionViService;
import ai.realworld.service.criteria.AlGoreConditionViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlGoreConditionVi}.
 */
@RestController
@RequestMapping("/api/al-gore-condition-vis")
public class AlGoreConditionViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreConditionViResource.class);

    private static final String ENTITY_NAME = "alGoreConditionVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoreConditionViService alGoreConditionViService;

    private final AlGoreConditionViRepository alGoreConditionViRepository;

    private final AlGoreConditionViQueryService alGoreConditionViQueryService;

    public AlGoreConditionViResource(
        AlGoreConditionViService alGoreConditionViService,
        AlGoreConditionViRepository alGoreConditionViRepository,
        AlGoreConditionViQueryService alGoreConditionViQueryService
    ) {
        this.alGoreConditionViService = alGoreConditionViService;
        this.alGoreConditionViRepository = alGoreConditionViRepository;
        this.alGoreConditionViQueryService = alGoreConditionViQueryService;
    }

    /**
     * {@code POST  /al-gore-condition-vis} : Create a new alGoreConditionVi.
     *
     * @param alGoreConditionVi the alGoreConditionVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGoreConditionVi, or with status {@code 400 (Bad Request)} if the alGoreConditionVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGoreConditionVi> createAlGoreConditionVi(@Valid @RequestBody AlGoreConditionVi alGoreConditionVi)
        throws URISyntaxException {
        LOG.debug("REST request to save AlGoreConditionVi : {}", alGoreConditionVi);
        if (alGoreConditionVi.getId() != null) {
            throw new BadRequestAlertException("A new alGoreConditionVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGoreConditionVi = alGoreConditionViService.save(alGoreConditionVi);
        return ResponseEntity.created(new URI("/api/al-gore-condition-vis/" + alGoreConditionVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGoreConditionVi.getId().toString()))
            .body(alGoreConditionVi);
    }

    /**
     * {@code PUT  /al-gore-condition-vis/:id} : Updates an existing alGoreConditionVi.
     *
     * @param id the id of the alGoreConditionVi to save.
     * @param alGoreConditionVi the alGoreConditionVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreConditionVi,
     * or with status {@code 400 (Bad Request)} if the alGoreConditionVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGoreConditionVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGoreConditionVi> updateAlGoreConditionVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlGoreConditionVi alGoreConditionVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGoreConditionVi : {}, {}", id, alGoreConditionVi);
        if (alGoreConditionVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreConditionVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreConditionViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGoreConditionVi = alGoreConditionViService.update(alGoreConditionVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreConditionVi.getId().toString()))
            .body(alGoreConditionVi);
    }

    /**
     * {@code PATCH  /al-gore-condition-vis/:id} : Partial updates given fields of an existing alGoreConditionVi, field will ignore if it is null
     *
     * @param id the id of the alGoreConditionVi to save.
     * @param alGoreConditionVi the alGoreConditionVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGoreConditionVi,
     * or with status {@code 400 (Bad Request)} if the alGoreConditionVi is not valid,
     * or with status {@code 404 (Not Found)} if the alGoreConditionVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGoreConditionVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGoreConditionVi> partialUpdateAlGoreConditionVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlGoreConditionVi alGoreConditionVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGoreConditionVi partially : {}, {}", id, alGoreConditionVi);
        if (alGoreConditionVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGoreConditionVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreConditionViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGoreConditionVi> result = alGoreConditionViService.partialUpdate(alGoreConditionVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGoreConditionVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-gore-condition-vis} : get all the alGoreConditionVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGoreConditionVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGoreConditionVi>> getAllAlGoreConditionVis(
        AlGoreConditionViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGoreConditionVis by criteria: {}", criteria);

        Page<AlGoreConditionVi> page = alGoreConditionViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-gore-condition-vis/count} : count all the alGoreConditionVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGoreConditionVis(AlGoreConditionViCriteria criteria) {
        LOG.debug("REST request to count AlGoreConditionVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoreConditionViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-gore-condition-vis/:id} : get the "id" alGoreConditionVi.
     *
     * @param id the id of the alGoreConditionVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGoreConditionVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGoreConditionVi> getAlGoreConditionVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlGoreConditionVi : {}", id);
        Optional<AlGoreConditionVi> alGoreConditionVi = alGoreConditionViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGoreConditionVi);
    }

    /**
     * {@code DELETE  /al-gore-condition-vis/:id} : delete the "id" alGoreConditionVi.
     *
     * @param id the id of the alGoreConditionVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGoreConditionVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlGoreConditionVi : {}", id);
        alGoreConditionViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

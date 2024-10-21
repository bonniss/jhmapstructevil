package ai.realworld.web.rest;

import ai.realworld.domain.AlPyuThomasWayneVi;
import ai.realworld.repository.AlPyuThomasWayneViRepository;
import ai.realworld.service.AlPyuThomasWayneViQueryService;
import ai.realworld.service.AlPyuThomasWayneViService;
import ai.realworld.service.criteria.AlPyuThomasWayneViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlPyuThomasWayneVi}.
 */
@RestController
@RequestMapping("/api/al-pyu-thomas-wayne-vis")
public class AlPyuThomasWayneViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneViResource.class);

    private static final String ENTITY_NAME = "alPyuThomasWayneVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuThomasWayneViService alPyuThomasWayneViService;

    private final AlPyuThomasWayneViRepository alPyuThomasWayneViRepository;

    private final AlPyuThomasWayneViQueryService alPyuThomasWayneViQueryService;

    public AlPyuThomasWayneViResource(
        AlPyuThomasWayneViService alPyuThomasWayneViService,
        AlPyuThomasWayneViRepository alPyuThomasWayneViRepository,
        AlPyuThomasWayneViQueryService alPyuThomasWayneViQueryService
    ) {
        this.alPyuThomasWayneViService = alPyuThomasWayneViService;
        this.alPyuThomasWayneViRepository = alPyuThomasWayneViRepository;
        this.alPyuThomasWayneViQueryService = alPyuThomasWayneViQueryService;
    }

    /**
     * {@code POST  /al-pyu-thomas-wayne-vis} : Create a new alPyuThomasWayneVi.
     *
     * @param alPyuThomasWayneVi the alPyuThomasWayneVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuThomasWayneVi, or with status {@code 400 (Bad Request)} if the alPyuThomasWayneVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuThomasWayneVi> createAlPyuThomasWayneVi(@Valid @RequestBody AlPyuThomasWayneVi alPyuThomasWayneVi)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPyuThomasWayneVi : {}", alPyuThomasWayneVi);
        if (alPyuThomasWayneVi.getId() != null) {
            throw new BadRequestAlertException("A new alPyuThomasWayneVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuThomasWayneVi = alPyuThomasWayneViService.save(alPyuThomasWayneVi);
        return ResponseEntity.created(new URI("/api/al-pyu-thomas-wayne-vis/" + alPyuThomasWayneVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuThomasWayneVi.getId().toString()))
            .body(alPyuThomasWayneVi);
    }

    /**
     * {@code PUT  /al-pyu-thomas-wayne-vis/:id} : Updates an existing alPyuThomasWayneVi.
     *
     * @param id the id of the alPyuThomasWayneVi to save.
     * @param alPyuThomasWayneVi the alPyuThomasWayneVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuThomasWayneVi,
     * or with status {@code 400 (Bad Request)} if the alPyuThomasWayneVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuThomasWayneVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuThomasWayneVi> updateAlPyuThomasWayneVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPyuThomasWayneVi alPyuThomasWayneVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuThomasWayneVi : {}, {}", id, alPyuThomasWayneVi);
        if (alPyuThomasWayneVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuThomasWayneVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuThomasWayneViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuThomasWayneVi = alPyuThomasWayneViService.update(alPyuThomasWayneVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuThomasWayneVi.getId().toString()))
            .body(alPyuThomasWayneVi);
    }

    /**
     * {@code PATCH  /al-pyu-thomas-wayne-vis/:id} : Partial updates given fields of an existing alPyuThomasWayneVi, field will ignore if it is null
     *
     * @param id the id of the alPyuThomasWayneVi to save.
     * @param alPyuThomasWayneVi the alPyuThomasWayneVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuThomasWayneVi,
     * or with status {@code 400 (Bad Request)} if the alPyuThomasWayneVi is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuThomasWayneVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuThomasWayneVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuThomasWayneVi> partialUpdateAlPyuThomasWayneVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPyuThomasWayneVi alPyuThomasWayneVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuThomasWayneVi partially : {}, {}", id, alPyuThomasWayneVi);
        if (alPyuThomasWayneVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuThomasWayneVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuThomasWayneViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuThomasWayneVi> result = alPyuThomasWayneViService.partialUpdate(alPyuThomasWayneVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuThomasWayneVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-thomas-wayne-vis} : get all the alPyuThomasWayneVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuThomasWayneVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuThomasWayneVi>> getAllAlPyuThomasWayneVis(
        AlPyuThomasWayneViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuThomasWayneVis by criteria: {}", criteria);

        Page<AlPyuThomasWayneVi> page = alPyuThomasWayneViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-thomas-wayne-vis/count} : count all the alPyuThomasWayneVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuThomasWayneVis(AlPyuThomasWayneViCriteria criteria) {
        LOG.debug("REST request to count AlPyuThomasWayneVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuThomasWayneViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-thomas-wayne-vis/:id} : get the "id" alPyuThomasWayneVi.
     *
     * @param id the id of the alPyuThomasWayneVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuThomasWayneVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuThomasWayneVi> getAlPyuThomasWayneVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPyuThomasWayneVi : {}", id);
        Optional<AlPyuThomasWayneVi> alPyuThomasWayneVi = alPyuThomasWayneViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuThomasWayneVi);
    }

    /**
     * {@code DELETE  /al-pyu-thomas-wayne-vis/:id} : delete the "id" alPyuThomasWayneVi.
     *
     * @param id the id of the alPyuThomasWayneVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuThomasWayneVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPyuThomasWayneVi : {}", id);
        alPyuThomasWayneViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

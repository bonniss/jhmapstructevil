package ai.realworld.web.rest;

import ai.realworld.domain.AlAppleVi;
import ai.realworld.repository.AlAppleViRepository;
import ai.realworld.service.AlAppleViQueryService;
import ai.realworld.service.AlAppleViService;
import ai.realworld.service.criteria.AlAppleViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link ai.realworld.domain.AlAppleVi}.
 */
@RestController
@RequestMapping("/api/al-apple-vis")
public class AlAppleViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleViResource.class);

    private static final String ENTITY_NAME = "alAppleVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlAppleViService alAppleViService;

    private final AlAppleViRepository alAppleViRepository;

    private final AlAppleViQueryService alAppleViQueryService;

    public AlAppleViResource(
        AlAppleViService alAppleViService,
        AlAppleViRepository alAppleViRepository,
        AlAppleViQueryService alAppleViQueryService
    ) {
        this.alAppleViService = alAppleViService;
        this.alAppleViRepository = alAppleViRepository;
        this.alAppleViQueryService = alAppleViQueryService;
    }

    /**
     * {@code POST  /al-apple-vis} : Create a new alAppleVi.
     *
     * @param alAppleVi the alAppleVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alAppleVi, or with status {@code 400 (Bad Request)} if the alAppleVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlAppleVi> createAlAppleVi(@Valid @RequestBody AlAppleVi alAppleVi) throws URISyntaxException {
        LOG.debug("REST request to save AlAppleVi : {}", alAppleVi);
        if (alAppleVi.getId() != null) {
            throw new BadRequestAlertException("A new alAppleVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alAppleVi = alAppleViService.save(alAppleVi);
        return ResponseEntity.created(new URI("/api/al-apple-vis/" + alAppleVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alAppleVi.getId().toString()))
            .body(alAppleVi);
    }

    /**
     * {@code PUT  /al-apple-vis/:id} : Updates an existing alAppleVi.
     *
     * @param id the id of the alAppleVi to save.
     * @param alAppleVi the alAppleVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAppleVi,
     * or with status {@code 400 (Bad Request)} if the alAppleVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alAppleVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlAppleVi> updateAlAppleVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlAppleVi alAppleVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlAppleVi : {}, {}", id, alAppleVi);
        if (alAppleVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAppleVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAppleViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alAppleVi = alAppleViService.update(alAppleVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAppleVi.getId().toString()))
            .body(alAppleVi);
    }

    /**
     * {@code PATCH  /al-apple-vis/:id} : Partial updates given fields of an existing alAppleVi, field will ignore if it is null
     *
     * @param id the id of the alAppleVi to save.
     * @param alAppleVi the alAppleVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAppleVi,
     * or with status {@code 400 (Bad Request)} if the alAppleVi is not valid,
     * or with status {@code 404 (Not Found)} if the alAppleVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alAppleVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlAppleVi> partialUpdateAlAppleVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlAppleVi alAppleVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlAppleVi partially : {}, {}", id, alAppleVi);
        if (alAppleVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAppleVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAppleViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlAppleVi> result = alAppleViService.partialUpdate(alAppleVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAppleVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-apple-vis} : get all the alAppleVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alAppleVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlAppleVi>> getAllAlAppleVis(
        AlAppleViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlAppleVis by criteria: {}", criteria);

        Page<AlAppleVi> page = alAppleViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-apple-vis/count} : count all the alAppleVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlAppleVis(AlAppleViCriteria criteria) {
        LOG.debug("REST request to count AlAppleVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alAppleViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-apple-vis/:id} : get the "id" alAppleVi.
     *
     * @param id the id of the alAppleVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alAppleVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlAppleVi> getAlAppleVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlAppleVi : {}", id);
        Optional<AlAppleVi> alAppleVi = alAppleViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alAppleVi);
    }

    /**
     * {@code DELETE  /al-apple-vis/:id} : delete the "id" alAppleVi.
     *
     * @param id the id of the alAppleVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlAppleVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlAppleVi : {}", id);
        alAppleViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

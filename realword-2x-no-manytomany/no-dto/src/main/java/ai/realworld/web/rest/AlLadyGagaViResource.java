package ai.realworld.web.rest;

import ai.realworld.domain.AlLadyGagaVi;
import ai.realworld.repository.AlLadyGagaViRepository;
import ai.realworld.service.AlLadyGagaViQueryService;
import ai.realworld.service.AlLadyGagaViService;
import ai.realworld.service.criteria.AlLadyGagaViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlLadyGagaVi}.
 */
@RestController
@RequestMapping("/api/al-lady-gaga-vis")
public class AlLadyGagaViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaViResource.class);

    private static final String ENTITY_NAME = "alLadyGagaVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLadyGagaViService alLadyGagaViService;

    private final AlLadyGagaViRepository alLadyGagaViRepository;

    private final AlLadyGagaViQueryService alLadyGagaViQueryService;

    public AlLadyGagaViResource(
        AlLadyGagaViService alLadyGagaViService,
        AlLadyGagaViRepository alLadyGagaViRepository,
        AlLadyGagaViQueryService alLadyGagaViQueryService
    ) {
        this.alLadyGagaViService = alLadyGagaViService;
        this.alLadyGagaViRepository = alLadyGagaViRepository;
        this.alLadyGagaViQueryService = alLadyGagaViQueryService;
    }

    /**
     * {@code POST  /al-lady-gaga-vis} : Create a new alLadyGagaVi.
     *
     * @param alLadyGagaVi the alLadyGagaVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLadyGagaVi, or with status {@code 400 (Bad Request)} if the alLadyGagaVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLadyGagaVi> createAlLadyGagaVi(@Valid @RequestBody AlLadyGagaVi alLadyGagaVi) throws URISyntaxException {
        LOG.debug("REST request to save AlLadyGagaVi : {}", alLadyGagaVi);
        if (alLadyGagaVi.getId() != null) {
            throw new BadRequestAlertException("A new alLadyGagaVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLadyGagaVi = alLadyGagaViService.save(alLadyGagaVi);
        return ResponseEntity.created(new URI("/api/al-lady-gaga-vis/" + alLadyGagaVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLadyGagaVi.getId().toString()))
            .body(alLadyGagaVi);
    }

    /**
     * {@code PUT  /al-lady-gaga-vis/:id} : Updates an existing alLadyGagaVi.
     *
     * @param id the id of the alLadyGagaVi to save.
     * @param alLadyGagaVi the alLadyGagaVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLadyGagaVi,
     * or with status {@code 400 (Bad Request)} if the alLadyGagaVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLadyGagaVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLadyGagaVi> updateAlLadyGagaVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlLadyGagaVi alLadyGagaVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLadyGagaVi : {}, {}", id, alLadyGagaVi);
        if (alLadyGagaVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLadyGagaVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLadyGagaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLadyGagaVi = alLadyGagaViService.update(alLadyGagaVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLadyGagaVi.getId().toString()))
            .body(alLadyGagaVi);
    }

    /**
     * {@code PATCH  /al-lady-gaga-vis/:id} : Partial updates given fields of an existing alLadyGagaVi, field will ignore if it is null
     *
     * @param id the id of the alLadyGagaVi to save.
     * @param alLadyGagaVi the alLadyGagaVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLadyGagaVi,
     * or with status {@code 400 (Bad Request)} if the alLadyGagaVi is not valid,
     * or with status {@code 404 (Not Found)} if the alLadyGagaVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLadyGagaVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLadyGagaVi> partialUpdateAlLadyGagaVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlLadyGagaVi alLadyGagaVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLadyGagaVi partially : {}, {}", id, alLadyGagaVi);
        if (alLadyGagaVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLadyGagaVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLadyGagaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLadyGagaVi> result = alLadyGagaViService.partialUpdate(alLadyGagaVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLadyGagaVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-lady-gaga-vis} : get all the alLadyGagaVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLadyGagaVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLadyGagaVi>> getAllAlLadyGagaVis(
        AlLadyGagaViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLadyGagaVis by criteria: {}", criteria);

        Page<AlLadyGagaVi> page = alLadyGagaViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-lady-gaga-vis/count} : count all the alLadyGagaVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLadyGagaVis(AlLadyGagaViCriteria criteria) {
        LOG.debug("REST request to count AlLadyGagaVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLadyGagaViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-lady-gaga-vis/:id} : get the "id" alLadyGagaVi.
     *
     * @param id the id of the alLadyGagaVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLadyGagaVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLadyGagaVi> getAlLadyGagaVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLadyGagaVi : {}", id);
        Optional<AlLadyGagaVi> alLadyGagaVi = alLadyGagaViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLadyGagaVi);
    }

    /**
     * {@code DELETE  /al-lady-gaga-vis/:id} : delete the "id" alLadyGagaVi.
     *
     * @param id the id of the alLadyGagaVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLadyGagaVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLadyGagaVi : {}", id);
        alLadyGagaViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

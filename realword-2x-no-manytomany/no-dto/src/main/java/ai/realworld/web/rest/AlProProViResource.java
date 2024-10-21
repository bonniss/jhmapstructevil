package ai.realworld.web.rest;

import ai.realworld.domain.AlProProVi;
import ai.realworld.repository.AlProProViRepository;
import ai.realworld.service.AlProProViQueryService;
import ai.realworld.service.AlProProViService;
import ai.realworld.service.criteria.AlProProViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlProProVi}.
 */
@RestController
@RequestMapping("/api/al-pro-pro-vis")
public class AlProProViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProViResource.class);

    private static final String ENTITY_NAME = "alProProVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlProProViService alProProViService;

    private final AlProProViRepository alProProViRepository;

    private final AlProProViQueryService alProProViQueryService;

    public AlProProViResource(
        AlProProViService alProProViService,
        AlProProViRepository alProProViRepository,
        AlProProViQueryService alProProViQueryService
    ) {
        this.alProProViService = alProProViService;
        this.alProProViRepository = alProProViRepository;
        this.alProProViQueryService = alProProViQueryService;
    }

    /**
     * {@code POST  /al-pro-pro-vis} : Create a new alProProVi.
     *
     * @param alProProVi the alProProVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alProProVi, or with status {@code 400 (Bad Request)} if the alProProVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlProProVi> createAlProProVi(@Valid @RequestBody AlProProVi alProProVi) throws URISyntaxException {
        LOG.debug("REST request to save AlProProVi : {}", alProProVi);
        if (alProProVi.getId() != null) {
            throw new BadRequestAlertException("A new alProProVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alProProVi = alProProViService.save(alProProVi);
        return ResponseEntity.created(new URI("/api/al-pro-pro-vis/" + alProProVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alProProVi.getId().toString()))
            .body(alProProVi);
    }

    /**
     * {@code PUT  /al-pro-pro-vis/:id} : Updates an existing alProProVi.
     *
     * @param id the id of the alProProVi to save.
     * @param alProProVi the alProProVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProProVi,
     * or with status {@code 400 (Bad Request)} if the alProProVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alProProVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlProProVi> updateAlProProVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlProProVi alProProVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlProProVi : {}, {}", id, alProProVi);
        if (alProProVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProProVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProProViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alProProVi = alProProViService.update(alProProVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProProVi.getId().toString()))
            .body(alProProVi);
    }

    /**
     * {@code PATCH  /al-pro-pro-vis/:id} : Partial updates given fields of an existing alProProVi, field will ignore if it is null
     *
     * @param id the id of the alProProVi to save.
     * @param alProProVi the alProProVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProProVi,
     * or with status {@code 400 (Bad Request)} if the alProProVi is not valid,
     * or with status {@code 404 (Not Found)} if the alProProVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alProProVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlProProVi> partialUpdateAlProProVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlProProVi alProProVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlProProVi partially : {}, {}", id, alProProVi);
        if (alProProVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProProVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProProViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlProProVi> result = alProProViService.partialUpdate(alProProVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProProVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pro-pro-vis} : get all the alProProVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alProProVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlProProVi>> getAllAlProProVis(
        AlProProViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlProProVis by criteria: {}", criteria);

        Page<AlProProVi> page = alProProViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pro-pro-vis/count} : count all the alProProVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlProProVis(AlProProViCriteria criteria) {
        LOG.debug("REST request to count AlProProVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alProProViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pro-pro-vis/:id} : get the "id" alProProVi.
     *
     * @param id the id of the alProProVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alProProVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlProProVi> getAlProProVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlProProVi : {}", id);
        Optional<AlProProVi> alProProVi = alProProViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alProProVi);
    }

    /**
     * {@code DELETE  /al-pro-pro-vis/:id} : delete the "id" alProProVi.
     *
     * @param id the id of the alProProVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlProProVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlProProVi : {}", id);
        alProProViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package ai.realworld.web.rest;

import ai.realworld.domain.AlZorroTemptationVi;
import ai.realworld.repository.AlZorroTemptationViRepository;
import ai.realworld.service.AlZorroTemptationViQueryService;
import ai.realworld.service.AlZorroTemptationViService;
import ai.realworld.service.criteria.AlZorroTemptationViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlZorroTemptationVi}.
 */
@RestController
@RequestMapping("/api/al-zorro-temptation-vis")
public class AlZorroTemptationViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlZorroTemptationViResource.class);

    private static final String ENTITY_NAME = "alZorroTemptationVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlZorroTemptationViService alZorroTemptationViService;

    private final AlZorroTemptationViRepository alZorroTemptationViRepository;

    private final AlZorroTemptationViQueryService alZorroTemptationViQueryService;

    public AlZorroTemptationViResource(
        AlZorroTemptationViService alZorroTemptationViService,
        AlZorroTemptationViRepository alZorroTemptationViRepository,
        AlZorroTemptationViQueryService alZorroTemptationViQueryService
    ) {
        this.alZorroTemptationViService = alZorroTemptationViService;
        this.alZorroTemptationViRepository = alZorroTemptationViRepository;
        this.alZorroTemptationViQueryService = alZorroTemptationViQueryService;
    }

    /**
     * {@code POST  /al-zorro-temptation-vis} : Create a new alZorroTemptationVi.
     *
     * @param alZorroTemptationVi the alZorroTemptationVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alZorroTemptationVi, or with status {@code 400 (Bad Request)} if the alZorroTemptationVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlZorroTemptationVi> createAlZorroTemptationVi(@Valid @RequestBody AlZorroTemptationVi alZorroTemptationVi)
        throws URISyntaxException {
        LOG.debug("REST request to save AlZorroTemptationVi : {}", alZorroTemptationVi);
        if (alZorroTemptationVi.getId() != null) {
            throw new BadRequestAlertException("A new alZorroTemptationVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alZorroTemptationVi = alZorroTemptationViService.save(alZorroTemptationVi);
        return ResponseEntity.created(new URI("/api/al-zorro-temptation-vis/" + alZorroTemptationVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alZorroTemptationVi.getId().toString()))
            .body(alZorroTemptationVi);
    }

    /**
     * {@code PUT  /al-zorro-temptation-vis/:id} : Updates an existing alZorroTemptationVi.
     *
     * @param id the id of the alZorroTemptationVi to save.
     * @param alZorroTemptationVi the alZorroTemptationVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptationVi,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptationVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptationVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlZorroTemptationVi> updateAlZorroTemptationVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlZorroTemptationVi alZorroTemptationVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlZorroTemptationVi : {}, {}", id, alZorroTemptationVi);
        if (alZorroTemptationVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptationVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alZorroTemptationVi = alZorroTemptationViService.update(alZorroTemptationVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptationVi.getId().toString()))
            .body(alZorroTemptationVi);
    }

    /**
     * {@code PATCH  /al-zorro-temptation-vis/:id} : Partial updates given fields of an existing alZorroTemptationVi, field will ignore if it is null
     *
     * @param id the id of the alZorroTemptationVi to save.
     * @param alZorroTemptationVi the alZorroTemptationVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alZorroTemptationVi,
     * or with status {@code 400 (Bad Request)} if the alZorroTemptationVi is not valid,
     * or with status {@code 404 (Not Found)} if the alZorroTemptationVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alZorroTemptationVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlZorroTemptationVi> partialUpdateAlZorroTemptationVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlZorroTemptationVi alZorroTemptationVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlZorroTemptationVi partially : {}, {}", id, alZorroTemptationVi);
        if (alZorroTemptationVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alZorroTemptationVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alZorroTemptationViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlZorroTemptationVi> result = alZorroTemptationViService.partialUpdate(alZorroTemptationVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alZorroTemptationVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-zorro-temptation-vis} : get all the alZorroTemptationVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alZorroTemptationVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlZorroTemptationVi>> getAllAlZorroTemptationVis(
        AlZorroTemptationViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlZorroTemptationVis by criteria: {}", criteria);

        Page<AlZorroTemptationVi> page = alZorroTemptationViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-zorro-temptation-vis/count} : count all the alZorroTemptationVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlZorroTemptationVis(AlZorroTemptationViCriteria criteria) {
        LOG.debug("REST request to count AlZorroTemptationVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alZorroTemptationViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-zorro-temptation-vis/:id} : get the "id" alZorroTemptationVi.
     *
     * @param id the id of the alZorroTemptationVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alZorroTemptationVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlZorroTemptationVi> getAlZorroTemptationVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlZorroTemptationVi : {}", id);
        Optional<AlZorroTemptationVi> alZorroTemptationVi = alZorroTemptationViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alZorroTemptationVi);
    }

    /**
     * {@code DELETE  /al-zorro-temptation-vis/:id} : delete the "id" alZorroTemptationVi.
     *
     * @param id the id of the alZorroTemptationVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlZorroTemptationVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlZorroTemptationVi : {}", id);
        alZorroTemptationViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

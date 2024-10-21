package ai.realworld.web.rest;

import ai.realworld.domain.AlCatalinaVi;
import ai.realworld.repository.AlCatalinaViRepository;
import ai.realworld.service.AlCatalinaViQueryService;
import ai.realworld.service.AlCatalinaViService;
import ai.realworld.service.criteria.AlCatalinaViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlCatalinaVi}.
 */
@RestController
@RequestMapping("/api/al-catalina-vis")
public class AlCatalinaViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaViResource.class);

    private static final String ENTITY_NAME = "alCatalinaVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlCatalinaViService alCatalinaViService;

    private final AlCatalinaViRepository alCatalinaViRepository;

    private final AlCatalinaViQueryService alCatalinaViQueryService;

    public AlCatalinaViResource(
        AlCatalinaViService alCatalinaViService,
        AlCatalinaViRepository alCatalinaViRepository,
        AlCatalinaViQueryService alCatalinaViQueryService
    ) {
        this.alCatalinaViService = alCatalinaViService;
        this.alCatalinaViRepository = alCatalinaViRepository;
        this.alCatalinaViQueryService = alCatalinaViQueryService;
    }

    /**
     * {@code POST  /al-catalina-vis} : Create a new alCatalinaVi.
     *
     * @param alCatalinaVi the alCatalinaVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alCatalinaVi, or with status {@code 400 (Bad Request)} if the alCatalinaVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlCatalinaVi> createAlCatalinaVi(@Valid @RequestBody AlCatalinaVi alCatalinaVi) throws URISyntaxException {
        LOG.debug("REST request to save AlCatalinaVi : {}", alCatalinaVi);
        if (alCatalinaVi.getId() != null) {
            throw new BadRequestAlertException("A new alCatalinaVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alCatalinaVi = alCatalinaViService.save(alCatalinaVi);
        return ResponseEntity.created(new URI("/api/al-catalina-vis/" + alCatalinaVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alCatalinaVi.getId().toString()))
            .body(alCatalinaVi);
    }

    /**
     * {@code PUT  /al-catalina-vis/:id} : Updates an existing alCatalinaVi.
     *
     * @param id the id of the alCatalinaVi to save.
     * @param alCatalinaVi the alCatalinaVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alCatalinaVi,
     * or with status {@code 400 (Bad Request)} if the alCatalinaVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alCatalinaVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlCatalinaVi> updateAlCatalinaVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlCatalinaVi alCatalinaVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlCatalinaVi : {}, {}", id, alCatalinaVi);
        if (alCatalinaVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alCatalinaVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alCatalinaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alCatalinaVi = alCatalinaViService.update(alCatalinaVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alCatalinaVi.getId().toString()))
            .body(alCatalinaVi);
    }

    /**
     * {@code PATCH  /al-catalina-vis/:id} : Partial updates given fields of an existing alCatalinaVi, field will ignore if it is null
     *
     * @param id the id of the alCatalinaVi to save.
     * @param alCatalinaVi the alCatalinaVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alCatalinaVi,
     * or with status {@code 400 (Bad Request)} if the alCatalinaVi is not valid,
     * or with status {@code 404 (Not Found)} if the alCatalinaVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alCatalinaVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlCatalinaVi> partialUpdateAlCatalinaVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlCatalinaVi alCatalinaVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlCatalinaVi partially : {}, {}", id, alCatalinaVi);
        if (alCatalinaVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alCatalinaVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alCatalinaViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlCatalinaVi> result = alCatalinaViService.partialUpdate(alCatalinaVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alCatalinaVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-catalina-vis} : get all the alCatalinaVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alCatalinaVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlCatalinaVi>> getAllAlCatalinaVis(
        AlCatalinaViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlCatalinaVis by criteria: {}", criteria);

        Page<AlCatalinaVi> page = alCatalinaViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-catalina-vis/count} : count all the alCatalinaVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlCatalinaVis(AlCatalinaViCriteria criteria) {
        LOG.debug("REST request to count AlCatalinaVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alCatalinaViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-catalina-vis/:id} : get the "id" alCatalinaVi.
     *
     * @param id the id of the alCatalinaVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alCatalinaVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlCatalinaVi> getAlCatalinaVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlCatalinaVi : {}", id);
        Optional<AlCatalinaVi> alCatalinaVi = alCatalinaViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alCatalinaVi);
    }

    /**
     * {@code DELETE  /al-catalina-vis/:id} : delete the "id" alCatalinaVi.
     *
     * @param id the id of the alCatalinaVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlCatalinaVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlCatalinaVi : {}", id);
        alCatalinaViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

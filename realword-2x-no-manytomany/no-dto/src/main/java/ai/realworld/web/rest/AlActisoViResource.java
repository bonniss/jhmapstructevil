package ai.realworld.web.rest;

import ai.realworld.domain.AlActisoVi;
import ai.realworld.repository.AlActisoViRepository;
import ai.realworld.service.AlActisoViQueryService;
import ai.realworld.service.AlActisoViService;
import ai.realworld.service.criteria.AlActisoViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlActisoVi}.
 */
@RestController
@RequestMapping("/api/al-actiso-vis")
public class AlActisoViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoViResource.class);

    private static final String ENTITY_NAME = "alActisoVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlActisoViService alActisoViService;

    private final AlActisoViRepository alActisoViRepository;

    private final AlActisoViQueryService alActisoViQueryService;

    public AlActisoViResource(
        AlActisoViService alActisoViService,
        AlActisoViRepository alActisoViRepository,
        AlActisoViQueryService alActisoViQueryService
    ) {
        this.alActisoViService = alActisoViService;
        this.alActisoViRepository = alActisoViRepository;
        this.alActisoViQueryService = alActisoViQueryService;
    }

    /**
     * {@code POST  /al-actiso-vis} : Create a new alActisoVi.
     *
     * @param alActisoVi the alActisoVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alActisoVi, or with status {@code 400 (Bad Request)} if the alActisoVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlActisoVi> createAlActisoVi(@Valid @RequestBody AlActisoVi alActisoVi) throws URISyntaxException {
        LOG.debug("REST request to save AlActisoVi : {}", alActisoVi);
        if (alActisoVi.getId() != null) {
            throw new BadRequestAlertException("A new alActisoVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alActisoVi = alActisoViService.save(alActisoVi);
        return ResponseEntity.created(new URI("/api/al-actiso-vis/" + alActisoVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alActisoVi.getId().toString()))
            .body(alActisoVi);
    }

    /**
     * {@code PUT  /al-actiso-vis/:id} : Updates an existing alActisoVi.
     *
     * @param id the id of the alActisoVi to save.
     * @param alActisoVi the alActisoVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alActisoVi,
     * or with status {@code 400 (Bad Request)} if the alActisoVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alActisoVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlActisoVi> updateAlActisoVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlActisoVi alActisoVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlActisoVi : {}, {}", id, alActisoVi);
        if (alActisoVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alActisoVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alActisoViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alActisoVi = alActisoViService.update(alActisoVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alActisoVi.getId().toString()))
            .body(alActisoVi);
    }

    /**
     * {@code PATCH  /al-actiso-vis/:id} : Partial updates given fields of an existing alActisoVi, field will ignore if it is null
     *
     * @param id the id of the alActisoVi to save.
     * @param alActisoVi the alActisoVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alActisoVi,
     * or with status {@code 400 (Bad Request)} if the alActisoVi is not valid,
     * or with status {@code 404 (Not Found)} if the alActisoVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alActisoVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlActisoVi> partialUpdateAlActisoVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlActisoVi alActisoVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlActisoVi partially : {}, {}", id, alActisoVi);
        if (alActisoVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alActisoVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alActisoViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlActisoVi> result = alActisoViService.partialUpdate(alActisoVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alActisoVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-actiso-vis} : get all the alActisoVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alActisoVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlActisoVi>> getAllAlActisoVis(
        AlActisoViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlActisoVis by criteria: {}", criteria);

        Page<AlActisoVi> page = alActisoViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-actiso-vis/count} : count all the alActisoVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlActisoVis(AlActisoViCriteria criteria) {
        LOG.debug("REST request to count AlActisoVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alActisoViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-actiso-vis/:id} : get the "id" alActisoVi.
     *
     * @param id the id of the alActisoVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alActisoVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlActisoVi> getAlActisoVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlActisoVi : {}", id);
        Optional<AlActisoVi> alActisoVi = alActisoViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alActisoVi);
    }

    /**
     * {@code DELETE  /al-actiso-vis/:id} : delete the "id" alActisoVi.
     *
     * @param id the id of the alActisoVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlActisoVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlActisoVi : {}", id);
        alActisoViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

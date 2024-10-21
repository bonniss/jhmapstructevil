package ai.realworld.web.rest;

import ai.realworld.domain.AlInquiryVi;
import ai.realworld.repository.AlInquiryViRepository;
import ai.realworld.service.AlInquiryViQueryService;
import ai.realworld.service.AlInquiryViService;
import ai.realworld.service.criteria.AlInquiryViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlInquiryVi}.
 */
@RestController
@RequestMapping("/api/al-inquiry-vis")
public class AlInquiryViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryViResource.class);

    private static final String ENTITY_NAME = "alInquiryVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlInquiryViService alInquiryViService;

    private final AlInquiryViRepository alInquiryViRepository;

    private final AlInquiryViQueryService alInquiryViQueryService;

    public AlInquiryViResource(
        AlInquiryViService alInquiryViService,
        AlInquiryViRepository alInquiryViRepository,
        AlInquiryViQueryService alInquiryViQueryService
    ) {
        this.alInquiryViService = alInquiryViService;
        this.alInquiryViRepository = alInquiryViRepository;
        this.alInquiryViQueryService = alInquiryViQueryService;
    }

    /**
     * {@code POST  /al-inquiry-vis} : Create a new alInquiryVi.
     *
     * @param alInquiryVi the alInquiryVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alInquiryVi, or with status {@code 400 (Bad Request)} if the alInquiryVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlInquiryVi> createAlInquiryVi(@Valid @RequestBody AlInquiryVi alInquiryVi) throws URISyntaxException {
        LOG.debug("REST request to save AlInquiryVi : {}", alInquiryVi);
        if (alInquiryVi.getId() != null) {
            throw new BadRequestAlertException("A new alInquiryVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alInquiryVi = alInquiryViService.save(alInquiryVi);
        return ResponseEntity.created(new URI("/api/al-inquiry-vis/" + alInquiryVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alInquiryVi.getId().toString()))
            .body(alInquiryVi);
    }

    /**
     * {@code PUT  /al-inquiry-vis/:id} : Updates an existing alInquiryVi.
     *
     * @param id the id of the alInquiryVi to save.
     * @param alInquiryVi the alInquiryVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alInquiryVi,
     * or with status {@code 400 (Bad Request)} if the alInquiryVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alInquiryVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlInquiryVi> updateAlInquiryVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlInquiryVi alInquiryVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlInquiryVi : {}, {}", id, alInquiryVi);
        if (alInquiryVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alInquiryVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alInquiryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alInquiryVi = alInquiryViService.update(alInquiryVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alInquiryVi.getId().toString()))
            .body(alInquiryVi);
    }

    /**
     * {@code PATCH  /al-inquiry-vis/:id} : Partial updates given fields of an existing alInquiryVi, field will ignore if it is null
     *
     * @param id the id of the alInquiryVi to save.
     * @param alInquiryVi the alInquiryVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alInquiryVi,
     * or with status {@code 400 (Bad Request)} if the alInquiryVi is not valid,
     * or with status {@code 404 (Not Found)} if the alInquiryVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alInquiryVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlInquiryVi> partialUpdateAlInquiryVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlInquiryVi alInquiryVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlInquiryVi partially : {}, {}", id, alInquiryVi);
        if (alInquiryVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alInquiryVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alInquiryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlInquiryVi> result = alInquiryViService.partialUpdate(alInquiryVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alInquiryVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-inquiry-vis} : get all the alInquiryVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alInquiryVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlInquiryVi>> getAllAlInquiryVis(
        AlInquiryViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlInquiryVis by criteria: {}", criteria);

        Page<AlInquiryVi> page = alInquiryViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-inquiry-vis/count} : count all the alInquiryVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlInquiryVis(AlInquiryViCriteria criteria) {
        LOG.debug("REST request to count AlInquiryVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alInquiryViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-inquiry-vis/:id} : get the "id" alInquiryVi.
     *
     * @param id the id of the alInquiryVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alInquiryVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlInquiryVi> getAlInquiryVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlInquiryVi : {}", id);
        Optional<AlInquiryVi> alInquiryVi = alInquiryViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alInquiryVi);
    }

    /**
     * {@code DELETE  /al-inquiry-vis/:id} : delete the "id" alInquiryVi.
     *
     * @param id the id of the alInquiryVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlInquiryVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlInquiryVi : {}", id);
        alInquiryViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

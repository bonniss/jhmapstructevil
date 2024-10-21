package ai.realworld.web.rest;

import ai.realworld.domain.OlMaster;
import ai.realworld.repository.OlMasterRepository;
import ai.realworld.service.OlMasterQueryService;
import ai.realworld.service.OlMasterService;
import ai.realworld.service.criteria.OlMasterCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.OlMaster}.
 */
@RestController
@RequestMapping("/api/ol-masters")
public class OlMasterResource {

    private static final Logger LOG = LoggerFactory.getLogger(OlMasterResource.class);

    private static final String ENTITY_NAME = "olMaster";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OlMasterService olMasterService;

    private final OlMasterRepository olMasterRepository;

    private final OlMasterQueryService olMasterQueryService;

    public OlMasterResource(
        OlMasterService olMasterService,
        OlMasterRepository olMasterRepository,
        OlMasterQueryService olMasterQueryService
    ) {
        this.olMasterService = olMasterService;
        this.olMasterRepository = olMasterRepository;
        this.olMasterQueryService = olMasterQueryService;
    }

    /**
     * {@code POST  /ol-masters} : Create a new olMaster.
     *
     * @param olMaster the olMaster to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new olMaster, or with status {@code 400 (Bad Request)} if the olMaster has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OlMaster> createOlMaster(@Valid @RequestBody OlMaster olMaster) throws URISyntaxException {
        LOG.debug("REST request to save OlMaster : {}", olMaster);
        if (olMaster.getId() != null) {
            throw new BadRequestAlertException("A new olMaster cannot already have an ID", ENTITY_NAME, "idexists");
        }
        olMaster = olMasterService.save(olMaster);
        return ResponseEntity.created(new URI("/api/ol-masters/" + olMaster.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, olMaster.getId().toString()))
            .body(olMaster);
    }

    /**
     * {@code PUT  /ol-masters/:id} : Updates an existing olMaster.
     *
     * @param id the id of the olMaster to save.
     * @param olMaster the olMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated olMaster,
     * or with status {@code 400 (Bad Request)} if the olMaster is not valid,
     * or with status {@code 500 (Internal Server Error)} if the olMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OlMaster> updateOlMaster(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody OlMaster olMaster
    ) throws URISyntaxException {
        LOG.debug("REST request to update OlMaster : {}, {}", id, olMaster);
        if (olMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, olMaster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!olMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        olMaster = olMasterService.update(olMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, olMaster.getId().toString()))
            .body(olMaster);
    }

    /**
     * {@code PATCH  /ol-masters/:id} : Partial updates given fields of an existing olMaster, field will ignore if it is null
     *
     * @param id the id of the olMaster to save.
     * @param olMaster the olMaster to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated olMaster,
     * or with status {@code 400 (Bad Request)} if the olMaster is not valid,
     * or with status {@code 404 (Not Found)} if the olMaster is not found,
     * or with status {@code 500 (Internal Server Error)} if the olMaster couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OlMaster> partialUpdateOlMaster(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody OlMaster olMaster
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OlMaster partially : {}, {}", id, olMaster);
        if (olMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, olMaster.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!olMasterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OlMaster> result = olMasterService.partialUpdate(olMaster);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, olMaster.getId().toString())
        );
    }

    /**
     * {@code GET  /ol-masters} : get all the olMasters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of olMasters in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OlMaster>> getAllOlMasters(
        OlMasterCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OlMasters by criteria: {}", criteria);

        Page<OlMaster> page = olMasterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ol-masters/count} : count all the olMasters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOlMasters(OlMasterCriteria criteria) {
        LOG.debug("REST request to count OlMasters by criteria: {}", criteria);
        return ResponseEntity.ok().body(olMasterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ol-masters/:id} : get the "id" olMaster.
     *
     * @param id the id of the olMaster to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the olMaster, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OlMaster> getOlMaster(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get OlMaster : {}", id);
        Optional<OlMaster> olMaster = olMasterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(olMaster);
    }

    /**
     * {@code DELETE  /ol-masters/:id} : delete the "id" olMaster.
     *
     * @param id the id of the olMaster to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOlMaster(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete OlMaster : {}", id);
        olMasterService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package ai.realworld.web.rest;

import ai.realworld.domain.Metaverse;
import ai.realworld.repository.MetaverseRepository;
import ai.realworld.service.MetaverseQueryService;
import ai.realworld.service.MetaverseService;
import ai.realworld.service.criteria.MetaverseCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.Metaverse}.
 */
@RestController
@RequestMapping("/api/metaverses")
public class MetaverseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaverseResource.class);

    private static final String ENTITY_NAME = "metaverse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaverseService metaverseService;

    private final MetaverseRepository metaverseRepository;

    private final MetaverseQueryService metaverseQueryService;

    public MetaverseResource(
        MetaverseService metaverseService,
        MetaverseRepository metaverseRepository,
        MetaverseQueryService metaverseQueryService
    ) {
        this.metaverseService = metaverseService;
        this.metaverseRepository = metaverseRepository;
        this.metaverseQueryService = metaverseQueryService;
    }

    /**
     * {@code POST  /metaverses} : Create a new metaverse.
     *
     * @param metaverse the metaverse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaverse, or with status {@code 400 (Bad Request)} if the metaverse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Metaverse> createMetaverse(@Valid @RequestBody Metaverse metaverse) throws URISyntaxException {
        LOG.debug("REST request to save Metaverse : {}", metaverse);
        if (metaverse.getId() != null) {
            throw new BadRequestAlertException("A new metaverse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaverse = metaverseService.save(metaverse);
        return ResponseEntity.created(new URI("/api/metaverses/" + metaverse.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaverse.getId().toString()))
            .body(metaverse);
    }

    /**
     * {@code PUT  /metaverses/:id} : Updates an existing metaverse.
     *
     * @param id the id of the metaverse to save.
     * @param metaverse the metaverse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaverse,
     * or with status {@code 400 (Bad Request)} if the metaverse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaverse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Metaverse> updateMetaverse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Metaverse metaverse
    ) throws URISyntaxException {
        LOG.debug("REST request to update Metaverse : {}, {}", id, metaverse);
        if (metaverse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaverse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaverseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaverse = metaverseService.update(metaverse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaverse.getId().toString()))
            .body(metaverse);
    }

    /**
     * {@code PATCH  /metaverses/:id} : Partial updates given fields of an existing metaverse, field will ignore if it is null
     *
     * @param id the id of the metaverse to save.
     * @param metaverse the metaverse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaverse,
     * or with status {@code 400 (Bad Request)} if the metaverse is not valid,
     * or with status {@code 404 (Not Found)} if the metaverse is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaverse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Metaverse> partialUpdateMetaverse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Metaverse metaverse
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Metaverse partially : {}, {}", id, metaverse);
        if (metaverse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaverse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaverseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Metaverse> result = metaverseService.partialUpdate(metaverse);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaverse.getId().toString())
        );
    }

    /**
     * {@code GET  /metaverses} : get all the metaverses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaverses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Metaverse>> getAllMetaverses(
        MetaverseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Metaverses by criteria: {}", criteria);

        Page<Metaverse> page = metaverseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /metaverses/count} : count all the metaverses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaverses(MetaverseCriteria criteria) {
        LOG.debug("REST request to count Metaverses by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaverseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /metaverses/:id} : get the "id" metaverse.
     *
     * @param id the id of the metaverse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaverse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Metaverse> getMetaverse(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Metaverse : {}", id);
        Optional<Metaverse> metaverse = metaverseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaverse);
    }

    /**
     * {@code DELETE  /metaverses/:id} : delete the "id" metaverse.
     *
     * @param id the id of the metaverse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaverse(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Metaverse : {}", id);
        metaverseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

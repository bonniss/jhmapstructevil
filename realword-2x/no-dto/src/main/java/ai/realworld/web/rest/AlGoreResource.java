package ai.realworld.web.rest;

import ai.realworld.domain.AlGore;
import ai.realworld.repository.AlGoreRepository;
import ai.realworld.service.AlGoreQueryService;
import ai.realworld.service.AlGoreService;
import ai.realworld.service.criteria.AlGoreCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlGore}.
 */
@RestController
@RequestMapping("/api/al-gores")
public class AlGoreResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoreResource.class);

    private static final String ENTITY_NAME = "alGore";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlGoreService alGoreService;

    private final AlGoreRepository alGoreRepository;

    private final AlGoreQueryService alGoreQueryService;

    public AlGoreResource(AlGoreService alGoreService, AlGoreRepository alGoreRepository, AlGoreQueryService alGoreQueryService) {
        this.alGoreService = alGoreService;
        this.alGoreRepository = alGoreRepository;
        this.alGoreQueryService = alGoreQueryService;
    }

    /**
     * {@code POST  /al-gores} : Create a new alGore.
     *
     * @param alGore the alGore to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alGore, or with status {@code 400 (Bad Request)} if the alGore has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlGore> createAlGore(@Valid @RequestBody AlGore alGore) throws URISyntaxException {
        LOG.debug("REST request to save AlGore : {}", alGore);
        if (alGore.getId() != null) {
            throw new BadRequestAlertException("A new alGore cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alGore = alGoreService.save(alGore);
        return ResponseEntity.created(new URI("/api/al-gores/" + alGore.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alGore.getId().toString()))
            .body(alGore);
    }

    /**
     * {@code PUT  /al-gores/:id} : Updates an existing alGore.
     *
     * @param id the id of the alGore to save.
     * @param alGore the alGore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGore,
     * or with status {@code 400 (Bad Request)} if the alGore is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alGore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlGore> updateAlGore(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlGore alGore
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlGore : {}, {}", id, alGore);
        if (alGore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alGore = alGoreService.update(alGore);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGore.getId().toString()))
            .body(alGore);
    }

    /**
     * {@code PATCH  /al-gores/:id} : Partial updates given fields of an existing alGore, field will ignore if it is null
     *
     * @param id the id of the alGore to save.
     * @param alGore the alGore to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alGore,
     * or with status {@code 400 (Bad Request)} if the alGore is not valid,
     * or with status {@code 404 (Not Found)} if the alGore is not found,
     * or with status {@code 500 (Internal Server Error)} if the alGore couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlGore> partialUpdateAlGore(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlGore alGore
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlGore partially : {}, {}", id, alGore);
        if (alGore.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alGore.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alGoreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlGore> result = alGoreService.partialUpdate(alGore);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alGore.getId().toString())
        );
    }

    /**
     * {@code GET  /al-gores} : get all the alGores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alGores in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlGore>> getAllAlGores(
        AlGoreCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlGores by criteria: {}", criteria);

        Page<AlGore> page = alGoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-gores/count} : count all the alGores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlGores(AlGoreCriteria criteria) {
        LOG.debug("REST request to count AlGores by criteria: {}", criteria);
        return ResponseEntity.ok().body(alGoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-gores/:id} : get the "id" alGore.
     *
     * @param id the id of the alGore to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alGore, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlGore> getAlGore(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlGore : {}", id);
        Optional<AlGore> alGore = alGoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alGore);
    }

    /**
     * {@code DELETE  /al-gores/:id} : delete the "id" alGore.
     *
     * @param id the id of the alGore to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlGore(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlGore : {}", id);
        alGoreService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

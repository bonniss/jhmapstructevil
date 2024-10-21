package ai.realworld.web.rest;

import ai.realworld.domain.AlProty;
import ai.realworld.repository.AlProtyRepository;
import ai.realworld.service.AlProtyQueryService;
import ai.realworld.service.AlProtyService;
import ai.realworld.service.criteria.AlProtyCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlProty}.
 */
@RestController
@RequestMapping("/api/al-proties")
public class AlProtyResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlProtyResource.class);

    private static final String ENTITY_NAME = "alProty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlProtyService alProtyService;

    private final AlProtyRepository alProtyRepository;

    private final AlProtyQueryService alProtyQueryService;

    public AlProtyResource(AlProtyService alProtyService, AlProtyRepository alProtyRepository, AlProtyQueryService alProtyQueryService) {
        this.alProtyService = alProtyService;
        this.alProtyRepository = alProtyRepository;
        this.alProtyQueryService = alProtyQueryService;
    }

    /**
     * {@code POST  /al-proties} : Create a new alProty.
     *
     * @param alProty the alProty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alProty, or with status {@code 400 (Bad Request)} if the alProty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlProty> createAlProty(@Valid @RequestBody AlProty alProty) throws URISyntaxException {
        LOG.debug("REST request to save AlProty : {}", alProty);
        if (alProty.getId() != null) {
            throw new BadRequestAlertException("A new alProty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alProty = alProtyService.save(alProty);
        return ResponseEntity.created(new URI("/api/al-proties/" + alProty.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alProty.getId().toString()))
            .body(alProty);
    }

    /**
     * {@code PUT  /al-proties/:id} : Updates an existing alProty.
     *
     * @param id the id of the alProty to save.
     * @param alProty the alProty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProty,
     * or with status {@code 400 (Bad Request)} if the alProty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alProty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlProty> updateAlProty(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlProty alProty
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlProty : {}, {}", id, alProty);
        if (alProty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProtyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alProty = alProtyService.update(alProty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProty.getId().toString()))
            .body(alProty);
    }

    /**
     * {@code PATCH  /al-proties/:id} : Partial updates given fields of an existing alProty, field will ignore if it is null
     *
     * @param id the id of the alProty to save.
     * @param alProty the alProty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProty,
     * or with status {@code 400 (Bad Request)} if the alProty is not valid,
     * or with status {@code 404 (Not Found)} if the alProty is not found,
     * or with status {@code 500 (Internal Server Error)} if the alProty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlProty> partialUpdateAlProty(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlProty alProty
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlProty partially : {}, {}", id, alProty);
        if (alProty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProtyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlProty> result = alProtyService.partialUpdate(alProty);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProty.getId().toString())
        );
    }

    /**
     * {@code GET  /al-proties} : get all the alProties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alProties in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlProty>> getAllAlProties(
        AlProtyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlProties by criteria: {}", criteria);

        Page<AlProty> page = alProtyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-proties/count} : count all the alProties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlProties(AlProtyCriteria criteria) {
        LOG.debug("REST request to count AlProties by criteria: {}", criteria);
        return ResponseEntity.ok().body(alProtyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-proties/:id} : get the "id" alProty.
     *
     * @param id the id of the alProty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alProty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlProty> getAlProty(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlProty : {}", id);
        Optional<AlProty> alProty = alProtyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alProty);
    }

    /**
     * {@code DELETE  /al-proties/:id} : delete the "id" alProty.
     *
     * @param id the id of the alProty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlProty(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlProty : {}", id);
        alProtyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

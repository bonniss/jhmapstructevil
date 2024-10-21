package ai.realworld.web.rest;

import ai.realworld.domain.AlPounder;
import ai.realworld.repository.AlPounderRepository;
import ai.realworld.service.AlPounderQueryService;
import ai.realworld.service.AlPounderService;
import ai.realworld.service.criteria.AlPounderCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlPounder}.
 */
@RestController
@RequestMapping("/api/al-pounders")
public class AlPounderResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderResource.class);

    private static final String ENTITY_NAME = "alPounder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPounderService alPounderService;

    private final AlPounderRepository alPounderRepository;

    private final AlPounderQueryService alPounderQueryService;

    public AlPounderResource(
        AlPounderService alPounderService,
        AlPounderRepository alPounderRepository,
        AlPounderQueryService alPounderQueryService
    ) {
        this.alPounderService = alPounderService;
        this.alPounderRepository = alPounderRepository;
        this.alPounderQueryService = alPounderQueryService;
    }

    /**
     * {@code POST  /al-pounders} : Create a new alPounder.
     *
     * @param alPounder the alPounder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPounder, or with status {@code 400 (Bad Request)} if the alPounder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPounder> createAlPounder(@Valid @RequestBody AlPounder alPounder) throws URISyntaxException {
        LOG.debug("REST request to save AlPounder : {}", alPounder);
        if (alPounder.getId() != null) {
            throw new BadRequestAlertException("A new alPounder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPounder = alPounderService.save(alPounder);
        return ResponseEntity.created(new URI("/api/al-pounders/" + alPounder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPounder.getId().toString()))
            .body(alPounder);
    }

    /**
     * {@code PUT  /al-pounders/:id} : Updates an existing alPounder.
     *
     * @param id the id of the alPounder to save.
     * @param alPounder the alPounder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPounder,
     * or with status {@code 400 (Bad Request)} if the alPounder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPounder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPounder> updateAlPounder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPounder alPounder
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPounder : {}, {}", id, alPounder);
        if (alPounder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPounder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPounderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPounder = alPounderService.update(alPounder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPounder.getId().toString()))
            .body(alPounder);
    }

    /**
     * {@code PATCH  /al-pounders/:id} : Partial updates given fields of an existing alPounder, field will ignore if it is null
     *
     * @param id the id of the alPounder to save.
     * @param alPounder the alPounder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPounder,
     * or with status {@code 400 (Bad Request)} if the alPounder is not valid,
     * or with status {@code 404 (Not Found)} if the alPounder is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPounder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPounder> partialUpdateAlPounder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPounder alPounder
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPounder partially : {}, {}", id, alPounder);
        if (alPounder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPounder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPounderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPounder> result = alPounderService.partialUpdate(alPounder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPounder.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pounders} : get all the alPounders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPounders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPounder>> getAllAlPounders(
        AlPounderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPounders by criteria: {}", criteria);

        Page<AlPounder> page = alPounderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pounders/count} : count all the alPounders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPounders(AlPounderCriteria criteria) {
        LOG.debug("REST request to count AlPounders by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPounderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pounders/:id} : get the "id" alPounder.
     *
     * @param id the id of the alPounder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPounder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPounder> getAlPounder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPounder : {}", id);
        Optional<AlPounder> alPounder = alPounderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPounder);
    }

    /**
     * {@code DELETE  /al-pounders/:id} : delete the "id" alPounder.
     *
     * @param id the id of the alPounder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPounder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPounder : {}", id);
        alPounderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

package ai.realworld.web.rest;

import ai.realworld.domain.AlPyuJoker;
import ai.realworld.repository.AlPyuJokerRepository;
import ai.realworld.service.AlPyuJokerQueryService;
import ai.realworld.service.AlPyuJokerService;
import ai.realworld.service.criteria.AlPyuJokerCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlPyuJoker}.
 */
@RestController
@RequestMapping("/api/al-pyu-jokers")
public class AlPyuJokerResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuJokerResource.class);

    private static final String ENTITY_NAME = "alPyuJoker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuJokerService alPyuJokerService;

    private final AlPyuJokerRepository alPyuJokerRepository;

    private final AlPyuJokerQueryService alPyuJokerQueryService;

    public AlPyuJokerResource(
        AlPyuJokerService alPyuJokerService,
        AlPyuJokerRepository alPyuJokerRepository,
        AlPyuJokerQueryService alPyuJokerQueryService
    ) {
        this.alPyuJokerService = alPyuJokerService;
        this.alPyuJokerRepository = alPyuJokerRepository;
        this.alPyuJokerQueryService = alPyuJokerQueryService;
    }

    /**
     * {@code POST  /al-pyu-jokers} : Create a new alPyuJoker.
     *
     * @param alPyuJoker the alPyuJoker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuJoker, or with status {@code 400 (Bad Request)} if the alPyuJoker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuJoker> createAlPyuJoker(@Valid @RequestBody AlPyuJoker alPyuJoker) throws URISyntaxException {
        LOG.debug("REST request to save AlPyuJoker : {}", alPyuJoker);
        if (alPyuJoker.getId() != null) {
            throw new BadRequestAlertException("A new alPyuJoker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuJoker = alPyuJokerService.save(alPyuJoker);
        return ResponseEntity.created(new URI("/api/al-pyu-jokers/" + alPyuJoker.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuJoker.getId().toString()))
            .body(alPyuJoker);
    }

    /**
     * {@code PUT  /al-pyu-jokers/:id} : Updates an existing alPyuJoker.
     *
     * @param id the id of the alPyuJoker to save.
     * @param alPyuJoker the alPyuJoker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuJoker,
     * or with status {@code 400 (Bad Request)} if the alPyuJoker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuJoker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuJoker> updateAlPyuJoker(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlPyuJoker alPyuJoker
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuJoker : {}, {}", id, alPyuJoker);
        if (alPyuJoker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuJoker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuJokerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuJoker = alPyuJokerService.update(alPyuJoker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuJoker.getId().toString()))
            .body(alPyuJoker);
    }

    /**
     * {@code PATCH  /al-pyu-jokers/:id} : Partial updates given fields of an existing alPyuJoker, field will ignore if it is null
     *
     * @param id the id of the alPyuJoker to save.
     * @param alPyuJoker the alPyuJoker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuJoker,
     * or with status {@code 400 (Bad Request)} if the alPyuJoker is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuJoker is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuJoker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuJoker> partialUpdateAlPyuJoker(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlPyuJoker alPyuJoker
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuJoker partially : {}, {}", id, alPyuJoker);
        if (alPyuJoker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuJoker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuJokerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuJoker> result = alPyuJokerService.partialUpdate(alPyuJoker);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuJoker.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-jokers} : get all the alPyuJokers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuJokers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuJoker>> getAllAlPyuJokers(
        AlPyuJokerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuJokers by criteria: {}", criteria);

        Page<AlPyuJoker> page = alPyuJokerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-jokers/count} : count all the alPyuJokers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuJokers(AlPyuJokerCriteria criteria) {
        LOG.debug("REST request to count AlPyuJokers by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuJokerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-jokers/:id} : get the "id" alPyuJoker.
     *
     * @param id the id of the alPyuJoker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuJoker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuJoker> getAlPyuJoker(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlPyuJoker : {}", id);
        Optional<AlPyuJoker> alPyuJoker = alPyuJokerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuJoker);
    }

    /**
     * {@code DELETE  /al-pyu-jokers/:id} : delete the "id" alPyuJoker.
     *
     * @param id the id of the alPyuJoker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuJoker(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlPyuJoker : {}", id);
        alPyuJokerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

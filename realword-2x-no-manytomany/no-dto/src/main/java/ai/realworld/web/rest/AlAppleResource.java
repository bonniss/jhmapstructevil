package ai.realworld.web.rest;

import ai.realworld.domain.AlApple;
import ai.realworld.repository.AlAppleRepository;
import ai.realworld.service.AlAppleQueryService;
import ai.realworld.service.AlAppleService;
import ai.realworld.service.criteria.AlAppleCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlApple}.
 */
@RestController
@RequestMapping("/api/al-apples")
public class AlAppleResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleResource.class);

    private static final String ENTITY_NAME = "alApple";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlAppleService alAppleService;

    private final AlAppleRepository alAppleRepository;

    private final AlAppleQueryService alAppleQueryService;

    public AlAppleResource(AlAppleService alAppleService, AlAppleRepository alAppleRepository, AlAppleQueryService alAppleQueryService) {
        this.alAppleService = alAppleService;
        this.alAppleRepository = alAppleRepository;
        this.alAppleQueryService = alAppleQueryService;
    }

    /**
     * {@code POST  /al-apples} : Create a new alApple.
     *
     * @param alApple the alApple to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alApple, or with status {@code 400 (Bad Request)} if the alApple has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlApple> createAlApple(@Valid @RequestBody AlApple alApple) throws URISyntaxException {
        LOG.debug("REST request to save AlApple : {}", alApple);
        if (alApple.getId() != null) {
            throw new BadRequestAlertException("A new alApple cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alApple = alAppleService.save(alApple);
        return ResponseEntity.created(new URI("/api/al-apples/" + alApple.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alApple.getId().toString()))
            .body(alApple);
    }

    /**
     * {@code PUT  /al-apples/:id} : Updates an existing alApple.
     *
     * @param id the id of the alApple to save.
     * @param alApple the alApple to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alApple,
     * or with status {@code 400 (Bad Request)} if the alApple is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alApple couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlApple> updateAlApple(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlApple alApple
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlApple : {}, {}", id, alApple);
        if (alApple.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alApple.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAppleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alApple = alAppleService.update(alApple);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alApple.getId().toString()))
            .body(alApple);
    }

    /**
     * {@code PATCH  /al-apples/:id} : Partial updates given fields of an existing alApple, field will ignore if it is null
     *
     * @param id the id of the alApple to save.
     * @param alApple the alApple to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alApple,
     * or with status {@code 400 (Bad Request)} if the alApple is not valid,
     * or with status {@code 404 (Not Found)} if the alApple is not found,
     * or with status {@code 500 (Internal Server Error)} if the alApple couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlApple> partialUpdateAlApple(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlApple alApple
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlApple partially : {}, {}", id, alApple);
        if (alApple.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alApple.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAppleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlApple> result = alAppleService.partialUpdate(alApple);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alApple.getId().toString())
        );
    }

    /**
     * {@code GET  /al-apples} : get all the alApples.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alApples in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlApple>> getAllAlApples(
        AlAppleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlApples by criteria: {}", criteria);

        Page<AlApple> page = alAppleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-apples/count} : count all the alApples.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlApples(AlAppleCriteria criteria) {
        LOG.debug("REST request to count AlApples by criteria: {}", criteria);
        return ResponseEntity.ok().body(alAppleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-apples/:id} : get the "id" alApple.
     *
     * @param id the id of the alApple to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alApple, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlApple> getAlApple(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlApple : {}", id);
        Optional<AlApple> alApple = alAppleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alApple);
    }

    /**
     * {@code DELETE  /al-apples/:id} : delete the "id" alApple.
     *
     * @param id the id of the alApple to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlApple(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlApple : {}", id);
        alAppleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

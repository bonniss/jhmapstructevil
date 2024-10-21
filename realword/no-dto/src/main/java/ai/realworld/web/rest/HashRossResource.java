package ai.realworld.web.rest;

import ai.realworld.domain.HashRoss;
import ai.realworld.repository.HashRossRepository;
import ai.realworld.service.HashRossQueryService;
import ai.realworld.service.HashRossService;
import ai.realworld.service.criteria.HashRossCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.HashRoss}.
 */
@RestController
@RequestMapping("/api/hash-rosses")
public class HashRossResource {

    private static final Logger LOG = LoggerFactory.getLogger(HashRossResource.class);

    private static final String ENTITY_NAME = "hashRoss";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashRossService hashRossService;

    private final HashRossRepository hashRossRepository;

    private final HashRossQueryService hashRossQueryService;

    public HashRossResource(
        HashRossService hashRossService,
        HashRossRepository hashRossRepository,
        HashRossQueryService hashRossQueryService
    ) {
        this.hashRossService = hashRossService;
        this.hashRossRepository = hashRossRepository;
        this.hashRossQueryService = hashRossQueryService;
    }

    /**
     * {@code POST  /hash-rosses} : Create a new hashRoss.
     *
     * @param hashRoss the hashRoss to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashRoss, or with status {@code 400 (Bad Request)} if the hashRoss has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HashRoss> createHashRoss(@Valid @RequestBody HashRoss hashRoss) throws URISyntaxException {
        LOG.debug("REST request to save HashRoss : {}", hashRoss);
        if (hashRoss.getId() != null) {
            throw new BadRequestAlertException("A new hashRoss cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hashRoss = hashRossService.save(hashRoss);
        return ResponseEntity.created(new URI("/api/hash-rosses/" + hashRoss.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hashRoss.getId().toString()))
            .body(hashRoss);
    }

    /**
     * {@code PUT  /hash-rosses/:id} : Updates an existing hashRoss.
     *
     * @param id the id of the hashRoss to save.
     * @param hashRoss the hashRoss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashRoss,
     * or with status {@code 400 (Bad Request)} if the hashRoss is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashRoss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HashRoss> updateHashRoss(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HashRoss hashRoss
    ) throws URISyntaxException {
        LOG.debug("REST request to update HashRoss : {}, {}", id, hashRoss);
        if (hashRoss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashRoss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashRossRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hashRoss = hashRossService.update(hashRoss);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashRoss.getId().toString()))
            .body(hashRoss);
    }

    /**
     * {@code PATCH  /hash-rosses/:id} : Partial updates given fields of an existing hashRoss, field will ignore if it is null
     *
     * @param id the id of the hashRoss to save.
     * @param hashRoss the hashRoss to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashRoss,
     * or with status {@code 400 (Bad Request)} if the hashRoss is not valid,
     * or with status {@code 404 (Not Found)} if the hashRoss is not found,
     * or with status {@code 500 (Internal Server Error)} if the hashRoss couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HashRoss> partialUpdateHashRoss(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HashRoss hashRoss
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HashRoss partially : {}, {}", id, hashRoss);
        if (hashRoss.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hashRoss.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hashRossRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HashRoss> result = hashRossService.partialUpdate(hashRoss);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashRoss.getId().toString())
        );
    }

    /**
     * {@code GET  /hash-rosses} : get all the hashRosses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashRosses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HashRoss>> getAllHashRosses(
        HashRossCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HashRosses by criteria: {}", criteria);

        Page<HashRoss> page = hashRossQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hash-rosses/count} : count all the hashRosses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHashRosses(HashRossCriteria criteria) {
        LOG.debug("REST request to count HashRosses by criteria: {}", criteria);
        return ResponseEntity.ok().body(hashRossQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hash-rosses/:id} : get the "id" hashRoss.
     *
     * @param id the id of the hashRoss to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashRoss, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HashRoss> getHashRoss(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HashRoss : {}", id);
        Optional<HashRoss> hashRoss = hashRossService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashRoss);
    }

    /**
     * {@code DELETE  /hash-rosses/:id} : delete the "id" hashRoss.
     *
     * @param id the id of the hashRoss to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHashRoss(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HashRoss : {}", id);
        hashRossService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

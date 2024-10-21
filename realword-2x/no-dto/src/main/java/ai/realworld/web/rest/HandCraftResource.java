package ai.realworld.web.rest;

import ai.realworld.domain.HandCraft;
import ai.realworld.repository.HandCraftRepository;
import ai.realworld.service.HandCraftQueryService;
import ai.realworld.service.HandCraftService;
import ai.realworld.service.criteria.HandCraftCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.HandCraft}.
 */
@RestController
@RequestMapping("/api/hand-crafts")
public class HandCraftResource {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftResource.class);

    private static final String ENTITY_NAME = "handCraft";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HandCraftService handCraftService;

    private final HandCraftRepository handCraftRepository;

    private final HandCraftQueryService handCraftQueryService;

    public HandCraftResource(
        HandCraftService handCraftService,
        HandCraftRepository handCraftRepository,
        HandCraftQueryService handCraftQueryService
    ) {
        this.handCraftService = handCraftService;
        this.handCraftRepository = handCraftRepository;
        this.handCraftQueryService = handCraftQueryService;
    }

    /**
     * {@code POST  /hand-crafts} : Create a new handCraft.
     *
     * @param handCraft the handCraft to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new handCraft, or with status {@code 400 (Bad Request)} if the handCraft has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HandCraft> createHandCraft(@Valid @RequestBody HandCraft handCraft) throws URISyntaxException {
        LOG.debug("REST request to save HandCraft : {}", handCraft);
        if (handCraft.getId() != null) {
            throw new BadRequestAlertException("A new handCraft cannot already have an ID", ENTITY_NAME, "idexists");
        }
        handCraft = handCraftService.save(handCraft);
        return ResponseEntity.created(new URI("/api/hand-crafts/" + handCraft.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, handCraft.getId().toString()))
            .body(handCraft);
    }

    /**
     * {@code PUT  /hand-crafts/:id} : Updates an existing handCraft.
     *
     * @param id the id of the handCraft to save.
     * @param handCraft the handCraft to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated handCraft,
     * or with status {@code 400 (Bad Request)} if the handCraft is not valid,
     * or with status {@code 500 (Internal Server Error)} if the handCraft couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HandCraft> updateHandCraft(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HandCraft handCraft
    ) throws URISyntaxException {
        LOG.debug("REST request to update HandCraft : {}, {}", id, handCraft);
        if (handCraft.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, handCraft.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!handCraftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        handCraft = handCraftService.update(handCraft);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, handCraft.getId().toString()))
            .body(handCraft);
    }

    /**
     * {@code PATCH  /hand-crafts/:id} : Partial updates given fields of an existing handCraft, field will ignore if it is null
     *
     * @param id the id of the handCraft to save.
     * @param handCraft the handCraft to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated handCraft,
     * or with status {@code 400 (Bad Request)} if the handCraft is not valid,
     * or with status {@code 404 (Not Found)} if the handCraft is not found,
     * or with status {@code 500 (Internal Server Error)} if the handCraft couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HandCraft> partialUpdateHandCraft(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HandCraft handCraft
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HandCraft partially : {}, {}", id, handCraft);
        if (handCraft.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, handCraft.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!handCraftRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HandCraft> result = handCraftService.partialUpdate(handCraft);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, handCraft.getId().toString())
        );
    }

    /**
     * {@code GET  /hand-crafts} : get all the handCrafts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of handCrafts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HandCraft>> getAllHandCrafts(
        HandCraftCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HandCrafts by criteria: {}", criteria);

        Page<HandCraft> page = handCraftQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hand-crafts/count} : count all the handCrafts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHandCrafts(HandCraftCriteria criteria) {
        LOG.debug("REST request to count HandCrafts by criteria: {}", criteria);
        return ResponseEntity.ok().body(handCraftQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hand-crafts/:id} : get the "id" handCraft.
     *
     * @param id the id of the handCraft to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the handCraft, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HandCraft> getHandCraft(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HandCraft : {}", id);
        Optional<HandCraft> handCraft = handCraftService.findOne(id);
        return ResponseUtil.wrapOrNotFound(handCraft);
    }

    /**
     * {@code DELETE  /hand-crafts/:id} : delete the "id" handCraft.
     *
     * @param id the id of the handCraft to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHandCraft(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HandCraft : {}", id);
        handCraftService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

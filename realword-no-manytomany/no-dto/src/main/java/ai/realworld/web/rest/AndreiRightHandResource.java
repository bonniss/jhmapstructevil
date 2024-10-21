package ai.realworld.web.rest;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.repository.AndreiRightHandRepository;
import ai.realworld.service.AndreiRightHandQueryService;
import ai.realworld.service.AndreiRightHandService;
import ai.realworld.service.criteria.AndreiRightHandCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AndreiRightHand}.
 */
@RestController
@RequestMapping("/api/andrei-right-hands")
public class AndreiRightHandResource {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandResource.class);

    private static final String ENTITY_NAME = "andreiRightHand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AndreiRightHandService andreiRightHandService;

    private final AndreiRightHandRepository andreiRightHandRepository;

    private final AndreiRightHandQueryService andreiRightHandQueryService;

    public AndreiRightHandResource(
        AndreiRightHandService andreiRightHandService,
        AndreiRightHandRepository andreiRightHandRepository,
        AndreiRightHandQueryService andreiRightHandQueryService
    ) {
        this.andreiRightHandService = andreiRightHandService;
        this.andreiRightHandRepository = andreiRightHandRepository;
        this.andreiRightHandQueryService = andreiRightHandQueryService;
    }

    /**
     * {@code POST  /andrei-right-hands} : Create a new andreiRightHand.
     *
     * @param andreiRightHand the andreiRightHand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new andreiRightHand, or with status {@code 400 (Bad Request)} if the andreiRightHand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AndreiRightHand> createAndreiRightHand(@RequestBody AndreiRightHand andreiRightHand) throws URISyntaxException {
        LOG.debug("REST request to save AndreiRightHand : {}", andreiRightHand);
        if (andreiRightHand.getId() != null) {
            throw new BadRequestAlertException("A new andreiRightHand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        andreiRightHand = andreiRightHandService.save(andreiRightHand);
        return ResponseEntity.created(new URI("/api/andrei-right-hands/" + andreiRightHand.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, andreiRightHand.getId().toString()))
            .body(andreiRightHand);
    }

    /**
     * {@code PUT  /andrei-right-hands/:id} : Updates an existing andreiRightHand.
     *
     * @param id the id of the andreiRightHand to save.
     * @param andreiRightHand the andreiRightHand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated andreiRightHand,
     * or with status {@code 400 (Bad Request)} if the andreiRightHand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the andreiRightHand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AndreiRightHand> updateAndreiRightHand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AndreiRightHand andreiRightHand
    ) throws URISyntaxException {
        LOG.debug("REST request to update AndreiRightHand : {}, {}", id, andreiRightHand);
        if (andreiRightHand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, andreiRightHand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!andreiRightHandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        andreiRightHand = andreiRightHandService.update(andreiRightHand);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, andreiRightHand.getId().toString()))
            .body(andreiRightHand);
    }

    /**
     * {@code PATCH  /andrei-right-hands/:id} : Partial updates given fields of an existing andreiRightHand, field will ignore if it is null
     *
     * @param id the id of the andreiRightHand to save.
     * @param andreiRightHand the andreiRightHand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated andreiRightHand,
     * or with status {@code 400 (Bad Request)} if the andreiRightHand is not valid,
     * or with status {@code 404 (Not Found)} if the andreiRightHand is not found,
     * or with status {@code 500 (Internal Server Error)} if the andreiRightHand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AndreiRightHand> partialUpdateAndreiRightHand(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AndreiRightHand andreiRightHand
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AndreiRightHand partially : {}, {}", id, andreiRightHand);
        if (andreiRightHand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, andreiRightHand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!andreiRightHandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AndreiRightHand> result = andreiRightHandService.partialUpdate(andreiRightHand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, andreiRightHand.getId().toString())
        );
    }

    /**
     * {@code GET  /andrei-right-hands} : get all the andreiRightHands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of andreiRightHands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AndreiRightHand>> getAllAndreiRightHands(
        AndreiRightHandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AndreiRightHands by criteria: {}", criteria);

        Page<AndreiRightHand> page = andreiRightHandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /andrei-right-hands/count} : count all the andreiRightHands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAndreiRightHands(AndreiRightHandCriteria criteria) {
        LOG.debug("REST request to count AndreiRightHands by criteria: {}", criteria);
        return ResponseEntity.ok().body(andreiRightHandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /andrei-right-hands/:id} : get the "id" andreiRightHand.
     *
     * @param id the id of the andreiRightHand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the andreiRightHand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AndreiRightHand> getAndreiRightHand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AndreiRightHand : {}", id);
        Optional<AndreiRightHand> andreiRightHand = andreiRightHandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(andreiRightHand);
    }

    /**
     * {@code DELETE  /andrei-right-hands/:id} : delete the "id" andreiRightHand.
     *
     * @param id the id of the andreiRightHand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAndreiRightHand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AndreiRightHand : {}", id);
        andreiRightHandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

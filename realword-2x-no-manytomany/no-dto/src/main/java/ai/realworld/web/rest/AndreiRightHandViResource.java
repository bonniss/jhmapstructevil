package ai.realworld.web.rest;

import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.repository.AndreiRightHandViRepository;
import ai.realworld.service.AndreiRightHandViQueryService;
import ai.realworld.service.AndreiRightHandViService;
import ai.realworld.service.criteria.AndreiRightHandViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AndreiRightHandVi}.
 */
@RestController
@RequestMapping("/api/andrei-right-hand-vis")
public class AndreiRightHandViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandViResource.class);

    private static final String ENTITY_NAME = "andreiRightHandVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AndreiRightHandViService andreiRightHandViService;

    private final AndreiRightHandViRepository andreiRightHandViRepository;

    private final AndreiRightHandViQueryService andreiRightHandViQueryService;

    public AndreiRightHandViResource(
        AndreiRightHandViService andreiRightHandViService,
        AndreiRightHandViRepository andreiRightHandViRepository,
        AndreiRightHandViQueryService andreiRightHandViQueryService
    ) {
        this.andreiRightHandViService = andreiRightHandViService;
        this.andreiRightHandViRepository = andreiRightHandViRepository;
        this.andreiRightHandViQueryService = andreiRightHandViQueryService;
    }

    /**
     * {@code POST  /andrei-right-hand-vis} : Create a new andreiRightHandVi.
     *
     * @param andreiRightHandVi the andreiRightHandVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new andreiRightHandVi, or with status {@code 400 (Bad Request)} if the andreiRightHandVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AndreiRightHandVi> createAndreiRightHandVi(@RequestBody AndreiRightHandVi andreiRightHandVi)
        throws URISyntaxException {
        LOG.debug("REST request to save AndreiRightHandVi : {}", andreiRightHandVi);
        if (andreiRightHandVi.getId() != null) {
            throw new BadRequestAlertException("A new andreiRightHandVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        andreiRightHandVi = andreiRightHandViService.save(andreiRightHandVi);
        return ResponseEntity.created(new URI("/api/andrei-right-hand-vis/" + andreiRightHandVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, andreiRightHandVi.getId().toString()))
            .body(andreiRightHandVi);
    }

    /**
     * {@code PUT  /andrei-right-hand-vis/:id} : Updates an existing andreiRightHandVi.
     *
     * @param id the id of the andreiRightHandVi to save.
     * @param andreiRightHandVi the andreiRightHandVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated andreiRightHandVi,
     * or with status {@code 400 (Bad Request)} if the andreiRightHandVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the andreiRightHandVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AndreiRightHandVi> updateAndreiRightHandVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AndreiRightHandVi andreiRightHandVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AndreiRightHandVi : {}, {}", id, andreiRightHandVi);
        if (andreiRightHandVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, andreiRightHandVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!andreiRightHandViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        andreiRightHandVi = andreiRightHandViService.update(andreiRightHandVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, andreiRightHandVi.getId().toString()))
            .body(andreiRightHandVi);
    }

    /**
     * {@code PATCH  /andrei-right-hand-vis/:id} : Partial updates given fields of an existing andreiRightHandVi, field will ignore if it is null
     *
     * @param id the id of the andreiRightHandVi to save.
     * @param andreiRightHandVi the andreiRightHandVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated andreiRightHandVi,
     * or with status {@code 400 (Bad Request)} if the andreiRightHandVi is not valid,
     * or with status {@code 404 (Not Found)} if the andreiRightHandVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the andreiRightHandVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AndreiRightHandVi> partialUpdateAndreiRightHandVi(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AndreiRightHandVi andreiRightHandVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AndreiRightHandVi partially : {}, {}", id, andreiRightHandVi);
        if (andreiRightHandVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, andreiRightHandVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!andreiRightHandViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AndreiRightHandVi> result = andreiRightHandViService.partialUpdate(andreiRightHandVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, andreiRightHandVi.getId().toString())
        );
    }

    /**
     * {@code GET  /andrei-right-hand-vis} : get all the andreiRightHandVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of andreiRightHandVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AndreiRightHandVi>> getAllAndreiRightHandVis(
        AndreiRightHandViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AndreiRightHandVis by criteria: {}", criteria);

        Page<AndreiRightHandVi> page = andreiRightHandViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /andrei-right-hand-vis/count} : count all the andreiRightHandVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAndreiRightHandVis(AndreiRightHandViCriteria criteria) {
        LOG.debug("REST request to count AndreiRightHandVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(andreiRightHandViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /andrei-right-hand-vis/:id} : get the "id" andreiRightHandVi.
     *
     * @param id the id of the andreiRightHandVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the andreiRightHandVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AndreiRightHandVi> getAndreiRightHandVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AndreiRightHandVi : {}", id);
        Optional<AndreiRightHandVi> andreiRightHandVi = andreiRightHandViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(andreiRightHandVi);
    }

    /**
     * {@code DELETE  /andrei-right-hand-vis/:id} : delete the "id" andreiRightHandVi.
     *
     * @param id the id of the andreiRightHandVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAndreiRightHandVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AndreiRightHandVi : {}", id);
        andreiRightHandViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

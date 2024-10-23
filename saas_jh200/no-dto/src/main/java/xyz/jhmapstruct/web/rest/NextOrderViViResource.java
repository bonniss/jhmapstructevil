package xyz.jhmapstruct.web.rest;

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
import xyz.jhmapstruct.domain.NextOrderViVi;
import xyz.jhmapstruct.repository.NextOrderViViRepository;
import xyz.jhmapstruct.service.NextOrderViViQueryService;
import xyz.jhmapstruct.service.NextOrderViViService;
import xyz.jhmapstruct.service.criteria.NextOrderViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderViVi}.
 */
@RestController
@RequestMapping("/api/next-order-vi-vis")
public class NextOrderViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViViResource.class);

    private static final String ENTITY_NAME = "nextOrderViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderViViService nextOrderViViService;

    private final NextOrderViViRepository nextOrderViViRepository;

    private final NextOrderViViQueryService nextOrderViViQueryService;

    public NextOrderViViResource(
        NextOrderViViService nextOrderViViService,
        NextOrderViViRepository nextOrderViViRepository,
        NextOrderViViQueryService nextOrderViViQueryService
    ) {
        this.nextOrderViViService = nextOrderViViService;
        this.nextOrderViViRepository = nextOrderViViRepository;
        this.nextOrderViViQueryService = nextOrderViViQueryService;
    }

    /**
     * {@code POST  /next-order-vi-vis} : Create a new nextOrderViVi.
     *
     * @param nextOrderViVi the nextOrderViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderViVi, or with status {@code 400 (Bad Request)} if the nextOrderViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderViVi> createNextOrderViVi(@Valid @RequestBody NextOrderViVi nextOrderViVi) throws URISyntaxException {
        LOG.debug("REST request to save NextOrderViVi : {}", nextOrderViVi);
        if (nextOrderViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderViVi = nextOrderViViService.save(nextOrderViVi);
        return ResponseEntity.created(new URI("/api/next-order-vi-vis/" + nextOrderViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderViVi.getId().toString()))
            .body(nextOrderViVi);
    }

    /**
     * {@code PUT  /next-order-vi-vis/:id} : Updates an existing nextOrderViVi.
     *
     * @param id the id of the nextOrderViVi to save.
     * @param nextOrderViVi the nextOrderViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderViVi,
     * or with status {@code 400 (Bad Request)} if the nextOrderViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderViVi> updateNextOrderViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderViVi nextOrderViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderViVi : {}, {}", id, nextOrderViVi);
        if (nextOrderViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderViVi = nextOrderViViService.update(nextOrderViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderViVi.getId().toString()))
            .body(nextOrderViVi);
    }

    /**
     * {@code PATCH  /next-order-vi-vis/:id} : Partial updates given fields of an existing nextOrderViVi, field will ignore if it is null
     *
     * @param id the id of the nextOrderViVi to save.
     * @param nextOrderViVi the nextOrderViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderViVi,
     * or with status {@code 400 (Bad Request)} if the nextOrderViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderViVi> partialUpdateNextOrderViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderViVi nextOrderViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderViVi partially : {}, {}", id, nextOrderViVi);
        if (nextOrderViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderViVi> result = nextOrderViViService.partialUpdate(nextOrderViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-vi-vis} : get all the nextOrderViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderViVi>> getAllNextOrderViVis(
        NextOrderViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderViVis by criteria: {}", criteria);

        Page<NextOrderViVi> page = nextOrderViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-vi-vis/count} : count all the nextOrderViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderViVis(NextOrderViViCriteria criteria) {
        LOG.debug("REST request to count NextOrderViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-vi-vis/:id} : get the "id" nextOrderViVi.
     *
     * @param id the id of the nextOrderViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderViVi> getNextOrderViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderViVi : {}", id);
        Optional<NextOrderViVi> nextOrderViVi = nextOrderViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderViVi);
    }

    /**
     * {@code DELETE  /next-order-vi-vis/:id} : delete the "id" nextOrderViVi.
     *
     * @param id the id of the nextOrderViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderViVi : {}", id);
        nextOrderViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

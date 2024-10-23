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
import xyz.jhmapstruct.domain.NextOrderVi;
import xyz.jhmapstruct.repository.NextOrderViRepository;
import xyz.jhmapstruct.service.NextOrderViQueryService;
import xyz.jhmapstruct.service.NextOrderViService;
import xyz.jhmapstruct.service.criteria.NextOrderViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderVi}.
 */
@RestController
@RequestMapping("/api/next-order-vis")
public class NextOrderViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderViResource.class);

    private static final String ENTITY_NAME = "nextOrderVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderViService nextOrderViService;

    private final NextOrderViRepository nextOrderViRepository;

    private final NextOrderViQueryService nextOrderViQueryService;

    public NextOrderViResource(
        NextOrderViService nextOrderViService,
        NextOrderViRepository nextOrderViRepository,
        NextOrderViQueryService nextOrderViQueryService
    ) {
        this.nextOrderViService = nextOrderViService;
        this.nextOrderViRepository = nextOrderViRepository;
        this.nextOrderViQueryService = nextOrderViQueryService;
    }

    /**
     * {@code POST  /next-order-vis} : Create a new nextOrderVi.
     *
     * @param nextOrderVi the nextOrderVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderVi, or with status {@code 400 (Bad Request)} if the nextOrderVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderVi> createNextOrderVi(@Valid @RequestBody NextOrderVi nextOrderVi) throws URISyntaxException {
        LOG.debug("REST request to save NextOrderVi : {}", nextOrderVi);
        if (nextOrderVi.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderVi = nextOrderViService.save(nextOrderVi);
        return ResponseEntity.created(new URI("/api/next-order-vis/" + nextOrderVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderVi.getId().toString()))
            .body(nextOrderVi);
    }

    /**
     * {@code PUT  /next-order-vis/:id} : Updates an existing nextOrderVi.
     *
     * @param id the id of the nextOrderVi to save.
     * @param nextOrderVi the nextOrderVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderVi,
     * or with status {@code 400 (Bad Request)} if the nextOrderVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderVi> updateNextOrderVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderVi nextOrderVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderVi : {}, {}", id, nextOrderVi);
        if (nextOrderVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderVi = nextOrderViService.update(nextOrderVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderVi.getId().toString()))
            .body(nextOrderVi);
    }

    /**
     * {@code PATCH  /next-order-vis/:id} : Partial updates given fields of an existing nextOrderVi, field will ignore if it is null
     *
     * @param id the id of the nextOrderVi to save.
     * @param nextOrderVi the nextOrderVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderVi,
     * or with status {@code 400 (Bad Request)} if the nextOrderVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderVi> partialUpdateNextOrderVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderVi nextOrderVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderVi partially : {}, {}", id, nextOrderVi);
        if (nextOrderVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderVi> result = nextOrderViService.partialUpdate(nextOrderVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-vis} : get all the nextOrderVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderVi>> getAllNextOrderVis(
        NextOrderViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderVis by criteria: {}", criteria);

        Page<NextOrderVi> page = nextOrderViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-vis/count} : count all the nextOrderVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderVis(NextOrderViCriteria criteria) {
        LOG.debug("REST request to count NextOrderVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-vis/:id} : get the "id" nextOrderVi.
     *
     * @param id the id of the nextOrderVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderVi> getNextOrderVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderVi : {}", id);
        Optional<NextOrderVi> nextOrderVi = nextOrderViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderVi);
    }

    /**
     * {@code DELETE  /next-order-vis/:id} : delete the "id" nextOrderVi.
     *
     * @param id the id of the nextOrderVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderVi : {}", id);
        nextOrderViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

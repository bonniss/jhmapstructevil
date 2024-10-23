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
import xyz.jhmapstruct.domain.NextSupplier;
import xyz.jhmapstruct.repository.NextSupplierRepository;
import xyz.jhmapstruct.service.NextSupplierQueryService;
import xyz.jhmapstruct.service.NextSupplierService;
import xyz.jhmapstruct.service.criteria.NextSupplierCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplier}.
 */
@RestController
@RequestMapping("/api/next-suppliers")
public class NextSupplierResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierResource.class);

    private static final String ENTITY_NAME = "nextSupplier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierService nextSupplierService;

    private final NextSupplierRepository nextSupplierRepository;

    private final NextSupplierQueryService nextSupplierQueryService;

    public NextSupplierResource(
        NextSupplierService nextSupplierService,
        NextSupplierRepository nextSupplierRepository,
        NextSupplierQueryService nextSupplierQueryService
    ) {
        this.nextSupplierService = nextSupplierService;
        this.nextSupplierRepository = nextSupplierRepository;
        this.nextSupplierQueryService = nextSupplierQueryService;
    }

    /**
     * {@code POST  /next-suppliers} : Create a new nextSupplier.
     *
     * @param nextSupplier the nextSupplier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplier, or with status {@code 400 (Bad Request)} if the nextSupplier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplier> createNextSupplier(@Valid @RequestBody NextSupplier nextSupplier) throws URISyntaxException {
        LOG.debug("REST request to save NextSupplier : {}", nextSupplier);
        if (nextSupplier.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplier = nextSupplierService.save(nextSupplier);
        return ResponseEntity.created(new URI("/api/next-suppliers/" + nextSupplier.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplier.getId().toString()))
            .body(nextSupplier);
    }

    /**
     * {@code PUT  /next-suppliers/:id} : Updates an existing nextSupplier.
     *
     * @param id the id of the nextSupplier to save.
     * @param nextSupplier the nextSupplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplier,
     * or with status {@code 400 (Bad Request)} if the nextSupplier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplier> updateNextSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplier nextSupplier
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplier : {}, {}", id, nextSupplier);
        if (nextSupplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplier = nextSupplierService.update(nextSupplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplier.getId().toString()))
            .body(nextSupplier);
    }

    /**
     * {@code PATCH  /next-suppliers/:id} : Partial updates given fields of an existing nextSupplier, field will ignore if it is null
     *
     * @param id the id of the nextSupplier to save.
     * @param nextSupplier the nextSupplier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplier,
     * or with status {@code 400 (Bad Request)} if the nextSupplier is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplier is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplier> partialUpdateNextSupplier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplier nextSupplier
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplier partially : {}, {}", id, nextSupplier);
        if (nextSupplier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplier> result = nextSupplierService.partialUpdate(nextSupplier);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplier.getId().toString())
        );
    }

    /**
     * {@code GET  /next-suppliers} : get all the nextSuppliers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSuppliers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplier>> getAllNextSuppliers(
        NextSupplierCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSuppliers by criteria: {}", criteria);

        Page<NextSupplier> page = nextSupplierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-suppliers/count} : count all the nextSuppliers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSuppliers(NextSupplierCriteria criteria) {
        LOG.debug("REST request to count NextSuppliers by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-suppliers/:id} : get the "id" nextSupplier.
     *
     * @param id the id of the nextSupplier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplier> getNextSupplier(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplier : {}", id);
        Optional<NextSupplier> nextSupplier = nextSupplierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplier);
    }

    /**
     * {@code DELETE  /next-suppliers/:id} : delete the "id" nextSupplier.
     *
     * @param id the id of the nextSupplier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplier(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplier : {}", id);
        nextSupplierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

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
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.repository.NextCustomerRepository;
import xyz.jhmapstruct.service.NextCustomerQueryService;
import xyz.jhmapstruct.service.NextCustomerService;
import xyz.jhmapstruct.service.criteria.NextCustomerCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomer}.
 */
@RestController
@RequestMapping("/api/next-customers")
public class NextCustomerResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerResource.class);

    private static final String ENTITY_NAME = "nextCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerService nextCustomerService;

    private final NextCustomerRepository nextCustomerRepository;

    private final NextCustomerQueryService nextCustomerQueryService;

    public NextCustomerResource(
        NextCustomerService nextCustomerService,
        NextCustomerRepository nextCustomerRepository,
        NextCustomerQueryService nextCustomerQueryService
    ) {
        this.nextCustomerService = nextCustomerService;
        this.nextCustomerRepository = nextCustomerRepository;
        this.nextCustomerQueryService = nextCustomerQueryService;
    }

    /**
     * {@code POST  /next-customers} : Create a new nextCustomer.
     *
     * @param nextCustomer the nextCustomer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomer, or with status {@code 400 (Bad Request)} if the nextCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomer> createNextCustomer(@Valid @RequestBody NextCustomer nextCustomer) throws URISyntaxException {
        LOG.debug("REST request to save NextCustomer : {}", nextCustomer);
        if (nextCustomer.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomer = nextCustomerService.save(nextCustomer);
        return ResponseEntity.created(new URI("/api/next-customers/" + nextCustomer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomer.getId().toString()))
            .body(nextCustomer);
    }

    /**
     * {@code PUT  /next-customers/:id} : Updates an existing nextCustomer.
     *
     * @param id the id of the nextCustomer to save.
     * @param nextCustomer the nextCustomer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomer,
     * or with status {@code 400 (Bad Request)} if the nextCustomer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomer> updateNextCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomer nextCustomer
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomer : {}, {}", id, nextCustomer);
        if (nextCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomer = nextCustomerService.update(nextCustomer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomer.getId().toString()))
            .body(nextCustomer);
    }

    /**
     * {@code PATCH  /next-customers/:id} : Partial updates given fields of an existing nextCustomer, field will ignore if it is null
     *
     * @param id the id of the nextCustomer to save.
     * @param nextCustomer the nextCustomer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomer,
     * or with status {@code 400 (Bad Request)} if the nextCustomer is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomer is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomer> partialUpdateNextCustomer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomer nextCustomer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomer partially : {}, {}", id, nextCustomer);
        if (nextCustomer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomer> result = nextCustomerService.partialUpdate(nextCustomer);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomer.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customers} : get all the nextCustomers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomer>> getAllNextCustomers(
        NextCustomerCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomers by criteria: {}", criteria);

        Page<NextCustomer> page = nextCustomerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customers/count} : count all the nextCustomers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomers(NextCustomerCriteria criteria) {
        LOG.debug("REST request to count NextCustomers by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customers/:id} : get the "id" nextCustomer.
     *
     * @param id the id of the nextCustomer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomer> getNextCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomer : {}", id);
        Optional<NextCustomer> nextCustomer = nextCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomer);
    }

    /**
     * {@code DELETE  /next-customers/:id} : delete the "id" nextCustomer.
     *
     * @param id the id of the nextCustomer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomer : {}", id);
        nextCustomerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

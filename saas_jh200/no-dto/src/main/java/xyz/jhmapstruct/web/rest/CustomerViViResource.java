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
import xyz.jhmapstruct.domain.CustomerViVi;
import xyz.jhmapstruct.repository.CustomerViViRepository;
import xyz.jhmapstruct.service.CustomerViViQueryService;
import xyz.jhmapstruct.service.CustomerViViService;
import xyz.jhmapstruct.service.criteria.CustomerViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerViVi}.
 */
@RestController
@RequestMapping("/api/customer-vi-vis")
public class CustomerViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViViResource.class);

    private static final String ENTITY_NAME = "customerViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerViViService customerViViService;

    private final CustomerViViRepository customerViViRepository;

    private final CustomerViViQueryService customerViViQueryService;

    public CustomerViViResource(
        CustomerViViService customerViViService,
        CustomerViViRepository customerViViRepository,
        CustomerViViQueryService customerViViQueryService
    ) {
        this.customerViViService = customerViViService;
        this.customerViViRepository = customerViViRepository;
        this.customerViViQueryService = customerViViQueryService;
    }

    /**
     * {@code POST  /customer-vi-vis} : Create a new customerViVi.
     *
     * @param customerViVi the customerViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerViVi, or with status {@code 400 (Bad Request)} if the customerViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerViVi> createCustomerViVi(@Valid @RequestBody CustomerViVi customerViVi) throws URISyntaxException {
        LOG.debug("REST request to save CustomerViVi : {}", customerViVi);
        if (customerViVi.getId() != null) {
            throw new BadRequestAlertException("A new customerViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerViVi = customerViViService.save(customerViVi);
        return ResponseEntity.created(new URI("/api/customer-vi-vis/" + customerViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerViVi.getId().toString()))
            .body(customerViVi);
    }

    /**
     * {@code PUT  /customer-vi-vis/:id} : Updates an existing customerViVi.
     *
     * @param id the id of the customerViVi to save.
     * @param customerViVi the customerViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerViVi,
     * or with status {@code 400 (Bad Request)} if the customerViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerViVi> updateCustomerViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerViVi customerViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerViVi : {}, {}", id, customerViVi);
        if (customerViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerViVi = customerViViService.update(customerViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerViVi.getId().toString()))
            .body(customerViVi);
    }

    /**
     * {@code PATCH  /customer-vi-vis/:id} : Partial updates given fields of an existing customerViVi, field will ignore if it is null
     *
     * @param id the id of the customerViVi to save.
     * @param customerViVi the customerViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerViVi,
     * or with status {@code 400 (Bad Request)} if the customerViVi is not valid,
     * or with status {@code 404 (Not Found)} if the customerViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerViVi> partialUpdateCustomerViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerViVi customerViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerViVi partially : {}, {}", id, customerViVi);
        if (customerViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerViVi> result = customerViViService.partialUpdate(customerViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-vi-vis} : get all the customerViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerViVi>> getAllCustomerViVis(
        CustomerViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerViVis by criteria: {}", criteria);

        Page<CustomerViVi> page = customerViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-vi-vis/count} : count all the customerViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerViVis(CustomerViViCriteria criteria) {
        LOG.debug("REST request to count CustomerViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-vi-vis/:id} : get the "id" customerViVi.
     *
     * @param id the id of the customerViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerViVi> getCustomerViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerViVi : {}", id);
        Optional<CustomerViVi> customerViVi = customerViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerViVi);
    }

    /**
     * {@code DELETE  /customer-vi-vis/:id} : delete the "id" customerViVi.
     *
     * @param id the id of the customerViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerViVi : {}", id);
        customerViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

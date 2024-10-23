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
import xyz.jhmapstruct.domain.CustomerVi;
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.CustomerViQueryService;
import xyz.jhmapstruct.service.CustomerViService;
import xyz.jhmapstruct.service.criteria.CustomerViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerVi}.
 */
@RestController
@RequestMapping("/api/customer-vis")
public class CustomerViResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerViResource.class);

    private static final String ENTITY_NAME = "customerVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerViService customerViService;

    private final CustomerViRepository customerViRepository;

    private final CustomerViQueryService customerViQueryService;

    public CustomerViResource(
        CustomerViService customerViService,
        CustomerViRepository customerViRepository,
        CustomerViQueryService customerViQueryService
    ) {
        this.customerViService = customerViService;
        this.customerViRepository = customerViRepository;
        this.customerViQueryService = customerViQueryService;
    }

    /**
     * {@code POST  /customer-vis} : Create a new customerVi.
     *
     * @param customerVi the customerVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerVi, or with status {@code 400 (Bad Request)} if the customerVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerVi> createCustomerVi(@Valid @RequestBody CustomerVi customerVi) throws URISyntaxException {
        LOG.debug("REST request to save CustomerVi : {}", customerVi);
        if (customerVi.getId() != null) {
            throw new BadRequestAlertException("A new customerVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerVi = customerViService.save(customerVi);
        return ResponseEntity.created(new URI("/api/customer-vis/" + customerVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerVi.getId().toString()))
            .body(customerVi);
    }

    /**
     * {@code PUT  /customer-vis/:id} : Updates an existing customerVi.
     *
     * @param id the id of the customerVi to save.
     * @param customerVi the customerVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerVi,
     * or with status {@code 400 (Bad Request)} if the customerVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerVi> updateCustomerVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerVi customerVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerVi : {}, {}", id, customerVi);
        if (customerVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerVi = customerViService.update(customerVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerVi.getId().toString()))
            .body(customerVi);
    }

    /**
     * {@code PATCH  /customer-vis/:id} : Partial updates given fields of an existing customerVi, field will ignore if it is null
     *
     * @param id the id of the customerVi to save.
     * @param customerVi the customerVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerVi,
     * or with status {@code 400 (Bad Request)} if the customerVi is not valid,
     * or with status {@code 404 (Not Found)} if the customerVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerVi> partialUpdateCustomerVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerVi customerVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerVi partially : {}, {}", id, customerVi);
        if (customerVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerVi> result = customerViService.partialUpdate(customerVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerVi.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-vis} : get all the customerVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerVi>> getAllCustomerVis(
        CustomerViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerVis by criteria: {}", criteria);

        Page<CustomerVi> page = customerViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-vis/count} : count all the customerVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerVis(CustomerViCriteria criteria) {
        LOG.debug("REST request to count CustomerVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-vis/:id} : get the "id" customerVi.
     *
     * @param id the id of the customerVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerVi> getCustomerVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerVi : {}", id);
        Optional<CustomerVi> customerVi = customerViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerVi);
    }

    /**
     * {@code DELETE  /customer-vis/:id} : delete the "id" customerVi.
     *
     * @param id the id of the customerVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerVi : {}", id);
        customerViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

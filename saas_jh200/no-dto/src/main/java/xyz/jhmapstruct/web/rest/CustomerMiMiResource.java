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
import xyz.jhmapstruct.domain.CustomerMiMi;
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.CustomerMiMiQueryService;
import xyz.jhmapstruct.service.CustomerMiMiService;
import xyz.jhmapstruct.service.criteria.CustomerMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerMiMi}.
 */
@RestController
@RequestMapping("/api/customer-mi-mis")
public class CustomerMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiMiResource.class);

    private static final String ENTITY_NAME = "customerMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerMiMiService customerMiMiService;

    private final CustomerMiMiRepository customerMiMiRepository;

    private final CustomerMiMiQueryService customerMiMiQueryService;

    public CustomerMiMiResource(
        CustomerMiMiService customerMiMiService,
        CustomerMiMiRepository customerMiMiRepository,
        CustomerMiMiQueryService customerMiMiQueryService
    ) {
        this.customerMiMiService = customerMiMiService;
        this.customerMiMiRepository = customerMiMiRepository;
        this.customerMiMiQueryService = customerMiMiQueryService;
    }

    /**
     * {@code POST  /customer-mi-mis} : Create a new customerMiMi.
     *
     * @param customerMiMi the customerMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerMiMi, or with status {@code 400 (Bad Request)} if the customerMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerMiMi> createCustomerMiMi(@Valid @RequestBody CustomerMiMi customerMiMi) throws URISyntaxException {
        LOG.debug("REST request to save CustomerMiMi : {}", customerMiMi);
        if (customerMiMi.getId() != null) {
            throw new BadRequestAlertException("A new customerMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerMiMi = customerMiMiService.save(customerMiMi);
        return ResponseEntity.created(new URI("/api/customer-mi-mis/" + customerMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerMiMi.getId().toString()))
            .body(customerMiMi);
    }

    /**
     * {@code PUT  /customer-mi-mis/:id} : Updates an existing customerMiMi.
     *
     * @param id the id of the customerMiMi to save.
     * @param customerMiMi the customerMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerMiMi,
     * or with status {@code 400 (Bad Request)} if the customerMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerMiMi> updateCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerMiMi customerMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerMiMi : {}, {}", id, customerMiMi);
        if (customerMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerMiMi = customerMiMiService.update(customerMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerMiMi.getId().toString()))
            .body(customerMiMi);
    }

    /**
     * {@code PATCH  /customer-mi-mis/:id} : Partial updates given fields of an existing customerMiMi, field will ignore if it is null
     *
     * @param id the id of the customerMiMi to save.
     * @param customerMiMi the customerMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerMiMi,
     * or with status {@code 400 (Bad Request)} if the customerMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the customerMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerMiMi> partialUpdateCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerMiMi customerMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerMiMi partially : {}, {}", id, customerMiMi);
        if (customerMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerMiMi> result = customerMiMiService.partialUpdate(customerMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-mi-mis} : get all the customerMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerMiMi>> getAllCustomerMiMis(
        CustomerMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerMiMis by criteria: {}", criteria);

        Page<CustomerMiMi> page = customerMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-mi-mis/count} : count all the customerMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerMiMis(CustomerMiMiCriteria criteria) {
        LOG.debug("REST request to count CustomerMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-mi-mis/:id} : get the "id" customerMiMi.
     *
     * @param id the id of the customerMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerMiMi> getCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerMiMi : {}", id);
        Optional<CustomerMiMi> customerMiMi = customerMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerMiMi);
    }

    /**
     * {@code DELETE  /customer-mi-mis/:id} : delete the "id" customerMiMi.
     *
     * @param id the id of the customerMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerMiMi : {}", id);
        customerMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

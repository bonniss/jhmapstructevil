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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.CustomerMi;
import xyz.jhmapstruct.repository.CustomerMiRepository;
import xyz.jhmapstruct.service.CustomerMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerMi}.
 */
@RestController
@RequestMapping("/api/customer-mis")
public class CustomerMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerMiResource.class);

    private static final String ENTITY_NAME = "customerMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerMiService customerMiService;

    private final CustomerMiRepository customerMiRepository;

    public CustomerMiResource(CustomerMiService customerMiService, CustomerMiRepository customerMiRepository) {
        this.customerMiService = customerMiService;
        this.customerMiRepository = customerMiRepository;
    }

    /**
     * {@code POST  /customer-mis} : Create a new customerMi.
     *
     * @param customerMi the customerMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerMi, or with status {@code 400 (Bad Request)} if the customerMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerMi> createCustomerMi(@Valid @RequestBody CustomerMi customerMi) throws URISyntaxException {
        LOG.debug("REST request to save CustomerMi : {}", customerMi);
        if (customerMi.getId() != null) {
            throw new BadRequestAlertException("A new customerMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerMi = customerMiService.save(customerMi);
        return ResponseEntity.created(new URI("/api/customer-mis/" + customerMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerMi.getId().toString()))
            .body(customerMi);
    }

    /**
     * {@code PUT  /customer-mis/:id} : Updates an existing customerMi.
     *
     * @param id the id of the customerMi to save.
     * @param customerMi the customerMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerMi,
     * or with status {@code 400 (Bad Request)} if the customerMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerMi> updateCustomerMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerMi customerMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerMi : {}, {}", id, customerMi);
        if (customerMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerMi = customerMiService.update(customerMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerMi.getId().toString()))
            .body(customerMi);
    }

    /**
     * {@code PATCH  /customer-mis/:id} : Partial updates given fields of an existing customerMi, field will ignore if it is null
     *
     * @param id the id of the customerMi to save.
     * @param customerMi the customerMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerMi,
     * or with status {@code 400 (Bad Request)} if the customerMi is not valid,
     * or with status {@code 404 (Not Found)} if the customerMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerMi> partialUpdateCustomerMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerMi customerMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerMi partially : {}, {}", id, customerMi);
        if (customerMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerMi> result = customerMiService.partialUpdate(customerMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerMi.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-mis} : get all the customerMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerMis in body.
     */
    @GetMapping("")
    public List<CustomerMi> getAllCustomerMis() {
        LOG.debug("REST request to get all CustomerMis");
        return customerMiService.findAll();
    }

    /**
     * {@code GET  /customer-mis/:id} : get the "id" customerMi.
     *
     * @param id the id of the customerMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerMi> getCustomerMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerMi : {}", id);
        Optional<CustomerMi> customerMi = customerMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerMi);
    }

    /**
     * {@code DELETE  /customer-mis/:id} : delete the "id" customerMi.
     *
     * @param id the id of the customerMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerMi : {}", id);
        customerMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

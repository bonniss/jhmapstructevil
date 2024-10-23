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
import xyz.jhmapstruct.domain.CustomerAlpha;
import xyz.jhmapstruct.repository.CustomerAlphaRepository;
import xyz.jhmapstruct.service.CustomerAlphaQueryService;
import xyz.jhmapstruct.service.CustomerAlphaService;
import xyz.jhmapstruct.service.criteria.CustomerAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerAlpha}.
 */
@RestController
@RequestMapping("/api/customer-alphas")
public class CustomerAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerAlphaResource.class);

    private static final String ENTITY_NAME = "customerAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerAlphaService customerAlphaService;

    private final CustomerAlphaRepository customerAlphaRepository;

    private final CustomerAlphaQueryService customerAlphaQueryService;

    public CustomerAlphaResource(
        CustomerAlphaService customerAlphaService,
        CustomerAlphaRepository customerAlphaRepository,
        CustomerAlphaQueryService customerAlphaQueryService
    ) {
        this.customerAlphaService = customerAlphaService;
        this.customerAlphaRepository = customerAlphaRepository;
        this.customerAlphaQueryService = customerAlphaQueryService;
    }

    /**
     * {@code POST  /customer-alphas} : Create a new customerAlpha.
     *
     * @param customerAlpha the customerAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerAlpha, or with status {@code 400 (Bad Request)} if the customerAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerAlpha> createCustomerAlpha(@Valid @RequestBody CustomerAlpha customerAlpha) throws URISyntaxException {
        LOG.debug("REST request to save CustomerAlpha : {}", customerAlpha);
        if (customerAlpha.getId() != null) {
            throw new BadRequestAlertException("A new customerAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerAlpha = customerAlphaService.save(customerAlpha);
        return ResponseEntity.created(new URI("/api/customer-alphas/" + customerAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerAlpha.getId().toString()))
            .body(customerAlpha);
    }

    /**
     * {@code PUT  /customer-alphas/:id} : Updates an existing customerAlpha.
     *
     * @param id the id of the customerAlpha to save.
     * @param customerAlpha the customerAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAlpha,
     * or with status {@code 400 (Bad Request)} if the customerAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerAlpha> updateCustomerAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerAlpha customerAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerAlpha : {}, {}", id, customerAlpha);
        if (customerAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerAlpha = customerAlphaService.update(customerAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerAlpha.getId().toString()))
            .body(customerAlpha);
    }

    /**
     * {@code PATCH  /customer-alphas/:id} : Partial updates given fields of an existing customerAlpha, field will ignore if it is null
     *
     * @param id the id of the customerAlpha to save.
     * @param customerAlpha the customerAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerAlpha,
     * or with status {@code 400 (Bad Request)} if the customerAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the customerAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerAlpha> partialUpdateCustomerAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerAlpha customerAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerAlpha partially : {}, {}", id, customerAlpha);
        if (customerAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerAlpha> result = customerAlphaService.partialUpdate(customerAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-alphas} : get all the customerAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerAlpha>> getAllCustomerAlphas(
        CustomerAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerAlphas by criteria: {}", criteria);

        Page<CustomerAlpha> page = customerAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-alphas/count} : count all the customerAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerAlphas(CustomerAlphaCriteria criteria) {
        LOG.debug("REST request to count CustomerAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-alphas/:id} : get the "id" customerAlpha.
     *
     * @param id the id of the customerAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerAlpha> getCustomerAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerAlpha : {}", id);
        Optional<CustomerAlpha> customerAlpha = customerAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAlpha);
    }

    /**
     * {@code DELETE  /customer-alphas/:id} : delete the "id" customerAlpha.
     *
     * @param id the id of the customerAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerAlpha : {}", id);
        customerAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

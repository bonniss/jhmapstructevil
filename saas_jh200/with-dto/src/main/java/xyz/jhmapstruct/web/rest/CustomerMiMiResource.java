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
import xyz.jhmapstruct.repository.CustomerMiMiRepository;
import xyz.jhmapstruct.service.CustomerMiMiQueryService;
import xyz.jhmapstruct.service.CustomerMiMiService;
import xyz.jhmapstruct.service.criteria.CustomerMiMiCriteria;
import xyz.jhmapstruct.service.dto.CustomerMiMiDTO;
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
     * @param customerMiMiDTO the customerMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerMiMiDTO, or with status {@code 400 (Bad Request)} if the customerMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerMiMiDTO> createCustomerMiMi(@Valid @RequestBody CustomerMiMiDTO customerMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerMiMi : {}", customerMiMiDTO);
        if (customerMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerMiMiDTO = customerMiMiService.save(customerMiMiDTO);
        return ResponseEntity.created(new URI("/api/customer-mi-mis/" + customerMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerMiMiDTO.getId().toString()))
            .body(customerMiMiDTO);
    }

    /**
     * {@code PUT  /customer-mi-mis/:id} : Updates an existing customerMiMi.
     *
     * @param id the id of the customerMiMiDTO to save.
     * @param customerMiMiDTO the customerMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the customerMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerMiMiDTO> updateCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerMiMiDTO customerMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerMiMi : {}, {}", id, customerMiMiDTO);
        if (customerMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerMiMiDTO = customerMiMiService.update(customerMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerMiMiDTO.getId().toString()))
            .body(customerMiMiDTO);
    }

    /**
     * {@code PATCH  /customer-mi-mis/:id} : Partial updates given fields of an existing customerMiMi, field will ignore if it is null
     *
     * @param id the id of the customerMiMiDTO to save.
     * @param customerMiMiDTO the customerMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the customerMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerMiMiDTO> partialUpdateCustomerMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerMiMiDTO customerMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerMiMi partially : {}, {}", id, customerMiMiDTO);
        if (customerMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerMiMiDTO> result = customerMiMiService.partialUpdate(customerMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerMiMiDTO.getId().toString())
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
    public ResponseEntity<List<CustomerMiMiDTO>> getAllCustomerMiMis(
        CustomerMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerMiMis by criteria: {}", criteria);

        Page<CustomerMiMiDTO> page = customerMiMiQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the customerMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerMiMiDTO> getCustomerMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerMiMi : {}", id);
        Optional<CustomerMiMiDTO> customerMiMiDTO = customerMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerMiMiDTO);
    }

    /**
     * {@code DELETE  /customer-mi-mis/:id} : delete the "id" customerMiMi.
     *
     * @param id the id of the customerMiMiDTO to delete.
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

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
import xyz.jhmapstruct.repository.CustomerGammaRepository;
import xyz.jhmapstruct.service.CustomerGammaQueryService;
import xyz.jhmapstruct.service.CustomerGammaService;
import xyz.jhmapstruct.service.criteria.CustomerGammaCriteria;
import xyz.jhmapstruct.service.dto.CustomerGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerGamma}.
 */
@RestController
@RequestMapping("/api/customer-gammas")
public class CustomerGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerGammaResource.class);

    private static final String ENTITY_NAME = "customerGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerGammaService customerGammaService;

    private final CustomerGammaRepository customerGammaRepository;

    private final CustomerGammaQueryService customerGammaQueryService;

    public CustomerGammaResource(
        CustomerGammaService customerGammaService,
        CustomerGammaRepository customerGammaRepository,
        CustomerGammaQueryService customerGammaQueryService
    ) {
        this.customerGammaService = customerGammaService;
        this.customerGammaRepository = customerGammaRepository;
        this.customerGammaQueryService = customerGammaQueryService;
    }

    /**
     * {@code POST  /customer-gammas} : Create a new customerGamma.
     *
     * @param customerGammaDTO the customerGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerGammaDTO, or with status {@code 400 (Bad Request)} if the customerGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerGammaDTO> createCustomerGamma(@Valid @RequestBody CustomerGammaDTO customerGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerGamma : {}", customerGammaDTO);
        if (customerGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerGammaDTO = customerGammaService.save(customerGammaDTO);
        return ResponseEntity.created(new URI("/api/customer-gammas/" + customerGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerGammaDTO.getId().toString()))
            .body(customerGammaDTO);
    }

    /**
     * {@code PUT  /customer-gammas/:id} : Updates an existing customerGamma.
     *
     * @param id the id of the customerGammaDTO to save.
     * @param customerGammaDTO the customerGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerGammaDTO,
     * or with status {@code 400 (Bad Request)} if the customerGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerGammaDTO> updateCustomerGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerGammaDTO customerGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerGamma : {}, {}", id, customerGammaDTO);
        if (customerGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerGammaDTO = customerGammaService.update(customerGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerGammaDTO.getId().toString()))
            .body(customerGammaDTO);
    }

    /**
     * {@code PATCH  /customer-gammas/:id} : Partial updates given fields of an existing customerGamma, field will ignore if it is null
     *
     * @param id the id of the customerGammaDTO to save.
     * @param customerGammaDTO the customerGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerGammaDTO,
     * or with status {@code 400 (Bad Request)} if the customerGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerGammaDTO> partialUpdateCustomerGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerGammaDTO customerGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerGamma partially : {}, {}", id, customerGammaDTO);
        if (customerGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerGammaDTO> result = customerGammaService.partialUpdate(customerGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-gammas} : get all the customerGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerGammaDTO>> getAllCustomerGammas(
        CustomerGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerGammas by criteria: {}", criteria);

        Page<CustomerGammaDTO> page = customerGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-gammas/count} : count all the customerGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerGammas(CustomerGammaCriteria criteria) {
        LOG.debug("REST request to count CustomerGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-gammas/:id} : get the "id" customerGamma.
     *
     * @param id the id of the customerGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerGammaDTO> getCustomerGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerGamma : {}", id);
        Optional<CustomerGammaDTO> customerGammaDTO = customerGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerGammaDTO);
    }

    /**
     * {@code DELETE  /customer-gammas/:id} : delete the "id" customerGamma.
     *
     * @param id the id of the customerGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerGamma : {}", id);
        customerGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

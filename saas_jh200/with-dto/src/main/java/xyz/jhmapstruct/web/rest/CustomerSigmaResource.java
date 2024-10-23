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
import xyz.jhmapstruct.repository.CustomerSigmaRepository;
import xyz.jhmapstruct.service.CustomerSigmaQueryService;
import xyz.jhmapstruct.service.CustomerSigmaService;
import xyz.jhmapstruct.service.criteria.CustomerSigmaCriteria;
import xyz.jhmapstruct.service.dto.CustomerSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerSigma}.
 */
@RestController
@RequestMapping("/api/customer-sigmas")
public class CustomerSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerSigmaResource.class);

    private static final String ENTITY_NAME = "customerSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerSigmaService customerSigmaService;

    private final CustomerSigmaRepository customerSigmaRepository;

    private final CustomerSigmaQueryService customerSigmaQueryService;

    public CustomerSigmaResource(
        CustomerSigmaService customerSigmaService,
        CustomerSigmaRepository customerSigmaRepository,
        CustomerSigmaQueryService customerSigmaQueryService
    ) {
        this.customerSigmaService = customerSigmaService;
        this.customerSigmaRepository = customerSigmaRepository;
        this.customerSigmaQueryService = customerSigmaQueryService;
    }

    /**
     * {@code POST  /customer-sigmas} : Create a new customerSigma.
     *
     * @param customerSigmaDTO the customerSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerSigmaDTO, or with status {@code 400 (Bad Request)} if the customerSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerSigmaDTO> createCustomerSigma(@Valid @RequestBody CustomerSigmaDTO customerSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerSigma : {}", customerSigmaDTO);
        if (customerSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerSigmaDTO = customerSigmaService.save(customerSigmaDTO);
        return ResponseEntity.created(new URI("/api/customer-sigmas/" + customerSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerSigmaDTO.getId().toString()))
            .body(customerSigmaDTO);
    }

    /**
     * {@code PUT  /customer-sigmas/:id} : Updates an existing customerSigma.
     *
     * @param id the id of the customerSigmaDTO to save.
     * @param customerSigmaDTO the customerSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the customerSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerSigmaDTO> updateCustomerSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerSigmaDTO customerSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerSigma : {}, {}", id, customerSigmaDTO);
        if (customerSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerSigmaDTO = customerSigmaService.update(customerSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerSigmaDTO.getId().toString()))
            .body(customerSigmaDTO);
    }

    /**
     * {@code PATCH  /customer-sigmas/:id} : Partial updates given fields of an existing customerSigma, field will ignore if it is null
     *
     * @param id the id of the customerSigmaDTO to save.
     * @param customerSigmaDTO the customerSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the customerSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerSigmaDTO> partialUpdateCustomerSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerSigmaDTO customerSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerSigma partially : {}, {}", id, customerSigmaDTO);
        if (customerSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerSigmaDTO> result = customerSigmaService.partialUpdate(customerSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-sigmas} : get all the customerSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerSigmaDTO>> getAllCustomerSigmas(
        CustomerSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerSigmas by criteria: {}", criteria);

        Page<CustomerSigmaDTO> page = customerSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-sigmas/count} : count all the customerSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerSigmas(CustomerSigmaCriteria criteria) {
        LOG.debug("REST request to count CustomerSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-sigmas/:id} : get the "id" customerSigma.
     *
     * @param id the id of the customerSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerSigmaDTO> getCustomerSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerSigma : {}", id);
        Optional<CustomerSigmaDTO> customerSigmaDTO = customerSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerSigmaDTO);
    }

    /**
     * {@code DELETE  /customer-sigmas/:id} : delete the "id" customerSigma.
     *
     * @param id the id of the customerSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerSigma : {}", id);
        customerSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

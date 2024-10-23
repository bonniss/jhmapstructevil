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
import xyz.jhmapstruct.repository.CustomerBetaRepository;
import xyz.jhmapstruct.service.CustomerBetaQueryService;
import xyz.jhmapstruct.service.CustomerBetaService;
import xyz.jhmapstruct.service.criteria.CustomerBetaCriteria;
import xyz.jhmapstruct.service.dto.CustomerBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerBeta}.
 */
@RestController
@RequestMapping("/api/customer-betas")
public class CustomerBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerBetaResource.class);

    private static final String ENTITY_NAME = "customerBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerBetaService customerBetaService;

    private final CustomerBetaRepository customerBetaRepository;

    private final CustomerBetaQueryService customerBetaQueryService;

    public CustomerBetaResource(
        CustomerBetaService customerBetaService,
        CustomerBetaRepository customerBetaRepository,
        CustomerBetaQueryService customerBetaQueryService
    ) {
        this.customerBetaService = customerBetaService;
        this.customerBetaRepository = customerBetaRepository;
        this.customerBetaQueryService = customerBetaQueryService;
    }

    /**
     * {@code POST  /customer-betas} : Create a new customerBeta.
     *
     * @param customerBetaDTO the customerBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerBetaDTO, or with status {@code 400 (Bad Request)} if the customerBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerBetaDTO> createCustomerBeta(@Valid @RequestBody CustomerBetaDTO customerBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerBeta : {}", customerBetaDTO);
        if (customerBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerBetaDTO = customerBetaService.save(customerBetaDTO);
        return ResponseEntity.created(new URI("/api/customer-betas/" + customerBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerBetaDTO.getId().toString()))
            .body(customerBetaDTO);
    }

    /**
     * {@code PUT  /customer-betas/:id} : Updates an existing customerBeta.
     *
     * @param id the id of the customerBetaDTO to save.
     * @param customerBetaDTO the customerBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerBetaDTO,
     * or with status {@code 400 (Bad Request)} if the customerBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerBetaDTO> updateCustomerBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerBetaDTO customerBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerBeta : {}, {}", id, customerBetaDTO);
        if (customerBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerBetaDTO = customerBetaService.update(customerBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerBetaDTO.getId().toString()))
            .body(customerBetaDTO);
    }

    /**
     * {@code PATCH  /customer-betas/:id} : Partial updates given fields of an existing customerBeta, field will ignore if it is null
     *
     * @param id the id of the customerBetaDTO to save.
     * @param customerBetaDTO the customerBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerBetaDTO,
     * or with status {@code 400 (Bad Request)} if the customerBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerBetaDTO> partialUpdateCustomerBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerBetaDTO customerBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerBeta partially : {}, {}", id, customerBetaDTO);
        if (customerBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerBetaDTO> result = customerBetaService.partialUpdate(customerBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-betas} : get all the customerBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerBetaDTO>> getAllCustomerBetas(
        CustomerBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerBetas by criteria: {}", criteria);

        Page<CustomerBetaDTO> page = customerBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-betas/count} : count all the customerBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerBetas(CustomerBetaCriteria criteria) {
        LOG.debug("REST request to count CustomerBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-betas/:id} : get the "id" customerBeta.
     *
     * @param id the id of the customerBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerBetaDTO> getCustomerBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerBeta : {}", id);
        Optional<CustomerBetaDTO> customerBetaDTO = customerBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerBetaDTO);
    }

    /**
     * {@code DELETE  /customer-betas/:id} : delete the "id" customerBeta.
     *
     * @param id the id of the customerBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerBeta : {}", id);
        customerBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

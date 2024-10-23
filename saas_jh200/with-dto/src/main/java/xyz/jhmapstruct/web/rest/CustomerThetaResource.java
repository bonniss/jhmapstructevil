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
import xyz.jhmapstruct.repository.CustomerThetaRepository;
import xyz.jhmapstruct.service.CustomerThetaQueryService;
import xyz.jhmapstruct.service.CustomerThetaService;
import xyz.jhmapstruct.service.criteria.CustomerThetaCriteria;
import xyz.jhmapstruct.service.dto.CustomerThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.CustomerTheta}.
 */
@RestController
@RequestMapping("/api/customer-thetas")
public class CustomerThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerThetaResource.class);

    private static final String ENTITY_NAME = "customerTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerThetaService customerThetaService;

    private final CustomerThetaRepository customerThetaRepository;

    private final CustomerThetaQueryService customerThetaQueryService;

    public CustomerThetaResource(
        CustomerThetaService customerThetaService,
        CustomerThetaRepository customerThetaRepository,
        CustomerThetaQueryService customerThetaQueryService
    ) {
        this.customerThetaService = customerThetaService;
        this.customerThetaRepository = customerThetaRepository;
        this.customerThetaQueryService = customerThetaQueryService;
    }

    /**
     * {@code POST  /customer-thetas} : Create a new customerTheta.
     *
     * @param customerThetaDTO the customerThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerThetaDTO, or with status {@code 400 (Bad Request)} if the customerTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerThetaDTO> createCustomerTheta(@Valid @RequestBody CustomerThetaDTO customerThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CustomerTheta : {}", customerThetaDTO);
        if (customerThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerThetaDTO = customerThetaService.save(customerThetaDTO);
        return ResponseEntity.created(new URI("/api/customer-thetas/" + customerThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerThetaDTO.getId().toString()))
            .body(customerThetaDTO);
    }

    /**
     * {@code PUT  /customer-thetas/:id} : Updates an existing customerTheta.
     *
     * @param id the id of the customerThetaDTO to save.
     * @param customerThetaDTO the customerThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerThetaDTO,
     * or with status {@code 400 (Bad Request)} if the customerThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerThetaDTO> updateCustomerTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerThetaDTO customerThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerTheta : {}, {}", id, customerThetaDTO);
        if (customerThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerThetaDTO = customerThetaService.update(customerThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerThetaDTO.getId().toString()))
            .body(customerThetaDTO);
    }

    /**
     * {@code PATCH  /customer-thetas/:id} : Partial updates given fields of an existing customerTheta, field will ignore if it is null
     *
     * @param id the id of the customerThetaDTO to save.
     * @param customerThetaDTO the customerThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerThetaDTO,
     * or with status {@code 400 (Bad Request)} if the customerThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerThetaDTO> partialUpdateCustomerTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerThetaDTO customerThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerTheta partially : {}, {}", id, customerThetaDTO);
        if (customerThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerThetaDTO> result = customerThetaService.partialUpdate(customerThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-thetas} : get all the customerThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CustomerThetaDTO>> getAllCustomerThetas(
        CustomerThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get CustomerThetas by criteria: {}", criteria);

        Page<CustomerThetaDTO> page = customerThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-thetas/count} : count all the customerThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCustomerThetas(CustomerThetaCriteria criteria) {
        LOG.debug("REST request to count CustomerThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-thetas/:id} : get the "id" customerTheta.
     *
     * @param id the id of the customerThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerThetaDTO> getCustomerTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerTheta : {}", id);
        Optional<CustomerThetaDTO> customerThetaDTO = customerThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerThetaDTO);
    }

    /**
     * {@code DELETE  /customer-thetas/:id} : delete the "id" customerTheta.
     *
     * @param id the id of the customerThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CustomerTheta : {}", id);
        customerThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

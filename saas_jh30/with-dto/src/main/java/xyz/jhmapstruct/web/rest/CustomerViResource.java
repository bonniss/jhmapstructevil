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
import xyz.jhmapstruct.repository.CustomerViRepository;
import xyz.jhmapstruct.service.CustomerViService;
import xyz.jhmapstruct.service.dto.CustomerViDTO;
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

    public CustomerViResource(CustomerViService customerViService, CustomerViRepository customerViRepository) {
        this.customerViService = customerViService;
        this.customerViRepository = customerViRepository;
    }

    /**
     * {@code POST  /customer-vis} : Create a new customerVi.
     *
     * @param customerViDTO the customerViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerViDTO, or with status {@code 400 (Bad Request)} if the customerVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomerViDTO> createCustomerVi(@Valid @RequestBody CustomerViDTO customerViDTO) throws URISyntaxException {
        LOG.debug("REST request to save CustomerVi : {}", customerViDTO);
        if (customerViDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customerViDTO = customerViService.save(customerViDTO);
        return ResponseEntity.created(new URI("/api/customer-vis/" + customerViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customerViDTO.getId().toString()))
            .body(customerViDTO);
    }

    /**
     * {@code PUT  /customer-vis/:id} : Updates an existing customerVi.
     *
     * @param id the id of the customerViDTO to save.
     * @param customerViDTO the customerViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerViDTO,
     * or with status {@code 400 (Bad Request)} if the customerViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerViDTO> updateCustomerVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerViDTO customerViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CustomerVi : {}, {}", id, customerViDTO);
        if (customerViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customerViDTO = customerViService.update(customerViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerViDTO.getId().toString()))
            .body(customerViDTO);
    }

    /**
     * {@code PATCH  /customer-vis/:id} : Partial updates given fields of an existing customerVi, field will ignore if it is null
     *
     * @param id the id of the customerViDTO to save.
     * @param customerViDTO the customerViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerViDTO,
     * or with status {@code 400 (Bad Request)} if the customerViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerViDTO> partialUpdateCustomerVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerViDTO customerViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CustomerVi partially : {}, {}", id, customerViDTO);
        if (customerViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerViDTO> result = customerViService.partialUpdate(customerViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-vis} : get all the customerVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerVis in body.
     */
    @GetMapping("")
    public List<CustomerViDTO> getAllCustomerVis() {
        LOG.debug("REST request to get all CustomerVis");
        return customerViService.findAll();
    }

    /**
     * {@code GET  /customer-vis/:id} : get the "id" customerVi.
     *
     * @param id the id of the customerViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerViDTO> getCustomerVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CustomerVi : {}", id);
        Optional<CustomerViDTO> customerViDTO = customerViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerViDTO);
    }

    /**
     * {@code DELETE  /customer-vis/:id} : delete the "id" customerVi.
     *
     * @param id the id of the customerViDTO to delete.
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

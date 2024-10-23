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
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.SupplierViQueryService;
import xyz.jhmapstruct.service.SupplierViService;
import xyz.jhmapstruct.service.criteria.SupplierViCriteria;
import xyz.jhmapstruct.service.dto.SupplierViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
@RestController
@RequestMapping("/api/supplier-vis")
public class SupplierViResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViResource.class);

    private static final String ENTITY_NAME = "supplierVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierViService supplierViService;

    private final SupplierViRepository supplierViRepository;

    private final SupplierViQueryService supplierViQueryService;

    public SupplierViResource(
        SupplierViService supplierViService,
        SupplierViRepository supplierViRepository,
        SupplierViQueryService supplierViQueryService
    ) {
        this.supplierViService = supplierViService;
        this.supplierViRepository = supplierViRepository;
        this.supplierViQueryService = supplierViQueryService;
    }

    /**
     * {@code POST  /supplier-vis} : Create a new supplierVi.
     *
     * @param supplierViDTO the supplierViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierViDTO, or with status {@code 400 (Bad Request)} if the supplierVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierViDTO> createSupplierVi(@Valid @RequestBody SupplierViDTO supplierViDTO) throws URISyntaxException {
        LOG.debug("REST request to save SupplierVi : {}", supplierViDTO);
        if (supplierViDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierViDTO = supplierViService.save(supplierViDTO);
        return ResponseEntity.created(new URI("/api/supplier-vis/" + supplierViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierViDTO.getId().toString()))
            .body(supplierViDTO);
    }

    /**
     * {@code PUT  /supplier-vis/:id} : Updates an existing supplierVi.
     *
     * @param id the id of the supplierViDTO to save.
     * @param supplierViDTO the supplierViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierViDTO,
     * or with status {@code 400 (Bad Request)} if the supplierViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierViDTO> updateSupplierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierViDTO supplierViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierVi : {}, {}", id, supplierViDTO);
        if (supplierViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierViDTO = supplierViService.update(supplierViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierViDTO.getId().toString()))
            .body(supplierViDTO);
    }

    /**
     * {@code PATCH  /supplier-vis/:id} : Partial updates given fields of an existing supplierVi, field will ignore if it is null
     *
     * @param id the id of the supplierViDTO to save.
     * @param supplierViDTO the supplierViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierViDTO,
     * or with status {@code 400 (Bad Request)} if the supplierViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierViDTO> partialUpdateSupplierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierViDTO supplierViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierVi partially : {}, {}", id, supplierViDTO);
        if (supplierViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierViDTO> result = supplierViService.partialUpdate(supplierViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-vis} : get all the supplierVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierViDTO>> getAllSupplierVis(
        SupplierViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierVis by criteria: {}", criteria);

        Page<SupplierViDTO> page = supplierViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-vis/count} : count all the supplierVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierVis(SupplierViCriteria criteria) {
        LOG.debug("REST request to count SupplierVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-vis/:id} : get the "id" supplierVi.
     *
     * @param id the id of the supplierViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierViDTO> getSupplierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierVi : {}", id);
        Optional<SupplierViDTO> supplierViDTO = supplierViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierViDTO);
    }

    /**
     * {@code DELETE  /supplier-vis/:id} : delete the "id" supplierVi.
     *
     * @param id the id of the supplierViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierVi : {}", id);
        supplierViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

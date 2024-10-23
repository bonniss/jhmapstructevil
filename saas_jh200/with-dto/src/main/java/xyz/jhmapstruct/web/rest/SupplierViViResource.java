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
import xyz.jhmapstruct.repository.SupplierViViRepository;
import xyz.jhmapstruct.service.SupplierViViQueryService;
import xyz.jhmapstruct.service.SupplierViViService;
import xyz.jhmapstruct.service.criteria.SupplierViViCriteria;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierViVi}.
 */
@RestController
@RequestMapping("/api/supplier-vi-vis")
public class SupplierViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViViResource.class);

    private static final String ENTITY_NAME = "supplierViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierViViService supplierViViService;

    private final SupplierViViRepository supplierViViRepository;

    private final SupplierViViQueryService supplierViViQueryService;

    public SupplierViViResource(
        SupplierViViService supplierViViService,
        SupplierViViRepository supplierViViRepository,
        SupplierViViQueryService supplierViViQueryService
    ) {
        this.supplierViViService = supplierViViService;
        this.supplierViViRepository = supplierViViRepository;
        this.supplierViViQueryService = supplierViViQueryService;
    }

    /**
     * {@code POST  /supplier-vi-vis} : Create a new supplierViVi.
     *
     * @param supplierViViDTO the supplierViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierViViDTO, or with status {@code 400 (Bad Request)} if the supplierViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierViViDTO> createSupplierViVi(@Valid @RequestBody SupplierViViDTO supplierViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierViVi : {}", supplierViViDTO);
        if (supplierViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierViViDTO = supplierViViService.save(supplierViViDTO);
        return ResponseEntity.created(new URI("/api/supplier-vi-vis/" + supplierViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierViViDTO.getId().toString()))
            .body(supplierViViDTO);
    }

    /**
     * {@code PUT  /supplier-vi-vis/:id} : Updates an existing supplierViVi.
     *
     * @param id the id of the supplierViViDTO to save.
     * @param supplierViViDTO the supplierViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierViViDTO,
     * or with status {@code 400 (Bad Request)} if the supplierViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierViViDTO> updateSupplierViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierViViDTO supplierViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierViVi : {}, {}", id, supplierViViDTO);
        if (supplierViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierViViDTO = supplierViViService.update(supplierViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierViViDTO.getId().toString()))
            .body(supplierViViDTO);
    }

    /**
     * {@code PATCH  /supplier-vi-vis/:id} : Partial updates given fields of an existing supplierViVi, field will ignore if it is null
     *
     * @param id the id of the supplierViViDTO to save.
     * @param supplierViViDTO the supplierViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierViViDTO,
     * or with status {@code 400 (Bad Request)} if the supplierViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierViViDTO> partialUpdateSupplierViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierViViDTO supplierViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierViVi partially : {}, {}", id, supplierViViDTO);
        if (supplierViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierViViDTO> result = supplierViViService.partialUpdate(supplierViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-vi-vis} : get all the supplierViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierViViDTO>> getAllSupplierViVis(
        SupplierViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierViVis by criteria: {}", criteria);

        Page<SupplierViViDTO> page = supplierViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-vi-vis/count} : count all the supplierViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierViVis(SupplierViViCriteria criteria) {
        LOG.debug("REST request to count SupplierViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-vi-vis/:id} : get the "id" supplierViVi.
     *
     * @param id the id of the supplierViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierViViDTO> getSupplierViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierViVi : {}", id);
        Optional<SupplierViViDTO> supplierViViDTO = supplierViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierViViDTO);
    }

    /**
     * {@code DELETE  /supplier-vi-vis/:id} : delete the "id" supplierViVi.
     *
     * @param id the id of the supplierViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierViVi : {}", id);
        supplierViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

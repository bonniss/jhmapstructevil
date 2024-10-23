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
import xyz.jhmapstruct.repository.SupplierBetaRepository;
import xyz.jhmapstruct.service.SupplierBetaQueryService;
import xyz.jhmapstruct.service.SupplierBetaService;
import xyz.jhmapstruct.service.criteria.SupplierBetaCriteria;
import xyz.jhmapstruct.service.dto.SupplierBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierBeta}.
 */
@RestController
@RequestMapping("/api/supplier-betas")
public class SupplierBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierBetaResource.class);

    private static final String ENTITY_NAME = "supplierBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierBetaService supplierBetaService;

    private final SupplierBetaRepository supplierBetaRepository;

    private final SupplierBetaQueryService supplierBetaQueryService;

    public SupplierBetaResource(
        SupplierBetaService supplierBetaService,
        SupplierBetaRepository supplierBetaRepository,
        SupplierBetaQueryService supplierBetaQueryService
    ) {
        this.supplierBetaService = supplierBetaService;
        this.supplierBetaRepository = supplierBetaRepository;
        this.supplierBetaQueryService = supplierBetaQueryService;
    }

    /**
     * {@code POST  /supplier-betas} : Create a new supplierBeta.
     *
     * @param supplierBetaDTO the supplierBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierBetaDTO, or with status {@code 400 (Bad Request)} if the supplierBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierBetaDTO> createSupplierBeta(@Valid @RequestBody SupplierBetaDTO supplierBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierBeta : {}", supplierBetaDTO);
        if (supplierBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierBetaDTO = supplierBetaService.save(supplierBetaDTO);
        return ResponseEntity.created(new URI("/api/supplier-betas/" + supplierBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierBetaDTO.getId().toString()))
            .body(supplierBetaDTO);
    }

    /**
     * {@code PUT  /supplier-betas/:id} : Updates an existing supplierBeta.
     *
     * @param id the id of the supplierBetaDTO to save.
     * @param supplierBetaDTO the supplierBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierBetaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierBetaDTO> updateSupplierBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierBetaDTO supplierBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierBeta : {}, {}", id, supplierBetaDTO);
        if (supplierBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierBetaDTO = supplierBetaService.update(supplierBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierBetaDTO.getId().toString()))
            .body(supplierBetaDTO);
    }

    /**
     * {@code PATCH  /supplier-betas/:id} : Partial updates given fields of an existing supplierBeta, field will ignore if it is null
     *
     * @param id the id of the supplierBetaDTO to save.
     * @param supplierBetaDTO the supplierBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierBetaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierBetaDTO> partialUpdateSupplierBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierBetaDTO supplierBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierBeta partially : {}, {}", id, supplierBetaDTO);
        if (supplierBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierBetaDTO> result = supplierBetaService.partialUpdate(supplierBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-betas} : get all the supplierBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierBetaDTO>> getAllSupplierBetas(
        SupplierBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierBetas by criteria: {}", criteria);

        Page<SupplierBetaDTO> page = supplierBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-betas/count} : count all the supplierBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierBetas(SupplierBetaCriteria criteria) {
        LOG.debug("REST request to count SupplierBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-betas/:id} : get the "id" supplierBeta.
     *
     * @param id the id of the supplierBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierBetaDTO> getSupplierBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierBeta : {}", id);
        Optional<SupplierBetaDTO> supplierBetaDTO = supplierBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierBetaDTO);
    }

    /**
     * {@code DELETE  /supplier-betas/:id} : delete the "id" supplierBeta.
     *
     * @param id the id of the supplierBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierBeta : {}", id);
        supplierBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

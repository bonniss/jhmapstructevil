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
import xyz.jhmapstruct.repository.SupplierThetaRepository;
import xyz.jhmapstruct.service.SupplierThetaQueryService;
import xyz.jhmapstruct.service.SupplierThetaService;
import xyz.jhmapstruct.service.criteria.SupplierThetaCriteria;
import xyz.jhmapstruct.service.dto.SupplierThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierTheta}.
 */
@RestController
@RequestMapping("/api/supplier-thetas")
public class SupplierThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierThetaResource.class);

    private static final String ENTITY_NAME = "supplierTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierThetaService supplierThetaService;

    private final SupplierThetaRepository supplierThetaRepository;

    private final SupplierThetaQueryService supplierThetaQueryService;

    public SupplierThetaResource(
        SupplierThetaService supplierThetaService,
        SupplierThetaRepository supplierThetaRepository,
        SupplierThetaQueryService supplierThetaQueryService
    ) {
        this.supplierThetaService = supplierThetaService;
        this.supplierThetaRepository = supplierThetaRepository;
        this.supplierThetaQueryService = supplierThetaQueryService;
    }

    /**
     * {@code POST  /supplier-thetas} : Create a new supplierTheta.
     *
     * @param supplierThetaDTO the supplierThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierThetaDTO, or with status {@code 400 (Bad Request)} if the supplierTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierThetaDTO> createSupplierTheta(@Valid @RequestBody SupplierThetaDTO supplierThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierTheta : {}", supplierThetaDTO);
        if (supplierThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierThetaDTO = supplierThetaService.save(supplierThetaDTO);
        return ResponseEntity.created(new URI("/api/supplier-thetas/" + supplierThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierThetaDTO.getId().toString()))
            .body(supplierThetaDTO);
    }

    /**
     * {@code PUT  /supplier-thetas/:id} : Updates an existing supplierTheta.
     *
     * @param id the id of the supplierThetaDTO to save.
     * @param supplierThetaDTO the supplierThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierThetaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierThetaDTO> updateSupplierTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierThetaDTO supplierThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierTheta : {}, {}", id, supplierThetaDTO);
        if (supplierThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierThetaDTO = supplierThetaService.update(supplierThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierThetaDTO.getId().toString()))
            .body(supplierThetaDTO);
    }

    /**
     * {@code PATCH  /supplier-thetas/:id} : Partial updates given fields of an existing supplierTheta, field will ignore if it is null
     *
     * @param id the id of the supplierThetaDTO to save.
     * @param supplierThetaDTO the supplierThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierThetaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierThetaDTO> partialUpdateSupplierTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierThetaDTO supplierThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierTheta partially : {}, {}", id, supplierThetaDTO);
        if (supplierThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierThetaDTO> result = supplierThetaService.partialUpdate(supplierThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-thetas} : get all the supplierThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierThetaDTO>> getAllSupplierThetas(
        SupplierThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierThetas by criteria: {}", criteria);

        Page<SupplierThetaDTO> page = supplierThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-thetas/count} : count all the supplierThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierThetas(SupplierThetaCriteria criteria) {
        LOG.debug("REST request to count SupplierThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-thetas/:id} : get the "id" supplierTheta.
     *
     * @param id the id of the supplierThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierThetaDTO> getSupplierTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierTheta : {}", id);
        Optional<SupplierThetaDTO> supplierThetaDTO = supplierThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierThetaDTO);
    }

    /**
     * {@code DELETE  /supplier-thetas/:id} : delete the "id" supplierTheta.
     *
     * @param id the id of the supplierThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierTheta : {}", id);
        supplierThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

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
import xyz.jhmapstruct.repository.SupplierGammaRepository;
import xyz.jhmapstruct.service.SupplierGammaQueryService;
import xyz.jhmapstruct.service.SupplierGammaService;
import xyz.jhmapstruct.service.criteria.SupplierGammaCriteria;
import xyz.jhmapstruct.service.dto.SupplierGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierGamma}.
 */
@RestController
@RequestMapping("/api/supplier-gammas")
public class SupplierGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierGammaResource.class);

    private static final String ENTITY_NAME = "supplierGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierGammaService supplierGammaService;

    private final SupplierGammaRepository supplierGammaRepository;

    private final SupplierGammaQueryService supplierGammaQueryService;

    public SupplierGammaResource(
        SupplierGammaService supplierGammaService,
        SupplierGammaRepository supplierGammaRepository,
        SupplierGammaQueryService supplierGammaQueryService
    ) {
        this.supplierGammaService = supplierGammaService;
        this.supplierGammaRepository = supplierGammaRepository;
        this.supplierGammaQueryService = supplierGammaQueryService;
    }

    /**
     * {@code POST  /supplier-gammas} : Create a new supplierGamma.
     *
     * @param supplierGammaDTO the supplierGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierGammaDTO, or with status {@code 400 (Bad Request)} if the supplierGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierGammaDTO> createSupplierGamma(@Valid @RequestBody SupplierGammaDTO supplierGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierGamma : {}", supplierGammaDTO);
        if (supplierGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierGammaDTO = supplierGammaService.save(supplierGammaDTO);
        return ResponseEntity.created(new URI("/api/supplier-gammas/" + supplierGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierGammaDTO.getId().toString()))
            .body(supplierGammaDTO);
    }

    /**
     * {@code PUT  /supplier-gammas/:id} : Updates an existing supplierGamma.
     *
     * @param id the id of the supplierGammaDTO to save.
     * @param supplierGammaDTO the supplierGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierGammaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierGammaDTO> updateSupplierGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierGammaDTO supplierGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierGamma : {}, {}", id, supplierGammaDTO);
        if (supplierGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierGammaDTO = supplierGammaService.update(supplierGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierGammaDTO.getId().toString()))
            .body(supplierGammaDTO);
    }

    /**
     * {@code PATCH  /supplier-gammas/:id} : Partial updates given fields of an existing supplierGamma, field will ignore if it is null
     *
     * @param id the id of the supplierGammaDTO to save.
     * @param supplierGammaDTO the supplierGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierGammaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierGammaDTO> partialUpdateSupplierGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierGammaDTO supplierGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierGamma partially : {}, {}", id, supplierGammaDTO);
        if (supplierGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierGammaDTO> result = supplierGammaService.partialUpdate(supplierGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-gammas} : get all the supplierGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierGammaDTO>> getAllSupplierGammas(
        SupplierGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierGammas by criteria: {}", criteria);

        Page<SupplierGammaDTO> page = supplierGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-gammas/count} : count all the supplierGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierGammas(SupplierGammaCriteria criteria) {
        LOG.debug("REST request to count SupplierGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-gammas/:id} : get the "id" supplierGamma.
     *
     * @param id the id of the supplierGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierGammaDTO> getSupplierGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierGamma : {}", id);
        Optional<SupplierGammaDTO> supplierGammaDTO = supplierGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierGammaDTO);
    }

    /**
     * {@code DELETE  /supplier-gammas/:id} : delete the "id" supplierGamma.
     *
     * @param id the id of the supplierGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierGamma : {}", id);
        supplierGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

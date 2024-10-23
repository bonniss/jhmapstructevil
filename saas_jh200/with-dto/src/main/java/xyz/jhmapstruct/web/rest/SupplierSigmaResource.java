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
import xyz.jhmapstruct.repository.SupplierSigmaRepository;
import xyz.jhmapstruct.service.SupplierSigmaQueryService;
import xyz.jhmapstruct.service.SupplierSigmaService;
import xyz.jhmapstruct.service.criteria.SupplierSigmaCriteria;
import xyz.jhmapstruct.service.dto.SupplierSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierSigma}.
 */
@RestController
@RequestMapping("/api/supplier-sigmas")
public class SupplierSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierSigmaResource.class);

    private static final String ENTITY_NAME = "supplierSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierSigmaService supplierSigmaService;

    private final SupplierSigmaRepository supplierSigmaRepository;

    private final SupplierSigmaQueryService supplierSigmaQueryService;

    public SupplierSigmaResource(
        SupplierSigmaService supplierSigmaService,
        SupplierSigmaRepository supplierSigmaRepository,
        SupplierSigmaQueryService supplierSigmaQueryService
    ) {
        this.supplierSigmaService = supplierSigmaService;
        this.supplierSigmaRepository = supplierSigmaRepository;
        this.supplierSigmaQueryService = supplierSigmaQueryService;
    }

    /**
     * {@code POST  /supplier-sigmas} : Create a new supplierSigma.
     *
     * @param supplierSigmaDTO the supplierSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierSigmaDTO, or with status {@code 400 (Bad Request)} if the supplierSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierSigmaDTO> createSupplierSigma(@Valid @RequestBody SupplierSigmaDTO supplierSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierSigma : {}", supplierSigmaDTO);
        if (supplierSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierSigmaDTO = supplierSigmaService.save(supplierSigmaDTO);
        return ResponseEntity.created(new URI("/api/supplier-sigmas/" + supplierSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierSigmaDTO.getId().toString()))
            .body(supplierSigmaDTO);
    }

    /**
     * {@code PUT  /supplier-sigmas/:id} : Updates an existing supplierSigma.
     *
     * @param id the id of the supplierSigmaDTO to save.
     * @param supplierSigmaDTO the supplierSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierSigmaDTO> updateSupplierSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierSigmaDTO supplierSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierSigma : {}, {}", id, supplierSigmaDTO);
        if (supplierSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierSigmaDTO = supplierSigmaService.update(supplierSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierSigmaDTO.getId().toString()))
            .body(supplierSigmaDTO);
    }

    /**
     * {@code PATCH  /supplier-sigmas/:id} : Partial updates given fields of an existing supplierSigma, field will ignore if it is null
     *
     * @param id the id of the supplierSigmaDTO to save.
     * @param supplierSigmaDTO the supplierSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierSigmaDTO> partialUpdateSupplierSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierSigmaDTO supplierSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierSigma partially : {}, {}", id, supplierSigmaDTO);
        if (supplierSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierSigmaDTO> result = supplierSigmaService.partialUpdate(supplierSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-sigmas} : get all the supplierSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierSigmaDTO>> getAllSupplierSigmas(
        SupplierSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierSigmas by criteria: {}", criteria);

        Page<SupplierSigmaDTO> page = supplierSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-sigmas/count} : count all the supplierSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierSigmas(SupplierSigmaCriteria criteria) {
        LOG.debug("REST request to count SupplierSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-sigmas/:id} : get the "id" supplierSigma.
     *
     * @param id the id of the supplierSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierSigmaDTO> getSupplierSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierSigma : {}", id);
        Optional<SupplierSigmaDTO> supplierSigmaDTO = supplierSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierSigmaDTO);
    }

    /**
     * {@code DELETE  /supplier-sigmas/:id} : delete the "id" supplierSigma.
     *
     * @param id the id of the supplierSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierSigma : {}", id);
        supplierSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
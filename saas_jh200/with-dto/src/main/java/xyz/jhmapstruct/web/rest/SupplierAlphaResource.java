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
import xyz.jhmapstruct.repository.SupplierAlphaRepository;
import xyz.jhmapstruct.service.SupplierAlphaQueryService;
import xyz.jhmapstruct.service.SupplierAlphaService;
import xyz.jhmapstruct.service.criteria.SupplierAlphaCriteria;
import xyz.jhmapstruct.service.dto.SupplierAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierAlpha}.
 */
@RestController
@RequestMapping("/api/supplier-alphas")
public class SupplierAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierAlphaResource.class);

    private static final String ENTITY_NAME = "supplierAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierAlphaService supplierAlphaService;

    private final SupplierAlphaRepository supplierAlphaRepository;

    private final SupplierAlphaQueryService supplierAlphaQueryService;

    public SupplierAlphaResource(
        SupplierAlphaService supplierAlphaService,
        SupplierAlphaRepository supplierAlphaRepository,
        SupplierAlphaQueryService supplierAlphaQueryService
    ) {
        this.supplierAlphaService = supplierAlphaService;
        this.supplierAlphaRepository = supplierAlphaRepository;
        this.supplierAlphaQueryService = supplierAlphaQueryService;
    }

    /**
     * {@code POST  /supplier-alphas} : Create a new supplierAlpha.
     *
     * @param supplierAlphaDTO the supplierAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAlphaDTO, or with status {@code 400 (Bad Request)} if the supplierAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierAlphaDTO> createSupplierAlpha(@Valid @RequestBody SupplierAlphaDTO supplierAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierAlpha : {}", supplierAlphaDTO);
        if (supplierAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierAlphaDTO = supplierAlphaService.save(supplierAlphaDTO);
        return ResponseEntity.created(new URI("/api/supplier-alphas/" + supplierAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierAlphaDTO.getId().toString()))
            .body(supplierAlphaDTO);
    }

    /**
     * {@code PUT  /supplier-alphas/:id} : Updates an existing supplierAlpha.
     *
     * @param id the id of the supplierAlphaDTO to save.
     * @param supplierAlphaDTO the supplierAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierAlphaDTO> updateSupplierAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierAlphaDTO supplierAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierAlpha : {}, {}", id, supplierAlphaDTO);
        if (supplierAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierAlphaDTO = supplierAlphaService.update(supplierAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAlphaDTO.getId().toString()))
            .body(supplierAlphaDTO);
    }

    /**
     * {@code PATCH  /supplier-alphas/:id} : Partial updates given fields of an existing supplierAlpha, field will ignore if it is null
     *
     * @param id the id of the supplierAlphaDTO to save.
     * @param supplierAlphaDTO the supplierAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the supplierAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierAlphaDTO> partialUpdateSupplierAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierAlphaDTO supplierAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierAlpha partially : {}, {}", id, supplierAlphaDTO);
        if (supplierAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierAlphaDTO> result = supplierAlphaService.partialUpdate(supplierAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-alphas} : get all the supplierAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierAlphaDTO>> getAllSupplierAlphas(
        SupplierAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierAlphas by criteria: {}", criteria);

        Page<SupplierAlphaDTO> page = supplierAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-alphas/count} : count all the supplierAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierAlphas(SupplierAlphaCriteria criteria) {
        LOG.debug("REST request to count SupplierAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-alphas/:id} : get the "id" supplierAlpha.
     *
     * @param id the id of the supplierAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierAlphaDTO> getSupplierAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierAlpha : {}", id);
        Optional<SupplierAlphaDTO> supplierAlphaDTO = supplierAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierAlphaDTO);
    }

    /**
     * {@code DELETE  /supplier-alphas/:id} : delete the "id" supplierAlpha.
     *
     * @param id the id of the supplierAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierAlpha : {}", id);
        supplierAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

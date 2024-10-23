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
import xyz.jhmapstruct.domain.SupplierAlpha;
import xyz.jhmapstruct.repository.SupplierAlphaRepository;
import xyz.jhmapstruct.service.SupplierAlphaQueryService;
import xyz.jhmapstruct.service.SupplierAlphaService;
import xyz.jhmapstruct.service.criteria.SupplierAlphaCriteria;
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
     * @param supplierAlpha the supplierAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierAlpha, or with status {@code 400 (Bad Request)} if the supplierAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierAlpha> createSupplierAlpha(@Valid @RequestBody SupplierAlpha supplierAlpha) throws URISyntaxException {
        LOG.debug("REST request to save SupplierAlpha : {}", supplierAlpha);
        if (supplierAlpha.getId() != null) {
            throw new BadRequestAlertException("A new supplierAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierAlpha = supplierAlphaService.save(supplierAlpha);
        return ResponseEntity.created(new URI("/api/supplier-alphas/" + supplierAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierAlpha.getId().toString()))
            .body(supplierAlpha);
    }

    /**
     * {@code PUT  /supplier-alphas/:id} : Updates an existing supplierAlpha.
     *
     * @param id the id of the supplierAlpha to save.
     * @param supplierAlpha the supplierAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAlpha,
     * or with status {@code 400 (Bad Request)} if the supplierAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierAlpha> updateSupplierAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierAlpha supplierAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierAlpha : {}, {}", id, supplierAlpha);
        if (supplierAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierAlpha = supplierAlphaService.update(supplierAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAlpha.getId().toString()))
            .body(supplierAlpha);
    }

    /**
     * {@code PATCH  /supplier-alphas/:id} : Partial updates given fields of an existing supplierAlpha, field will ignore if it is null
     *
     * @param id the id of the supplierAlpha to save.
     * @param supplierAlpha the supplierAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierAlpha,
     * or with status {@code 400 (Bad Request)} if the supplierAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the supplierAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierAlpha> partialUpdateSupplierAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierAlpha supplierAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierAlpha partially : {}, {}", id, supplierAlpha);
        if (supplierAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierAlpha> result = supplierAlphaService.partialUpdate(supplierAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierAlpha.getId().toString())
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
    public ResponseEntity<List<SupplierAlpha>> getAllSupplierAlphas(
        SupplierAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierAlphas by criteria: {}", criteria);

        Page<SupplierAlpha> page = supplierAlphaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the supplierAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierAlpha> getSupplierAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierAlpha : {}", id);
        Optional<SupplierAlpha> supplierAlpha = supplierAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierAlpha);
    }

    /**
     * {@code DELETE  /supplier-alphas/:id} : delete the "id" supplierAlpha.
     *
     * @param id the id of the supplierAlpha to delete.
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

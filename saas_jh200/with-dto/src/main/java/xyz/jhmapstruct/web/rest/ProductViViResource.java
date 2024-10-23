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
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.ProductViViQueryService;
import xyz.jhmapstruct.service.ProductViViService;
import xyz.jhmapstruct.service.criteria.ProductViViCriteria;
import xyz.jhmapstruct.service.dto.ProductViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
@RestController
@RequestMapping("/api/product-vi-vis")
public class ProductViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViResource.class);

    private static final String ENTITY_NAME = "productViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductViViService productViViService;

    private final ProductViViRepository productViViRepository;

    private final ProductViViQueryService productViViQueryService;

    public ProductViViResource(
        ProductViViService productViViService,
        ProductViViRepository productViViRepository,
        ProductViViQueryService productViViQueryService
    ) {
        this.productViViService = productViViService;
        this.productViViRepository = productViViRepository;
        this.productViViQueryService = productViViQueryService;
    }

    /**
     * {@code POST  /product-vi-vis} : Create a new productViVi.
     *
     * @param productViViDTO the productViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productViViDTO, or with status {@code 400 (Bad Request)} if the productViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductViViDTO> createProductViVi(@Valid @RequestBody ProductViViDTO productViViDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProductViVi : {}", productViViDTO);
        if (productViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new productViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productViViDTO = productViViService.save(productViViDTO);
        return ResponseEntity.created(new URI("/api/product-vi-vis/" + productViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productViViDTO.getId().toString()))
            .body(productViViDTO);
    }

    /**
     * {@code PUT  /product-vi-vis/:id} : Updates an existing productViVi.
     *
     * @param id the id of the productViViDTO to save.
     * @param productViViDTO the productViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productViViDTO,
     * or with status {@code 400 (Bad Request)} if the productViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductViViDTO> updateProductViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductViViDTO productViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductViVi : {}, {}", id, productViViDTO);
        if (productViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productViViDTO = productViViService.update(productViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productViViDTO.getId().toString()))
            .body(productViViDTO);
    }

    /**
     * {@code PATCH  /product-vi-vis/:id} : Partial updates given fields of an existing productViVi, field will ignore if it is null
     *
     * @param id the id of the productViViDTO to save.
     * @param productViViDTO the productViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productViViDTO,
     * or with status {@code 400 (Bad Request)} if the productViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductViViDTO> partialUpdateProductViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductViViDTO productViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductViVi partially : {}, {}", id, productViViDTO);
        if (productViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductViViDTO> result = productViViService.partialUpdate(productViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-vi-vis} : get all the productViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductViViDTO>> getAllProductViVis(
        ProductViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductViVis by criteria: {}", criteria);

        Page<ProductViViDTO> page = productViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-vi-vis/count} : count all the productViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductViVis(ProductViViCriteria criteria) {
        LOG.debug("REST request to count ProductViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(productViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-vi-vis/:id} : get the "id" productViVi.
     *
     * @param id the id of the productViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductViViDTO> getProductViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductViVi : {}", id);
        Optional<ProductViViDTO> productViViDTO = productViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productViViDTO);
    }

    /**
     * {@code DELETE  /product-vi-vis/:id} : delete the "id" productViVi.
     *
     * @param id the id of the productViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductViVi : {}", id);
        productViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

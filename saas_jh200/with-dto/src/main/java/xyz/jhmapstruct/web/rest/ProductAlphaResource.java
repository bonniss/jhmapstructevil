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
import xyz.jhmapstruct.repository.ProductAlphaRepository;
import xyz.jhmapstruct.service.ProductAlphaQueryService;
import xyz.jhmapstruct.service.ProductAlphaService;
import xyz.jhmapstruct.service.criteria.ProductAlphaCriteria;
import xyz.jhmapstruct.service.dto.ProductAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductAlpha}.
 */
@RestController
@RequestMapping("/api/product-alphas")
public class ProductAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAlphaResource.class);

    private static final String ENTITY_NAME = "productAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductAlphaService productAlphaService;

    private final ProductAlphaRepository productAlphaRepository;

    private final ProductAlphaQueryService productAlphaQueryService;

    public ProductAlphaResource(
        ProductAlphaService productAlphaService,
        ProductAlphaRepository productAlphaRepository,
        ProductAlphaQueryService productAlphaQueryService
    ) {
        this.productAlphaService = productAlphaService;
        this.productAlphaRepository = productAlphaRepository;
        this.productAlphaQueryService = productAlphaQueryService;
    }

    /**
     * {@code POST  /product-alphas} : Create a new productAlpha.
     *
     * @param productAlphaDTO the productAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productAlphaDTO, or with status {@code 400 (Bad Request)} if the productAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductAlphaDTO> createProductAlpha(@Valid @RequestBody ProductAlphaDTO productAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductAlpha : {}", productAlphaDTO);
        if (productAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new productAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productAlphaDTO = productAlphaService.save(productAlphaDTO);
        return ResponseEntity.created(new URI("/api/product-alphas/" + productAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productAlphaDTO.getId().toString()))
            .body(productAlphaDTO);
    }

    /**
     * {@code PUT  /product-alphas/:id} : Updates an existing productAlpha.
     *
     * @param id the id of the productAlphaDTO to save.
     * @param productAlphaDTO the productAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the productAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductAlphaDTO> updateProductAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductAlphaDTO productAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductAlpha : {}, {}", id, productAlphaDTO);
        if (productAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productAlphaDTO = productAlphaService.update(productAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAlphaDTO.getId().toString()))
            .body(productAlphaDTO);
    }

    /**
     * {@code PATCH  /product-alphas/:id} : Partial updates given fields of an existing productAlpha, field will ignore if it is null
     *
     * @param id the id of the productAlphaDTO to save.
     * @param productAlphaDTO the productAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the productAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductAlphaDTO> partialUpdateProductAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductAlphaDTO productAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductAlpha partially : {}, {}", id, productAlphaDTO);
        if (productAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductAlphaDTO> result = productAlphaService.partialUpdate(productAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-alphas} : get all the productAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductAlphaDTO>> getAllProductAlphas(
        ProductAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductAlphas by criteria: {}", criteria);

        Page<ProductAlphaDTO> page = productAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-alphas/count} : count all the productAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductAlphas(ProductAlphaCriteria criteria) {
        LOG.debug("REST request to count ProductAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(productAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-alphas/:id} : get the "id" productAlpha.
     *
     * @param id the id of the productAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductAlphaDTO> getProductAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductAlpha : {}", id);
        Optional<ProductAlphaDTO> productAlphaDTO = productAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productAlphaDTO);
    }

    /**
     * {@code DELETE  /product-alphas/:id} : delete the "id" productAlpha.
     *
     * @param id the id of the productAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductAlpha : {}", id);
        productAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

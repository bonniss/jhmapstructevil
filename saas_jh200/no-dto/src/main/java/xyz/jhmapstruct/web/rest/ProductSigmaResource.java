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
import xyz.jhmapstruct.domain.ProductSigma;
import xyz.jhmapstruct.repository.ProductSigmaRepository;
import xyz.jhmapstruct.service.ProductSigmaQueryService;
import xyz.jhmapstruct.service.ProductSigmaService;
import xyz.jhmapstruct.service.criteria.ProductSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductSigma}.
 */
@RestController
@RequestMapping("/api/product-sigmas")
public class ProductSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductSigmaResource.class);

    private static final String ENTITY_NAME = "productSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductSigmaService productSigmaService;

    private final ProductSigmaRepository productSigmaRepository;

    private final ProductSigmaQueryService productSigmaQueryService;

    public ProductSigmaResource(
        ProductSigmaService productSigmaService,
        ProductSigmaRepository productSigmaRepository,
        ProductSigmaQueryService productSigmaQueryService
    ) {
        this.productSigmaService = productSigmaService;
        this.productSigmaRepository = productSigmaRepository;
        this.productSigmaQueryService = productSigmaQueryService;
    }

    /**
     * {@code POST  /product-sigmas} : Create a new productSigma.
     *
     * @param productSigma the productSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productSigma, or with status {@code 400 (Bad Request)} if the productSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductSigma> createProductSigma(@Valid @RequestBody ProductSigma productSigma) throws URISyntaxException {
        LOG.debug("REST request to save ProductSigma : {}", productSigma);
        if (productSigma.getId() != null) {
            throw new BadRequestAlertException("A new productSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productSigma = productSigmaService.save(productSigma);
        return ResponseEntity.created(new URI("/api/product-sigmas/" + productSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productSigma.getId().toString()))
            .body(productSigma);
    }

    /**
     * {@code PUT  /product-sigmas/:id} : Updates an existing productSigma.
     *
     * @param id the id of the productSigma to save.
     * @param productSigma the productSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productSigma,
     * or with status {@code 400 (Bad Request)} if the productSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductSigma> updateProductSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductSigma productSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductSigma : {}, {}", id, productSigma);
        if (productSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productSigma = productSigmaService.update(productSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productSigma.getId().toString()))
            .body(productSigma);
    }

    /**
     * {@code PATCH  /product-sigmas/:id} : Partial updates given fields of an existing productSigma, field will ignore if it is null
     *
     * @param id the id of the productSigma to save.
     * @param productSigma the productSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productSigma,
     * or with status {@code 400 (Bad Request)} if the productSigma is not valid,
     * or with status {@code 404 (Not Found)} if the productSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the productSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductSigma> partialUpdateProductSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductSigma productSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductSigma partially : {}, {}", id, productSigma);
        if (productSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductSigma> result = productSigmaService.partialUpdate(productSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /product-sigmas} : get all the productSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductSigma>> getAllProductSigmas(
        ProductSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductSigmas by criteria: {}", criteria);

        Page<ProductSigma> page = productSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-sigmas/count} : count all the productSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductSigmas(ProductSigmaCriteria criteria) {
        LOG.debug("REST request to count ProductSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(productSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-sigmas/:id} : get the "id" productSigma.
     *
     * @param id the id of the productSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductSigma> getProductSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductSigma : {}", id);
        Optional<ProductSigma> productSigma = productSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productSigma);
    }

    /**
     * {@code DELETE  /product-sigmas/:id} : delete the "id" productSigma.
     *
     * @param id the id of the productSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductSigma : {}", id);
        productSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

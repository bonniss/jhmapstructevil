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
import xyz.jhmapstruct.domain.ProductGamma;
import xyz.jhmapstruct.repository.ProductGammaRepository;
import xyz.jhmapstruct.service.ProductGammaQueryService;
import xyz.jhmapstruct.service.ProductGammaService;
import xyz.jhmapstruct.service.criteria.ProductGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductGamma}.
 */
@RestController
@RequestMapping("/api/product-gammas")
public class ProductGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductGammaResource.class);

    private static final String ENTITY_NAME = "productGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductGammaService productGammaService;

    private final ProductGammaRepository productGammaRepository;

    private final ProductGammaQueryService productGammaQueryService;

    public ProductGammaResource(
        ProductGammaService productGammaService,
        ProductGammaRepository productGammaRepository,
        ProductGammaQueryService productGammaQueryService
    ) {
        this.productGammaService = productGammaService;
        this.productGammaRepository = productGammaRepository;
        this.productGammaQueryService = productGammaQueryService;
    }

    /**
     * {@code POST  /product-gammas} : Create a new productGamma.
     *
     * @param productGamma the productGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productGamma, or with status {@code 400 (Bad Request)} if the productGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductGamma> createProductGamma(@Valid @RequestBody ProductGamma productGamma) throws URISyntaxException {
        LOG.debug("REST request to save ProductGamma : {}", productGamma);
        if (productGamma.getId() != null) {
            throw new BadRequestAlertException("A new productGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productGamma = productGammaService.save(productGamma);
        return ResponseEntity.created(new URI("/api/product-gammas/" + productGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productGamma.getId().toString()))
            .body(productGamma);
    }

    /**
     * {@code PUT  /product-gammas/:id} : Updates an existing productGamma.
     *
     * @param id the id of the productGamma to save.
     * @param productGamma the productGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productGamma,
     * or with status {@code 400 (Bad Request)} if the productGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductGamma> updateProductGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductGamma productGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductGamma : {}, {}", id, productGamma);
        if (productGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productGamma = productGammaService.update(productGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productGamma.getId().toString()))
            .body(productGamma);
    }

    /**
     * {@code PATCH  /product-gammas/:id} : Partial updates given fields of an existing productGamma, field will ignore if it is null
     *
     * @param id the id of the productGamma to save.
     * @param productGamma the productGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productGamma,
     * or with status {@code 400 (Bad Request)} if the productGamma is not valid,
     * or with status {@code 404 (Not Found)} if the productGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the productGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductGamma> partialUpdateProductGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductGamma productGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductGamma partially : {}, {}", id, productGamma);
        if (productGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductGamma> result = productGammaService.partialUpdate(productGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /product-gammas} : get all the productGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductGamma>> getAllProductGammas(
        ProductGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductGammas by criteria: {}", criteria);

        Page<ProductGamma> page = productGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-gammas/count} : count all the productGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductGammas(ProductGammaCriteria criteria) {
        LOG.debug("REST request to count ProductGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(productGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-gammas/:id} : get the "id" productGamma.
     *
     * @param id the id of the productGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductGamma> getProductGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductGamma : {}", id);
        Optional<ProductGamma> productGamma = productGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productGamma);
    }

    /**
     * {@code DELETE  /product-gammas/:id} : delete the "id" productGamma.
     *
     * @param id the id of the productGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductGamma : {}", id);
        productGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

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
import xyz.jhmapstruct.repository.ProductSigmaRepository;
import xyz.jhmapstruct.service.ProductSigmaQueryService;
import xyz.jhmapstruct.service.ProductSigmaService;
import xyz.jhmapstruct.service.criteria.ProductSigmaCriteria;
import xyz.jhmapstruct.service.dto.ProductSigmaDTO;
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
     * @param productSigmaDTO the productSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productSigmaDTO, or with status {@code 400 (Bad Request)} if the productSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductSigmaDTO> createProductSigma(@Valid @RequestBody ProductSigmaDTO productSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProductSigma : {}", productSigmaDTO);
        if (productSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new productSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productSigmaDTO = productSigmaService.save(productSigmaDTO);
        return ResponseEntity.created(new URI("/api/product-sigmas/" + productSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productSigmaDTO.getId().toString()))
            .body(productSigmaDTO);
    }

    /**
     * {@code PUT  /product-sigmas/:id} : Updates an existing productSigma.
     *
     * @param id the id of the productSigmaDTO to save.
     * @param productSigmaDTO the productSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the productSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductSigmaDTO> updateProductSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductSigmaDTO productSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductSigma : {}, {}", id, productSigmaDTO);
        if (productSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productSigmaDTO = productSigmaService.update(productSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productSigmaDTO.getId().toString()))
            .body(productSigmaDTO);
    }

    /**
     * {@code PATCH  /product-sigmas/:id} : Partial updates given fields of an existing productSigma, field will ignore if it is null
     *
     * @param id the id of the productSigmaDTO to save.
     * @param productSigmaDTO the productSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the productSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductSigmaDTO> partialUpdateProductSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductSigmaDTO productSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductSigma partially : {}, {}", id, productSigmaDTO);
        if (productSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductSigmaDTO> result = productSigmaService.partialUpdate(productSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productSigmaDTO.getId().toString())
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
    public ResponseEntity<List<ProductSigmaDTO>> getAllProductSigmas(
        ProductSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductSigmas by criteria: {}", criteria);

        Page<ProductSigmaDTO> page = productSigmaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the productSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductSigmaDTO> getProductSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductSigma : {}", id);
        Optional<ProductSigmaDTO> productSigmaDTO = productSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productSigmaDTO);
    }

    /**
     * {@code DELETE  /product-sigmas/:id} : delete the "id" productSigma.
     *
     * @param id the id of the productSigmaDTO to delete.
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

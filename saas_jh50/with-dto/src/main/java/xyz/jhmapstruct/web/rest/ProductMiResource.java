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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.ProductMiService;
import xyz.jhmapstruct.service.dto.ProductMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
@RestController
@RequestMapping("/api/product-mis")
public class ProductMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiResource.class);

    private static final String ENTITY_NAME = "productMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductMiService productMiService;

    private final ProductMiRepository productMiRepository;

    public ProductMiResource(ProductMiService productMiService, ProductMiRepository productMiRepository) {
        this.productMiService = productMiService;
        this.productMiRepository = productMiRepository;
    }

    /**
     * {@code POST  /product-mis} : Create a new productMi.
     *
     * @param productMiDTO the productMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productMiDTO, or with status {@code 400 (Bad Request)} if the productMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductMiDTO> createProductMi(@Valid @RequestBody ProductMiDTO productMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProductMi : {}", productMiDTO);
        if (productMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new productMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productMiDTO = productMiService.save(productMiDTO);
        return ResponseEntity.created(new URI("/api/product-mis/" + productMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productMiDTO.getId().toString()))
            .body(productMiDTO);
    }

    /**
     * {@code PUT  /product-mis/:id} : Updates an existing productMi.
     *
     * @param id the id of the productMiDTO to save.
     * @param productMiDTO the productMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMiDTO,
     * or with status {@code 400 (Bad Request)} if the productMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductMiDTO> updateProductMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductMiDTO productMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductMi : {}, {}", id, productMiDTO);
        if (productMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productMiDTO = productMiService.update(productMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMiDTO.getId().toString()))
            .body(productMiDTO);
    }

    /**
     * {@code PATCH  /product-mis/:id} : Partial updates given fields of an existing productMi, field will ignore if it is null
     *
     * @param id the id of the productMiDTO to save.
     * @param productMiDTO the productMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMiDTO,
     * or with status {@code 400 (Bad Request)} if the productMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductMiDTO> partialUpdateProductMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductMiDTO productMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductMi partially : {}, {}", id, productMiDTO);
        if (productMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductMiDTO> result = productMiService.partialUpdate(productMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-mis} : get all the productMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productMis in body.
     */
    @GetMapping("")
    public List<ProductMiDTO> getAllProductMis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ProductMis");
        return productMiService.findAll();
    }

    /**
     * {@code GET  /product-mis/:id} : get the "id" productMi.
     *
     * @param id the id of the productMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductMiDTO> getProductMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductMi : {}", id);
        Optional<ProductMiDTO> productMiDTO = productMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productMiDTO);
    }

    /**
     * {@code DELETE  /product-mis/:id} : delete the "id" productMi.
     *
     * @param id the id of the productMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductMi : {}", id);
        productMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

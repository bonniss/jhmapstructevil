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
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.ProductMiMiService;
import xyz.jhmapstruct.service.dto.ProductMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
@RestController
@RequestMapping("/api/product-mi-mis")
public class ProductMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiResource.class);

    private static final String ENTITY_NAME = "productMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductMiMiService productMiMiService;

    private final ProductMiMiRepository productMiMiRepository;

    public ProductMiMiResource(ProductMiMiService productMiMiService, ProductMiMiRepository productMiMiRepository) {
        this.productMiMiService = productMiMiService;
        this.productMiMiRepository = productMiMiRepository;
    }

    /**
     * {@code POST  /product-mi-mis} : Create a new productMiMi.
     *
     * @param productMiMiDTO the productMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productMiMiDTO, or with status {@code 400 (Bad Request)} if the productMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductMiMiDTO> createProductMiMi(@Valid @RequestBody ProductMiMiDTO productMiMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProductMiMi : {}", productMiMiDTO);
        if (productMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new productMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productMiMiDTO = productMiMiService.save(productMiMiDTO);
        return ResponseEntity.created(new URI("/api/product-mi-mis/" + productMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productMiMiDTO.getId().toString()))
            .body(productMiMiDTO);
    }

    /**
     * {@code PUT  /product-mi-mis/:id} : Updates an existing productMiMi.
     *
     * @param id the id of the productMiMiDTO to save.
     * @param productMiMiDTO the productMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the productMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductMiMiDTO> updateProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductMiMiDTO productMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductMiMi : {}, {}", id, productMiMiDTO);
        if (productMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productMiMiDTO = productMiMiService.update(productMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMiMiDTO.getId().toString()))
            .body(productMiMiDTO);
    }

    /**
     * {@code PATCH  /product-mi-mis/:id} : Partial updates given fields of an existing productMiMi, field will ignore if it is null
     *
     * @param id the id of the productMiMiDTO to save.
     * @param productMiMiDTO the productMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the productMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductMiMiDTO> partialUpdateProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductMiMiDTO productMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductMiMi partially : {}, {}", id, productMiMiDTO);
        if (productMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductMiMiDTO> result = productMiMiService.partialUpdate(productMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-mi-mis} : get all the productMiMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productMiMis in body.
     */
    @GetMapping("")
    public List<ProductMiMiDTO> getAllProductMiMis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ProductMiMis");
        return productMiMiService.findAll();
    }

    /**
     * {@code GET  /product-mi-mis/:id} : get the "id" productMiMi.
     *
     * @param id the id of the productMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductMiMiDTO> getProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductMiMi : {}", id);
        Optional<ProductMiMiDTO> productMiMiDTO = productMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productMiMiDTO);
    }

    /**
     * {@code DELETE  /product-mi-mis/:id} : delete the "id" productMiMi.
     *
     * @param id the id of the productMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductMiMi : {}", id);
        productMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

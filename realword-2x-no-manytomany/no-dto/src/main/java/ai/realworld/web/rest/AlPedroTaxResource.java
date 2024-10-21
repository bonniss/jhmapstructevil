package ai.realworld.web.rest;

import ai.realworld.domain.AlPedroTax;
import ai.realworld.repository.AlPedroTaxRepository;
import ai.realworld.service.AlPedroTaxQueryService;
import ai.realworld.service.AlPedroTaxService;
import ai.realworld.service.criteria.AlPedroTaxCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AlPedroTax}.
 */
@RestController
@RequestMapping("/api/al-pedro-taxes")
public class AlPedroTaxResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxResource.class);

    private static final String ENTITY_NAME = "alPedroTax";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPedroTaxService alPedroTaxService;

    private final AlPedroTaxRepository alPedroTaxRepository;

    private final AlPedroTaxQueryService alPedroTaxQueryService;

    public AlPedroTaxResource(
        AlPedroTaxService alPedroTaxService,
        AlPedroTaxRepository alPedroTaxRepository,
        AlPedroTaxQueryService alPedroTaxQueryService
    ) {
        this.alPedroTaxService = alPedroTaxService;
        this.alPedroTaxRepository = alPedroTaxRepository;
        this.alPedroTaxQueryService = alPedroTaxQueryService;
    }

    /**
     * {@code POST  /al-pedro-taxes} : Create a new alPedroTax.
     *
     * @param alPedroTax the alPedroTax to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPedroTax, or with status {@code 400 (Bad Request)} if the alPedroTax has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPedroTax> createAlPedroTax(@Valid @RequestBody AlPedroTax alPedroTax) throws URISyntaxException {
        LOG.debug("REST request to save AlPedroTax : {}", alPedroTax);
        if (alPedroTax.getId() != null) {
            throw new BadRequestAlertException("A new alPedroTax cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPedroTax = alPedroTaxService.save(alPedroTax);
        return ResponseEntity.created(new URI("/api/al-pedro-taxes/" + alPedroTax.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPedroTax.getId().toString()))
            .body(alPedroTax);
    }

    /**
     * {@code PUT  /al-pedro-taxes/:id} : Updates an existing alPedroTax.
     *
     * @param id the id of the alPedroTax to save.
     * @param alPedroTax the alPedroTax to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPedroTax,
     * or with status {@code 400 (Bad Request)} if the alPedroTax is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPedroTax couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPedroTax> updateAlPedroTax(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPedroTax alPedroTax
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPedroTax : {}, {}", id, alPedroTax);
        if (alPedroTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPedroTax.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPedroTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPedroTax = alPedroTaxService.update(alPedroTax);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPedroTax.getId().toString()))
            .body(alPedroTax);
    }

    /**
     * {@code PATCH  /al-pedro-taxes/:id} : Partial updates given fields of an existing alPedroTax, field will ignore if it is null
     *
     * @param id the id of the alPedroTax to save.
     * @param alPedroTax the alPedroTax to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPedroTax,
     * or with status {@code 400 (Bad Request)} if the alPedroTax is not valid,
     * or with status {@code 404 (Not Found)} if the alPedroTax is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPedroTax couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPedroTax> partialUpdateAlPedroTax(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPedroTax alPedroTax
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPedroTax partially : {}, {}", id, alPedroTax);
        if (alPedroTax.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPedroTax.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPedroTaxRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPedroTax> result = alPedroTaxService.partialUpdate(alPedroTax);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPedroTax.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pedro-taxes} : get all the alPedroTaxes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPedroTaxes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPedroTax>> getAllAlPedroTaxes(
        AlPedroTaxCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPedroTaxes by criteria: {}", criteria);

        Page<AlPedroTax> page = alPedroTaxQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pedro-taxes/count} : count all the alPedroTaxes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPedroTaxes(AlPedroTaxCriteria criteria) {
        LOG.debug("REST request to count AlPedroTaxes by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPedroTaxQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pedro-taxes/:id} : get the "id" alPedroTax.
     *
     * @param id the id of the alPedroTax to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPedroTax, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPedroTax> getAlPedroTax(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPedroTax : {}", id);
        Optional<AlPedroTax> alPedroTax = alPedroTaxService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPedroTax);
    }

    /**
     * {@code DELETE  /al-pedro-taxes/:id} : delete the "id" alPedroTax.
     *
     * @param id the id of the alPedroTax to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPedroTax(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPedroTax : {}", id);
        alPedroTaxService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

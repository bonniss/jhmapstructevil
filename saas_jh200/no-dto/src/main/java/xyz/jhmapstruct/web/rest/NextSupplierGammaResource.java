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
import xyz.jhmapstruct.domain.NextSupplierGamma;
import xyz.jhmapstruct.repository.NextSupplierGammaRepository;
import xyz.jhmapstruct.service.NextSupplierGammaQueryService;
import xyz.jhmapstruct.service.NextSupplierGammaService;
import xyz.jhmapstruct.service.criteria.NextSupplierGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierGamma}.
 */
@RestController
@RequestMapping("/api/next-supplier-gammas")
public class NextSupplierGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierGammaResource.class);

    private static final String ENTITY_NAME = "nextSupplierGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierGammaService nextSupplierGammaService;

    private final NextSupplierGammaRepository nextSupplierGammaRepository;

    private final NextSupplierGammaQueryService nextSupplierGammaQueryService;

    public NextSupplierGammaResource(
        NextSupplierGammaService nextSupplierGammaService,
        NextSupplierGammaRepository nextSupplierGammaRepository,
        NextSupplierGammaQueryService nextSupplierGammaQueryService
    ) {
        this.nextSupplierGammaService = nextSupplierGammaService;
        this.nextSupplierGammaRepository = nextSupplierGammaRepository;
        this.nextSupplierGammaQueryService = nextSupplierGammaQueryService;
    }

    /**
     * {@code POST  /next-supplier-gammas} : Create a new nextSupplierGamma.
     *
     * @param nextSupplierGamma the nextSupplierGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierGamma, or with status {@code 400 (Bad Request)} if the nextSupplierGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierGamma> createNextSupplierGamma(@Valid @RequestBody NextSupplierGamma nextSupplierGamma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierGamma : {}", nextSupplierGamma);
        if (nextSupplierGamma.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierGamma = nextSupplierGammaService.save(nextSupplierGamma);
        return ResponseEntity.created(new URI("/api/next-supplier-gammas/" + nextSupplierGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierGamma.getId().toString()))
            .body(nextSupplierGamma);
    }

    /**
     * {@code PUT  /next-supplier-gammas/:id} : Updates an existing nextSupplierGamma.
     *
     * @param id the id of the nextSupplierGamma to save.
     * @param nextSupplierGamma the nextSupplierGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierGamma,
     * or with status {@code 400 (Bad Request)} if the nextSupplierGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierGamma> updateNextSupplierGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierGamma nextSupplierGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierGamma : {}, {}", id, nextSupplierGamma);
        if (nextSupplierGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierGamma = nextSupplierGammaService.update(nextSupplierGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierGamma.getId().toString()))
            .body(nextSupplierGamma);
    }

    /**
     * {@code PATCH  /next-supplier-gammas/:id} : Partial updates given fields of an existing nextSupplierGamma, field will ignore if it is null
     *
     * @param id the id of the nextSupplierGamma to save.
     * @param nextSupplierGamma the nextSupplierGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierGamma,
     * or with status {@code 400 (Bad Request)} if the nextSupplierGamma is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierGamma> partialUpdateNextSupplierGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierGamma nextSupplierGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierGamma partially : {}, {}", id, nextSupplierGamma);
        if (nextSupplierGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierGamma> result = nextSupplierGammaService.partialUpdate(nextSupplierGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-gammas} : get all the nextSupplierGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierGamma>> getAllNextSupplierGammas(
        NextSupplierGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierGammas by criteria: {}", criteria);

        Page<NextSupplierGamma> page = nextSupplierGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-gammas/count} : count all the nextSupplierGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierGammas(NextSupplierGammaCriteria criteria) {
        LOG.debug("REST request to count NextSupplierGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-gammas/:id} : get the "id" nextSupplierGamma.
     *
     * @param id the id of the nextSupplierGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierGamma> getNextSupplierGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierGamma : {}", id);
        Optional<NextSupplierGamma> nextSupplierGamma = nextSupplierGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierGamma);
    }

    /**
     * {@code DELETE  /next-supplier-gammas/:id} : delete the "id" nextSupplierGamma.
     *
     * @param id the id of the nextSupplierGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierGamma : {}", id);
        nextSupplierGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

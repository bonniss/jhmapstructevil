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
import xyz.jhmapstruct.domain.NextOrderGamma;
import xyz.jhmapstruct.repository.NextOrderGammaRepository;
import xyz.jhmapstruct.service.NextOrderGammaQueryService;
import xyz.jhmapstruct.service.NextOrderGammaService;
import xyz.jhmapstruct.service.criteria.NextOrderGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderGamma}.
 */
@RestController
@RequestMapping("/api/next-order-gammas")
public class NextOrderGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderGammaResource.class);

    private static final String ENTITY_NAME = "nextOrderGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderGammaService nextOrderGammaService;

    private final NextOrderGammaRepository nextOrderGammaRepository;

    private final NextOrderGammaQueryService nextOrderGammaQueryService;

    public NextOrderGammaResource(
        NextOrderGammaService nextOrderGammaService,
        NextOrderGammaRepository nextOrderGammaRepository,
        NextOrderGammaQueryService nextOrderGammaQueryService
    ) {
        this.nextOrderGammaService = nextOrderGammaService;
        this.nextOrderGammaRepository = nextOrderGammaRepository;
        this.nextOrderGammaQueryService = nextOrderGammaQueryService;
    }

    /**
     * {@code POST  /next-order-gammas} : Create a new nextOrderGamma.
     *
     * @param nextOrderGamma the nextOrderGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderGamma, or with status {@code 400 (Bad Request)} if the nextOrderGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderGamma> createNextOrderGamma(@Valid @RequestBody NextOrderGamma nextOrderGamma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextOrderGamma : {}", nextOrderGamma);
        if (nextOrderGamma.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderGamma = nextOrderGammaService.save(nextOrderGamma);
        return ResponseEntity.created(new URI("/api/next-order-gammas/" + nextOrderGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderGamma.getId().toString()))
            .body(nextOrderGamma);
    }

    /**
     * {@code PUT  /next-order-gammas/:id} : Updates an existing nextOrderGamma.
     *
     * @param id the id of the nextOrderGamma to save.
     * @param nextOrderGamma the nextOrderGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderGamma,
     * or with status {@code 400 (Bad Request)} if the nextOrderGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderGamma> updateNextOrderGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderGamma nextOrderGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderGamma : {}, {}", id, nextOrderGamma);
        if (nextOrderGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderGamma = nextOrderGammaService.update(nextOrderGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderGamma.getId().toString()))
            .body(nextOrderGamma);
    }

    /**
     * {@code PATCH  /next-order-gammas/:id} : Partial updates given fields of an existing nextOrderGamma, field will ignore if it is null
     *
     * @param id the id of the nextOrderGamma to save.
     * @param nextOrderGamma the nextOrderGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderGamma,
     * or with status {@code 400 (Bad Request)} if the nextOrderGamma is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderGamma> partialUpdateNextOrderGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderGamma nextOrderGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderGamma partially : {}, {}", id, nextOrderGamma);
        if (nextOrderGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderGamma> result = nextOrderGammaService.partialUpdate(nextOrderGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-gammas} : get all the nextOrderGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderGamma>> getAllNextOrderGammas(
        NextOrderGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderGammas by criteria: {}", criteria);

        Page<NextOrderGamma> page = nextOrderGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-gammas/count} : count all the nextOrderGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderGammas(NextOrderGammaCriteria criteria) {
        LOG.debug("REST request to count NextOrderGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-gammas/:id} : get the "id" nextOrderGamma.
     *
     * @param id the id of the nextOrderGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderGamma> getNextOrderGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderGamma : {}", id);
        Optional<NextOrderGamma> nextOrderGamma = nextOrderGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderGamma);
    }

    /**
     * {@code DELETE  /next-order-gammas/:id} : delete the "id" nextOrderGamma.
     *
     * @param id the id of the nextOrderGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderGamma : {}", id);
        nextOrderGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

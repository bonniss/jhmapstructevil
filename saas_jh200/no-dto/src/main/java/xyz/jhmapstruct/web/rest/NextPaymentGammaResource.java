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
import xyz.jhmapstruct.domain.NextPaymentGamma;
import xyz.jhmapstruct.repository.NextPaymentGammaRepository;
import xyz.jhmapstruct.service.NextPaymentGammaQueryService;
import xyz.jhmapstruct.service.NextPaymentGammaService;
import xyz.jhmapstruct.service.criteria.NextPaymentGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentGamma}.
 */
@RestController
@RequestMapping("/api/next-payment-gammas")
public class NextPaymentGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentGammaResource.class);

    private static final String ENTITY_NAME = "nextPaymentGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentGammaService nextPaymentGammaService;

    private final NextPaymentGammaRepository nextPaymentGammaRepository;

    private final NextPaymentGammaQueryService nextPaymentGammaQueryService;

    public NextPaymentGammaResource(
        NextPaymentGammaService nextPaymentGammaService,
        NextPaymentGammaRepository nextPaymentGammaRepository,
        NextPaymentGammaQueryService nextPaymentGammaQueryService
    ) {
        this.nextPaymentGammaService = nextPaymentGammaService;
        this.nextPaymentGammaRepository = nextPaymentGammaRepository;
        this.nextPaymentGammaQueryService = nextPaymentGammaQueryService;
    }

    /**
     * {@code POST  /next-payment-gammas} : Create a new nextPaymentGamma.
     *
     * @param nextPaymentGamma the nextPaymentGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentGamma, or with status {@code 400 (Bad Request)} if the nextPaymentGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentGamma> createNextPaymentGamma(@Valid @RequestBody NextPaymentGamma nextPaymentGamma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentGamma : {}", nextPaymentGamma);
        if (nextPaymentGamma.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentGamma = nextPaymentGammaService.save(nextPaymentGamma);
        return ResponseEntity.created(new URI("/api/next-payment-gammas/" + nextPaymentGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentGamma.getId().toString()))
            .body(nextPaymentGamma);
    }

    /**
     * {@code PUT  /next-payment-gammas/:id} : Updates an existing nextPaymentGamma.
     *
     * @param id the id of the nextPaymentGamma to save.
     * @param nextPaymentGamma the nextPaymentGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentGamma,
     * or with status {@code 400 (Bad Request)} if the nextPaymentGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentGamma> updateNextPaymentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentGamma nextPaymentGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentGamma : {}, {}", id, nextPaymentGamma);
        if (nextPaymentGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentGamma = nextPaymentGammaService.update(nextPaymentGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentGamma.getId().toString()))
            .body(nextPaymentGamma);
    }

    /**
     * {@code PATCH  /next-payment-gammas/:id} : Partial updates given fields of an existing nextPaymentGamma, field will ignore if it is null
     *
     * @param id the id of the nextPaymentGamma to save.
     * @param nextPaymentGamma the nextPaymentGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentGamma,
     * or with status {@code 400 (Bad Request)} if the nextPaymentGamma is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentGamma> partialUpdateNextPaymentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentGamma nextPaymentGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentGamma partially : {}, {}", id, nextPaymentGamma);
        if (nextPaymentGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentGamma> result = nextPaymentGammaService.partialUpdate(nextPaymentGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-gammas} : get all the nextPaymentGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentGamma>> getAllNextPaymentGammas(
        NextPaymentGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentGammas by criteria: {}", criteria);

        Page<NextPaymentGamma> page = nextPaymentGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-gammas/count} : count all the nextPaymentGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentGammas(NextPaymentGammaCriteria criteria) {
        LOG.debug("REST request to count NextPaymentGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-gammas/:id} : get the "id" nextPaymentGamma.
     *
     * @param id the id of the nextPaymentGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentGamma> getNextPaymentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentGamma : {}", id);
        Optional<NextPaymentGamma> nextPaymentGamma = nextPaymentGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentGamma);
    }

    /**
     * {@code DELETE  /next-payment-gammas/:id} : delete the "id" nextPaymentGamma.
     *
     * @param id the id of the nextPaymentGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentGamma : {}", id);
        nextPaymentGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

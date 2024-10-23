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
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.repository.NextPaymentSigmaRepository;
import xyz.jhmapstruct.service.NextPaymentSigmaQueryService;
import xyz.jhmapstruct.service.NextPaymentSigmaService;
import xyz.jhmapstruct.service.criteria.NextPaymentSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentSigma}.
 */
@RestController
@RequestMapping("/api/next-payment-sigmas")
public class NextPaymentSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentSigmaResource.class);

    private static final String ENTITY_NAME = "nextPaymentSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentSigmaService nextPaymentSigmaService;

    private final NextPaymentSigmaRepository nextPaymentSigmaRepository;

    private final NextPaymentSigmaQueryService nextPaymentSigmaQueryService;

    public NextPaymentSigmaResource(
        NextPaymentSigmaService nextPaymentSigmaService,
        NextPaymentSigmaRepository nextPaymentSigmaRepository,
        NextPaymentSigmaQueryService nextPaymentSigmaQueryService
    ) {
        this.nextPaymentSigmaService = nextPaymentSigmaService;
        this.nextPaymentSigmaRepository = nextPaymentSigmaRepository;
        this.nextPaymentSigmaQueryService = nextPaymentSigmaQueryService;
    }

    /**
     * {@code POST  /next-payment-sigmas} : Create a new nextPaymentSigma.
     *
     * @param nextPaymentSigma the nextPaymentSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentSigma, or with status {@code 400 (Bad Request)} if the nextPaymentSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentSigma> createNextPaymentSigma(@Valid @RequestBody NextPaymentSigma nextPaymentSigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentSigma : {}", nextPaymentSigma);
        if (nextPaymentSigma.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentSigma = nextPaymentSigmaService.save(nextPaymentSigma);
        return ResponseEntity.created(new URI("/api/next-payment-sigmas/" + nextPaymentSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentSigma.getId().toString()))
            .body(nextPaymentSigma);
    }

    /**
     * {@code PUT  /next-payment-sigmas/:id} : Updates an existing nextPaymentSigma.
     *
     * @param id the id of the nextPaymentSigma to save.
     * @param nextPaymentSigma the nextPaymentSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentSigma,
     * or with status {@code 400 (Bad Request)} if the nextPaymentSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentSigma> updateNextPaymentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentSigma nextPaymentSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentSigma : {}, {}", id, nextPaymentSigma);
        if (nextPaymentSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentSigma = nextPaymentSigmaService.update(nextPaymentSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentSigma.getId().toString()))
            .body(nextPaymentSigma);
    }

    /**
     * {@code PATCH  /next-payment-sigmas/:id} : Partial updates given fields of an existing nextPaymentSigma, field will ignore if it is null
     *
     * @param id the id of the nextPaymentSigma to save.
     * @param nextPaymentSigma the nextPaymentSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentSigma,
     * or with status {@code 400 (Bad Request)} if the nextPaymentSigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentSigma> partialUpdateNextPaymentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentSigma nextPaymentSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentSigma partially : {}, {}", id, nextPaymentSigma);
        if (nextPaymentSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentSigma> result = nextPaymentSigmaService.partialUpdate(nextPaymentSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-sigmas} : get all the nextPaymentSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentSigma>> getAllNextPaymentSigmas(
        NextPaymentSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentSigmas by criteria: {}", criteria);

        Page<NextPaymentSigma> page = nextPaymentSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-sigmas/count} : count all the nextPaymentSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentSigmas(NextPaymentSigmaCriteria criteria) {
        LOG.debug("REST request to count NextPaymentSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-sigmas/:id} : get the "id" nextPaymentSigma.
     *
     * @param id the id of the nextPaymentSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentSigma> getNextPaymentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentSigma : {}", id);
        Optional<NextPaymentSigma> nextPaymentSigma = nextPaymentSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentSigma);
    }

    /**
     * {@code DELETE  /next-payment-sigmas/:id} : delete the "id" nextPaymentSigma.
     *
     * @param id the id of the nextPaymentSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentSigma : {}", id);
        nextPaymentSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

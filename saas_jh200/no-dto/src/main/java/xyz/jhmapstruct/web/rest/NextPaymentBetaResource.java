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
import xyz.jhmapstruct.domain.NextPaymentBeta;
import xyz.jhmapstruct.repository.NextPaymentBetaRepository;
import xyz.jhmapstruct.service.NextPaymentBetaQueryService;
import xyz.jhmapstruct.service.NextPaymentBetaService;
import xyz.jhmapstruct.service.criteria.NextPaymentBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentBeta}.
 */
@RestController
@RequestMapping("/api/next-payment-betas")
public class NextPaymentBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentBetaResource.class);

    private static final String ENTITY_NAME = "nextPaymentBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentBetaService nextPaymentBetaService;

    private final NextPaymentBetaRepository nextPaymentBetaRepository;

    private final NextPaymentBetaQueryService nextPaymentBetaQueryService;

    public NextPaymentBetaResource(
        NextPaymentBetaService nextPaymentBetaService,
        NextPaymentBetaRepository nextPaymentBetaRepository,
        NextPaymentBetaQueryService nextPaymentBetaQueryService
    ) {
        this.nextPaymentBetaService = nextPaymentBetaService;
        this.nextPaymentBetaRepository = nextPaymentBetaRepository;
        this.nextPaymentBetaQueryService = nextPaymentBetaQueryService;
    }

    /**
     * {@code POST  /next-payment-betas} : Create a new nextPaymentBeta.
     *
     * @param nextPaymentBeta the nextPaymentBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentBeta, or with status {@code 400 (Bad Request)} if the nextPaymentBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentBeta> createNextPaymentBeta(@Valid @RequestBody NextPaymentBeta nextPaymentBeta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentBeta : {}", nextPaymentBeta);
        if (nextPaymentBeta.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentBeta = nextPaymentBetaService.save(nextPaymentBeta);
        return ResponseEntity.created(new URI("/api/next-payment-betas/" + nextPaymentBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentBeta.getId().toString()))
            .body(nextPaymentBeta);
    }

    /**
     * {@code PUT  /next-payment-betas/:id} : Updates an existing nextPaymentBeta.
     *
     * @param id the id of the nextPaymentBeta to save.
     * @param nextPaymentBeta the nextPaymentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentBeta,
     * or with status {@code 400 (Bad Request)} if the nextPaymentBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentBeta> updateNextPaymentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentBeta nextPaymentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentBeta : {}, {}", id, nextPaymentBeta);
        if (nextPaymentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentBeta = nextPaymentBetaService.update(nextPaymentBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentBeta.getId().toString()))
            .body(nextPaymentBeta);
    }

    /**
     * {@code PATCH  /next-payment-betas/:id} : Partial updates given fields of an existing nextPaymentBeta, field will ignore if it is null
     *
     * @param id the id of the nextPaymentBeta to save.
     * @param nextPaymentBeta the nextPaymentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentBeta,
     * or with status {@code 400 (Bad Request)} if the nextPaymentBeta is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentBeta> partialUpdateNextPaymentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentBeta nextPaymentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentBeta partially : {}, {}", id, nextPaymentBeta);
        if (nextPaymentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentBeta> result = nextPaymentBetaService.partialUpdate(nextPaymentBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-betas} : get all the nextPaymentBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentBeta>> getAllNextPaymentBetas(
        NextPaymentBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentBetas by criteria: {}", criteria);

        Page<NextPaymentBeta> page = nextPaymentBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-betas/count} : count all the nextPaymentBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentBetas(NextPaymentBetaCriteria criteria) {
        LOG.debug("REST request to count NextPaymentBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-betas/:id} : get the "id" nextPaymentBeta.
     *
     * @param id the id of the nextPaymentBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentBeta> getNextPaymentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentBeta : {}", id);
        Optional<NextPaymentBeta> nextPaymentBeta = nextPaymentBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentBeta);
    }

    /**
     * {@code DELETE  /next-payment-betas/:id} : delete the "id" nextPaymentBeta.
     *
     * @param id the id of the nextPaymentBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentBeta : {}", id);
        nextPaymentBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

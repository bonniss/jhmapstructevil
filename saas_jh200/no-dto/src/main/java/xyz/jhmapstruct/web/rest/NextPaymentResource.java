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
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.repository.NextPaymentRepository;
import xyz.jhmapstruct.service.NextPaymentQueryService;
import xyz.jhmapstruct.service.NextPaymentService;
import xyz.jhmapstruct.service.criteria.NextPaymentCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPayment}.
 */
@RestController
@RequestMapping("/api/next-payments")
public class NextPaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentResource.class);

    private static final String ENTITY_NAME = "nextPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentService nextPaymentService;

    private final NextPaymentRepository nextPaymentRepository;

    private final NextPaymentQueryService nextPaymentQueryService;

    public NextPaymentResource(
        NextPaymentService nextPaymentService,
        NextPaymentRepository nextPaymentRepository,
        NextPaymentQueryService nextPaymentQueryService
    ) {
        this.nextPaymentService = nextPaymentService;
        this.nextPaymentRepository = nextPaymentRepository;
        this.nextPaymentQueryService = nextPaymentQueryService;
    }

    /**
     * {@code POST  /next-payments} : Create a new nextPayment.
     *
     * @param nextPayment the nextPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPayment, or with status {@code 400 (Bad Request)} if the nextPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPayment> createNextPayment(@Valid @RequestBody NextPayment nextPayment) throws URISyntaxException {
        LOG.debug("REST request to save NextPayment : {}", nextPayment);
        if (nextPayment.getId() != null) {
            throw new BadRequestAlertException("A new nextPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPayment = nextPaymentService.save(nextPayment);
        return ResponseEntity.created(new URI("/api/next-payments/" + nextPayment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPayment.getId().toString()))
            .body(nextPayment);
    }

    /**
     * {@code PUT  /next-payments/:id} : Updates an existing nextPayment.
     *
     * @param id the id of the nextPayment to save.
     * @param nextPayment the nextPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPayment,
     * or with status {@code 400 (Bad Request)} if the nextPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPayment> updateNextPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPayment nextPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPayment : {}, {}", id, nextPayment);
        if (nextPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPayment = nextPaymentService.update(nextPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPayment.getId().toString()))
            .body(nextPayment);
    }

    /**
     * {@code PATCH  /next-payments/:id} : Partial updates given fields of an existing nextPayment, field will ignore if it is null
     *
     * @param id the id of the nextPayment to save.
     * @param nextPayment the nextPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPayment,
     * or with status {@code 400 (Bad Request)} if the nextPayment is not valid,
     * or with status {@code 404 (Not Found)} if the nextPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPayment> partialUpdateNextPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPayment nextPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPayment partially : {}, {}", id, nextPayment);
        if (nextPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPayment> result = nextPaymentService.partialUpdate(nextPayment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPayment.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payments} : get all the nextPayments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPayments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPayment>> getAllNextPayments(
        NextPaymentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPayments by criteria: {}", criteria);

        Page<NextPayment> page = nextPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payments/count} : count all the nextPayments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPayments(NextPaymentCriteria criteria) {
        LOG.debug("REST request to count NextPayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payments/:id} : get the "id" nextPayment.
     *
     * @param id the id of the nextPayment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPayment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPayment> getNextPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPayment : {}", id);
        Optional<NextPayment> nextPayment = nextPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPayment);
    }

    /**
     * {@code DELETE  /next-payments/:id} : delete the "id" nextPayment.
     *
     * @param id the id of the nextPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPayment : {}", id);
        nextPaymentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

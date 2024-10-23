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
import xyz.jhmapstruct.domain.PaymentVi;
import xyz.jhmapstruct.repository.PaymentViRepository;
import xyz.jhmapstruct.service.PaymentViQueryService;
import xyz.jhmapstruct.service.PaymentViService;
import xyz.jhmapstruct.service.criteria.PaymentViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentVi}.
 */
@RestController
@RequestMapping("/api/payment-vis")
public class PaymentViResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViResource.class);

    private static final String ENTITY_NAME = "paymentVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentViService paymentViService;

    private final PaymentViRepository paymentViRepository;

    private final PaymentViQueryService paymentViQueryService;

    public PaymentViResource(
        PaymentViService paymentViService,
        PaymentViRepository paymentViRepository,
        PaymentViQueryService paymentViQueryService
    ) {
        this.paymentViService = paymentViService;
        this.paymentViRepository = paymentViRepository;
        this.paymentViQueryService = paymentViQueryService;
    }

    /**
     * {@code POST  /payment-vis} : Create a new paymentVi.
     *
     * @param paymentVi the paymentVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentVi, or with status {@code 400 (Bad Request)} if the paymentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentVi> createPaymentVi(@Valid @RequestBody PaymentVi paymentVi) throws URISyntaxException {
        LOG.debug("REST request to save PaymentVi : {}", paymentVi);
        if (paymentVi.getId() != null) {
            throw new BadRequestAlertException("A new paymentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentVi = paymentViService.save(paymentVi);
        return ResponseEntity.created(new URI("/api/payment-vis/" + paymentVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentVi.getId().toString()))
            .body(paymentVi);
    }

    /**
     * {@code PUT  /payment-vis/:id} : Updates an existing paymentVi.
     *
     * @param id the id of the paymentVi to save.
     * @param paymentVi the paymentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentVi,
     * or with status {@code 400 (Bad Request)} if the paymentVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentVi> updatePaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentVi paymentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentVi : {}, {}", id, paymentVi);
        if (paymentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentVi = paymentViService.update(paymentVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentVi.getId().toString()))
            .body(paymentVi);
    }

    /**
     * {@code PATCH  /payment-vis/:id} : Partial updates given fields of an existing paymentVi, field will ignore if it is null
     *
     * @param id the id of the paymentVi to save.
     * @param paymentVi the paymentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentVi,
     * or with status {@code 400 (Bad Request)} if the paymentVi is not valid,
     * or with status {@code 404 (Not Found)} if the paymentVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentVi> partialUpdatePaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentVi paymentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentVi partially : {}, {}", id, paymentVi);
        if (paymentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentVi> result = paymentViService.partialUpdate(paymentVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentVi.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-vis} : get all the paymentVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentVi>> getAllPaymentVis(
        PaymentViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentVis by criteria: {}", criteria);

        Page<PaymentVi> page = paymentViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-vis/count} : count all the paymentVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentVis(PaymentViCriteria criteria) {
        LOG.debug("REST request to count PaymentVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-vis/:id} : get the "id" paymentVi.
     *
     * @param id the id of the paymentVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentVi> getPaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentVi : {}", id);
        Optional<PaymentVi> paymentVi = paymentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentVi);
    }

    /**
     * {@code DELETE  /payment-vis/:id} : delete the "id" paymentVi.
     *
     * @param id the id of the paymentVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentVi : {}", id);
        paymentViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

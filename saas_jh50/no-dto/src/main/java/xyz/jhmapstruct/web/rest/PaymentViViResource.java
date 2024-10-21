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
import xyz.jhmapstruct.domain.PaymentViVi;
import xyz.jhmapstruct.repository.PaymentViViRepository;
import xyz.jhmapstruct.service.PaymentViViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentViVi}.
 */
@RestController
@RequestMapping("/api/payment-vi-vis")
public class PaymentViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentViViResource.class);

    private static final String ENTITY_NAME = "paymentViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentViViService paymentViViService;

    private final PaymentViViRepository paymentViViRepository;

    public PaymentViViResource(PaymentViViService paymentViViService, PaymentViViRepository paymentViViRepository) {
        this.paymentViViService = paymentViViService;
        this.paymentViViRepository = paymentViViRepository;
    }

    /**
     * {@code POST  /payment-vi-vis} : Create a new paymentViVi.
     *
     * @param paymentViVi the paymentViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentViVi, or with status {@code 400 (Bad Request)} if the paymentViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentViVi> createPaymentViVi(@Valid @RequestBody PaymentViVi paymentViVi) throws URISyntaxException {
        LOG.debug("REST request to save PaymentViVi : {}", paymentViVi);
        if (paymentViVi.getId() != null) {
            throw new BadRequestAlertException("A new paymentViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentViVi = paymentViViService.save(paymentViVi);
        return ResponseEntity.created(new URI("/api/payment-vi-vis/" + paymentViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentViVi.getId().toString()))
            .body(paymentViVi);
    }

    /**
     * {@code PUT  /payment-vi-vis/:id} : Updates an existing paymentViVi.
     *
     * @param id the id of the paymentViVi to save.
     * @param paymentViVi the paymentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentViVi,
     * or with status {@code 400 (Bad Request)} if the paymentViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentViVi> updatePaymentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentViVi paymentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentViVi : {}, {}", id, paymentViVi);
        if (paymentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentViVi = paymentViViService.update(paymentViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentViVi.getId().toString()))
            .body(paymentViVi);
    }

    /**
     * {@code PATCH  /payment-vi-vis/:id} : Partial updates given fields of an existing paymentViVi, field will ignore if it is null
     *
     * @param id the id of the paymentViVi to save.
     * @param paymentViVi the paymentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentViVi,
     * or with status {@code 400 (Bad Request)} if the paymentViVi is not valid,
     * or with status {@code 404 (Not Found)} if the paymentViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentViVi> partialUpdatePaymentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentViVi paymentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentViVi partially : {}, {}", id, paymentViVi);
        if (paymentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentViVi> result = paymentViViService.partialUpdate(paymentViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-vi-vis} : get all the paymentViVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentViVis in body.
     */
    @GetMapping("")
    public List<PaymentViVi> getAllPaymentViVis() {
        LOG.debug("REST request to get all PaymentViVis");
        return paymentViViService.findAll();
    }

    /**
     * {@code GET  /payment-vi-vis/:id} : get the "id" paymentViVi.
     *
     * @param id the id of the paymentViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentViVi> getPaymentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentViVi : {}", id);
        Optional<PaymentViVi> paymentViVi = paymentViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentViVi);
    }

    /**
     * {@code DELETE  /payment-vi-vis/:id} : delete the "id" paymentViVi.
     *
     * @param id the id of the paymentViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentViVi : {}", id);
        paymentViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

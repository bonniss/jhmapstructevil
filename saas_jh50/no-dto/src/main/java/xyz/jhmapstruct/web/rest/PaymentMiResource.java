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
import xyz.jhmapstruct.domain.PaymentMi;
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.PaymentMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentMi}.
 */
@RestController
@RequestMapping("/api/payment-mis")
public class PaymentMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiResource.class);

    private static final String ENTITY_NAME = "paymentMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentMiService paymentMiService;

    private final PaymentMiRepository paymentMiRepository;

    public PaymentMiResource(PaymentMiService paymentMiService, PaymentMiRepository paymentMiRepository) {
        this.paymentMiService = paymentMiService;
        this.paymentMiRepository = paymentMiRepository;
    }

    /**
     * {@code POST  /payment-mis} : Create a new paymentMi.
     *
     * @param paymentMi the paymentMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMi, or with status {@code 400 (Bad Request)} if the paymentMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentMi> createPaymentMi(@Valid @RequestBody PaymentMi paymentMi) throws URISyntaxException {
        LOG.debug("REST request to save PaymentMi : {}", paymentMi);
        if (paymentMi.getId() != null) {
            throw new BadRequestAlertException("A new paymentMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentMi = paymentMiService.save(paymentMi);
        return ResponseEntity.created(new URI("/api/payment-mis/" + paymentMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentMi.getId().toString()))
            .body(paymentMi);
    }

    /**
     * {@code PUT  /payment-mis/:id} : Updates an existing paymentMi.
     *
     * @param id the id of the paymentMi to save.
     * @param paymentMi the paymentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMi,
     * or with status {@code 400 (Bad Request)} if the paymentMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMi> updatePaymentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentMi paymentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentMi : {}, {}", id, paymentMi);
        if (paymentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentMi = paymentMiService.update(paymentMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMi.getId().toString()))
            .body(paymentMi);
    }

    /**
     * {@code PATCH  /payment-mis/:id} : Partial updates given fields of an existing paymentMi, field will ignore if it is null
     *
     * @param id the id of the paymentMi to save.
     * @param paymentMi the paymentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMi,
     * or with status {@code 400 (Bad Request)} if the paymentMi is not valid,
     * or with status {@code 404 (Not Found)} if the paymentMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentMi> partialUpdatePaymentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentMi paymentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentMi partially : {}, {}", id, paymentMi);
        if (paymentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMi> result = paymentMiService.partialUpdate(paymentMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMi.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-mis} : get all the paymentMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMis in body.
     */
    @GetMapping("")
    public List<PaymentMi> getAllPaymentMis() {
        LOG.debug("REST request to get all PaymentMis");
        return paymentMiService.findAll();
    }

    /**
     * {@code GET  /payment-mis/:id} : get the "id" paymentMi.
     *
     * @param id the id of the paymentMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMi> getPaymentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentMi : {}", id);
        Optional<PaymentMi> paymentMi = paymentMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMi);
    }

    /**
     * {@code DELETE  /payment-mis/:id} : delete the "id" paymentMi.
     *
     * @param id the id of the paymentMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentMi : {}", id);
        paymentMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

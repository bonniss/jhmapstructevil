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
import xyz.jhmapstruct.domain.PaymentGamma;
import xyz.jhmapstruct.repository.PaymentGammaRepository;
import xyz.jhmapstruct.service.PaymentGammaQueryService;
import xyz.jhmapstruct.service.PaymentGammaService;
import xyz.jhmapstruct.service.criteria.PaymentGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentGamma}.
 */
@RestController
@RequestMapping("/api/payment-gammas")
public class PaymentGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentGammaResource.class);

    private static final String ENTITY_NAME = "paymentGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentGammaService paymentGammaService;

    private final PaymentGammaRepository paymentGammaRepository;

    private final PaymentGammaQueryService paymentGammaQueryService;

    public PaymentGammaResource(
        PaymentGammaService paymentGammaService,
        PaymentGammaRepository paymentGammaRepository,
        PaymentGammaQueryService paymentGammaQueryService
    ) {
        this.paymentGammaService = paymentGammaService;
        this.paymentGammaRepository = paymentGammaRepository;
        this.paymentGammaQueryService = paymentGammaQueryService;
    }

    /**
     * {@code POST  /payment-gammas} : Create a new paymentGamma.
     *
     * @param paymentGamma the paymentGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentGamma, or with status {@code 400 (Bad Request)} if the paymentGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentGamma> createPaymentGamma(@Valid @RequestBody PaymentGamma paymentGamma) throws URISyntaxException {
        LOG.debug("REST request to save PaymentGamma : {}", paymentGamma);
        if (paymentGamma.getId() != null) {
            throw new BadRequestAlertException("A new paymentGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentGamma = paymentGammaService.save(paymentGamma);
        return ResponseEntity.created(new URI("/api/payment-gammas/" + paymentGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentGamma.getId().toString()))
            .body(paymentGamma);
    }

    /**
     * {@code PUT  /payment-gammas/:id} : Updates an existing paymentGamma.
     *
     * @param id the id of the paymentGamma to save.
     * @param paymentGamma the paymentGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentGamma,
     * or with status {@code 400 (Bad Request)} if the paymentGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentGamma> updatePaymentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentGamma paymentGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentGamma : {}, {}", id, paymentGamma);
        if (paymentGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentGamma = paymentGammaService.update(paymentGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGamma.getId().toString()))
            .body(paymentGamma);
    }

    /**
     * {@code PATCH  /payment-gammas/:id} : Partial updates given fields of an existing paymentGamma, field will ignore if it is null
     *
     * @param id the id of the paymentGamma to save.
     * @param paymentGamma the paymentGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentGamma,
     * or with status {@code 400 (Bad Request)} if the paymentGamma is not valid,
     * or with status {@code 404 (Not Found)} if the paymentGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentGamma> partialUpdatePaymentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentGamma paymentGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentGamma partially : {}, {}", id, paymentGamma);
        if (paymentGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentGamma> result = paymentGammaService.partialUpdate(paymentGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-gammas} : get all the paymentGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentGamma>> getAllPaymentGammas(
        PaymentGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentGammas by criteria: {}", criteria);

        Page<PaymentGamma> page = paymentGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-gammas/count} : count all the paymentGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentGammas(PaymentGammaCriteria criteria) {
        LOG.debug("REST request to count PaymentGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-gammas/:id} : get the "id" paymentGamma.
     *
     * @param id the id of the paymentGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentGamma> getPaymentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentGamma : {}", id);
        Optional<PaymentGamma> paymentGamma = paymentGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentGamma);
    }

    /**
     * {@code DELETE  /payment-gammas/:id} : delete the "id" paymentGamma.
     *
     * @param id the id of the paymentGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentGamma : {}", id);
        paymentGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

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
import xyz.jhmapstruct.domain.PaymentAlpha;
import xyz.jhmapstruct.repository.PaymentAlphaRepository;
import xyz.jhmapstruct.service.PaymentAlphaQueryService;
import xyz.jhmapstruct.service.PaymentAlphaService;
import xyz.jhmapstruct.service.criteria.PaymentAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentAlpha}.
 */
@RestController
@RequestMapping("/api/payment-alphas")
public class PaymentAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentAlphaResource.class);

    private static final String ENTITY_NAME = "paymentAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentAlphaService paymentAlphaService;

    private final PaymentAlphaRepository paymentAlphaRepository;

    private final PaymentAlphaQueryService paymentAlphaQueryService;

    public PaymentAlphaResource(
        PaymentAlphaService paymentAlphaService,
        PaymentAlphaRepository paymentAlphaRepository,
        PaymentAlphaQueryService paymentAlphaQueryService
    ) {
        this.paymentAlphaService = paymentAlphaService;
        this.paymentAlphaRepository = paymentAlphaRepository;
        this.paymentAlphaQueryService = paymentAlphaQueryService;
    }

    /**
     * {@code POST  /payment-alphas} : Create a new paymentAlpha.
     *
     * @param paymentAlpha the paymentAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentAlpha, or with status {@code 400 (Bad Request)} if the paymentAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentAlpha> createPaymentAlpha(@Valid @RequestBody PaymentAlpha paymentAlpha) throws URISyntaxException {
        LOG.debug("REST request to save PaymentAlpha : {}", paymentAlpha);
        if (paymentAlpha.getId() != null) {
            throw new BadRequestAlertException("A new paymentAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentAlpha = paymentAlphaService.save(paymentAlpha);
        return ResponseEntity.created(new URI("/api/payment-alphas/" + paymentAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentAlpha.getId().toString()))
            .body(paymentAlpha);
    }

    /**
     * {@code PUT  /payment-alphas/:id} : Updates an existing paymentAlpha.
     *
     * @param id the id of the paymentAlpha to save.
     * @param paymentAlpha the paymentAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentAlpha,
     * or with status {@code 400 (Bad Request)} if the paymentAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentAlpha> updatePaymentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentAlpha paymentAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentAlpha : {}, {}", id, paymentAlpha);
        if (paymentAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentAlpha = paymentAlphaService.update(paymentAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentAlpha.getId().toString()))
            .body(paymentAlpha);
    }

    /**
     * {@code PATCH  /payment-alphas/:id} : Partial updates given fields of an existing paymentAlpha, field will ignore if it is null
     *
     * @param id the id of the paymentAlpha to save.
     * @param paymentAlpha the paymentAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentAlpha,
     * or with status {@code 400 (Bad Request)} if the paymentAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the paymentAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentAlpha> partialUpdatePaymentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentAlpha paymentAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentAlpha partially : {}, {}", id, paymentAlpha);
        if (paymentAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentAlpha> result = paymentAlphaService.partialUpdate(paymentAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-alphas} : get all the paymentAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentAlpha>> getAllPaymentAlphas(
        PaymentAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentAlphas by criteria: {}", criteria);

        Page<PaymentAlpha> page = paymentAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-alphas/count} : count all the paymentAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentAlphas(PaymentAlphaCriteria criteria) {
        LOG.debug("REST request to count PaymentAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-alphas/:id} : get the "id" paymentAlpha.
     *
     * @param id the id of the paymentAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentAlpha> getPaymentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentAlpha : {}", id);
        Optional<PaymentAlpha> paymentAlpha = paymentAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentAlpha);
    }

    /**
     * {@code DELETE  /payment-alphas/:id} : delete the "id" paymentAlpha.
     *
     * @param id the id of the paymentAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentAlpha : {}", id);
        paymentAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

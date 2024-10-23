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
import xyz.jhmapstruct.domain.PaymentSigma;
import xyz.jhmapstruct.repository.PaymentSigmaRepository;
import xyz.jhmapstruct.service.PaymentSigmaQueryService;
import xyz.jhmapstruct.service.PaymentSigmaService;
import xyz.jhmapstruct.service.criteria.PaymentSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentSigma}.
 */
@RestController
@RequestMapping("/api/payment-sigmas")
public class PaymentSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentSigmaResource.class);

    private static final String ENTITY_NAME = "paymentSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentSigmaService paymentSigmaService;

    private final PaymentSigmaRepository paymentSigmaRepository;

    private final PaymentSigmaQueryService paymentSigmaQueryService;

    public PaymentSigmaResource(
        PaymentSigmaService paymentSigmaService,
        PaymentSigmaRepository paymentSigmaRepository,
        PaymentSigmaQueryService paymentSigmaQueryService
    ) {
        this.paymentSigmaService = paymentSigmaService;
        this.paymentSigmaRepository = paymentSigmaRepository;
        this.paymentSigmaQueryService = paymentSigmaQueryService;
    }

    /**
     * {@code POST  /payment-sigmas} : Create a new paymentSigma.
     *
     * @param paymentSigma the paymentSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentSigma, or with status {@code 400 (Bad Request)} if the paymentSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentSigma> createPaymentSigma(@Valid @RequestBody PaymentSigma paymentSigma) throws URISyntaxException {
        LOG.debug("REST request to save PaymentSigma : {}", paymentSigma);
        if (paymentSigma.getId() != null) {
            throw new BadRequestAlertException("A new paymentSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentSigma = paymentSigmaService.save(paymentSigma);
        return ResponseEntity.created(new URI("/api/payment-sigmas/" + paymentSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentSigma.getId().toString()))
            .body(paymentSigma);
    }

    /**
     * {@code PUT  /payment-sigmas/:id} : Updates an existing paymentSigma.
     *
     * @param id the id of the paymentSigma to save.
     * @param paymentSigma the paymentSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSigma,
     * or with status {@code 400 (Bad Request)} if the paymentSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentSigma> updatePaymentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentSigma paymentSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentSigma : {}, {}", id, paymentSigma);
        if (paymentSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentSigma = paymentSigmaService.update(paymentSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSigma.getId().toString()))
            .body(paymentSigma);
    }

    /**
     * {@code PATCH  /payment-sigmas/:id} : Partial updates given fields of an existing paymentSigma, field will ignore if it is null
     *
     * @param id the id of the paymentSigma to save.
     * @param paymentSigma the paymentSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentSigma,
     * or with status {@code 400 (Bad Request)} if the paymentSigma is not valid,
     * or with status {@code 404 (Not Found)} if the paymentSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentSigma> partialUpdatePaymentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentSigma paymentSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentSigma partially : {}, {}", id, paymentSigma);
        if (paymentSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentSigma> result = paymentSigmaService.partialUpdate(paymentSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-sigmas} : get all the paymentSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentSigma>> getAllPaymentSigmas(
        PaymentSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentSigmas by criteria: {}", criteria);

        Page<PaymentSigma> page = paymentSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-sigmas/count} : count all the paymentSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentSigmas(PaymentSigmaCriteria criteria) {
        LOG.debug("REST request to count PaymentSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-sigmas/:id} : get the "id" paymentSigma.
     *
     * @param id the id of the paymentSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSigma> getPaymentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentSigma : {}", id);
        Optional<PaymentSigma> paymentSigma = paymentSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentSigma);
    }

    /**
     * {@code DELETE  /payment-sigmas/:id} : delete the "id" paymentSigma.
     *
     * @param id the id of the paymentSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentSigma : {}", id);
        paymentSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

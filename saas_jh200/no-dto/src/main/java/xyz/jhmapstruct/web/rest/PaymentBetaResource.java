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
import xyz.jhmapstruct.domain.PaymentBeta;
import xyz.jhmapstruct.repository.PaymentBetaRepository;
import xyz.jhmapstruct.service.PaymentBetaQueryService;
import xyz.jhmapstruct.service.PaymentBetaService;
import xyz.jhmapstruct.service.criteria.PaymentBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentBeta}.
 */
@RestController
@RequestMapping("/api/payment-betas")
public class PaymentBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentBetaResource.class);

    private static final String ENTITY_NAME = "paymentBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentBetaService paymentBetaService;

    private final PaymentBetaRepository paymentBetaRepository;

    private final PaymentBetaQueryService paymentBetaQueryService;

    public PaymentBetaResource(
        PaymentBetaService paymentBetaService,
        PaymentBetaRepository paymentBetaRepository,
        PaymentBetaQueryService paymentBetaQueryService
    ) {
        this.paymentBetaService = paymentBetaService;
        this.paymentBetaRepository = paymentBetaRepository;
        this.paymentBetaQueryService = paymentBetaQueryService;
    }

    /**
     * {@code POST  /payment-betas} : Create a new paymentBeta.
     *
     * @param paymentBeta the paymentBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentBeta, or with status {@code 400 (Bad Request)} if the paymentBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentBeta> createPaymentBeta(@Valid @RequestBody PaymentBeta paymentBeta) throws URISyntaxException {
        LOG.debug("REST request to save PaymentBeta : {}", paymentBeta);
        if (paymentBeta.getId() != null) {
            throw new BadRequestAlertException("A new paymentBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentBeta = paymentBetaService.save(paymentBeta);
        return ResponseEntity.created(new URI("/api/payment-betas/" + paymentBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentBeta.getId().toString()))
            .body(paymentBeta);
    }

    /**
     * {@code PUT  /payment-betas/:id} : Updates an existing paymentBeta.
     *
     * @param id the id of the paymentBeta to save.
     * @param paymentBeta the paymentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentBeta,
     * or with status {@code 400 (Bad Request)} if the paymentBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentBeta> updatePaymentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentBeta paymentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentBeta : {}, {}", id, paymentBeta);
        if (paymentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentBeta = paymentBetaService.update(paymentBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentBeta.getId().toString()))
            .body(paymentBeta);
    }

    /**
     * {@code PATCH  /payment-betas/:id} : Partial updates given fields of an existing paymentBeta, field will ignore if it is null
     *
     * @param id the id of the paymentBeta to save.
     * @param paymentBeta the paymentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentBeta,
     * or with status {@code 400 (Bad Request)} if the paymentBeta is not valid,
     * or with status {@code 404 (Not Found)} if the paymentBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentBeta> partialUpdatePaymentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentBeta paymentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentBeta partially : {}, {}", id, paymentBeta);
        if (paymentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentBeta> result = paymentBetaService.partialUpdate(paymentBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-betas} : get all the paymentBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentBeta>> getAllPaymentBetas(
        PaymentBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentBetas by criteria: {}", criteria);

        Page<PaymentBeta> page = paymentBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-betas/count} : count all the paymentBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentBetas(PaymentBetaCriteria criteria) {
        LOG.debug("REST request to count PaymentBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-betas/:id} : get the "id" paymentBeta.
     *
     * @param id the id of the paymentBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentBeta> getPaymentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentBeta : {}", id);
        Optional<PaymentBeta> paymentBeta = paymentBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentBeta);
    }

    /**
     * {@code DELETE  /payment-betas/:id} : delete the "id" paymentBeta.
     *
     * @param id the id of the paymentBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentBeta : {}", id);
        paymentBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

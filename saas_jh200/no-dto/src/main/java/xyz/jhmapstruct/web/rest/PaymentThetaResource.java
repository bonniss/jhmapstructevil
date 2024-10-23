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
import xyz.jhmapstruct.domain.PaymentTheta;
import xyz.jhmapstruct.repository.PaymentThetaRepository;
import xyz.jhmapstruct.service.PaymentThetaQueryService;
import xyz.jhmapstruct.service.PaymentThetaService;
import xyz.jhmapstruct.service.criteria.PaymentThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentTheta}.
 */
@RestController
@RequestMapping("/api/payment-thetas")
public class PaymentThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentThetaResource.class);

    private static final String ENTITY_NAME = "paymentTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentThetaService paymentThetaService;

    private final PaymentThetaRepository paymentThetaRepository;

    private final PaymentThetaQueryService paymentThetaQueryService;

    public PaymentThetaResource(
        PaymentThetaService paymentThetaService,
        PaymentThetaRepository paymentThetaRepository,
        PaymentThetaQueryService paymentThetaQueryService
    ) {
        this.paymentThetaService = paymentThetaService;
        this.paymentThetaRepository = paymentThetaRepository;
        this.paymentThetaQueryService = paymentThetaQueryService;
    }

    /**
     * {@code POST  /payment-thetas} : Create a new paymentTheta.
     *
     * @param paymentTheta the paymentTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentTheta, or with status {@code 400 (Bad Request)} if the paymentTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentTheta> createPaymentTheta(@Valid @RequestBody PaymentTheta paymentTheta) throws URISyntaxException {
        LOG.debug("REST request to save PaymentTheta : {}", paymentTheta);
        if (paymentTheta.getId() != null) {
            throw new BadRequestAlertException("A new paymentTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentTheta = paymentThetaService.save(paymentTheta);
        return ResponseEntity.created(new URI("/api/payment-thetas/" + paymentTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentTheta.getId().toString()))
            .body(paymentTheta);
    }

    /**
     * {@code PUT  /payment-thetas/:id} : Updates an existing paymentTheta.
     *
     * @param id the id of the paymentTheta to save.
     * @param paymentTheta the paymentTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTheta,
     * or with status {@code 400 (Bad Request)} if the paymentTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentTheta> updatePaymentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentTheta paymentTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentTheta : {}, {}", id, paymentTheta);
        if (paymentTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentTheta = paymentThetaService.update(paymentTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentTheta.getId().toString()))
            .body(paymentTheta);
    }

    /**
     * {@code PATCH  /payment-thetas/:id} : Partial updates given fields of an existing paymentTheta, field will ignore if it is null
     *
     * @param id the id of the paymentTheta to save.
     * @param paymentTheta the paymentTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentTheta,
     * or with status {@code 400 (Bad Request)} if the paymentTheta is not valid,
     * or with status {@code 404 (Not Found)} if the paymentTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentTheta> partialUpdatePaymentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentTheta paymentTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentTheta partially : {}, {}", id, paymentTheta);
        if (paymentTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentTheta> result = paymentThetaService.partialUpdate(paymentTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-thetas} : get all the paymentThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentTheta>> getAllPaymentThetas(
        PaymentThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentThetas by criteria: {}", criteria);

        Page<PaymentTheta> page = paymentThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-thetas/count} : count all the paymentThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentThetas(PaymentThetaCriteria criteria) {
        LOG.debug("REST request to count PaymentThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-thetas/:id} : get the "id" paymentTheta.
     *
     * @param id the id of the paymentTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentTheta> getPaymentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentTheta : {}", id);
        Optional<PaymentTheta> paymentTheta = paymentThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentTheta);
    }

    /**
     * {@code DELETE  /payment-thetas/:id} : delete the "id" paymentTheta.
     *
     * @param id the id of the paymentTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentTheta : {}", id);
        paymentThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

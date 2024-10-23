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
import xyz.jhmapstruct.repository.PaymentViRepository;
import xyz.jhmapstruct.service.PaymentViQueryService;
import xyz.jhmapstruct.service.PaymentViService;
import xyz.jhmapstruct.service.criteria.PaymentViCriteria;
import xyz.jhmapstruct.service.dto.PaymentViDTO;
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
     * @param paymentViDTO the paymentViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentViDTO, or with status {@code 400 (Bad Request)} if the paymentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentViDTO> createPaymentVi(@Valid @RequestBody PaymentViDTO paymentViDTO) throws URISyntaxException {
        LOG.debug("REST request to save PaymentVi : {}", paymentViDTO);
        if (paymentViDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentViDTO = paymentViService.save(paymentViDTO);
        return ResponseEntity.created(new URI("/api/payment-vis/" + paymentViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentViDTO.getId().toString()))
            .body(paymentViDTO);
    }

    /**
     * {@code PUT  /payment-vis/:id} : Updates an existing paymentVi.
     *
     * @param id the id of the paymentViDTO to save.
     * @param paymentViDTO the paymentViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentViDTO,
     * or with status {@code 400 (Bad Request)} if the paymentViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentViDTO> updatePaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentViDTO paymentViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentVi : {}, {}", id, paymentViDTO);
        if (paymentViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentViDTO = paymentViService.update(paymentViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentViDTO.getId().toString()))
            .body(paymentViDTO);
    }

    /**
     * {@code PATCH  /payment-vis/:id} : Partial updates given fields of an existing paymentVi, field will ignore if it is null
     *
     * @param id the id of the paymentViDTO to save.
     * @param paymentViDTO the paymentViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentViDTO,
     * or with status {@code 400 (Bad Request)} if the paymentViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentViDTO> partialUpdatePaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentViDTO paymentViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentVi partially : {}, {}", id, paymentViDTO);
        if (paymentViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentViDTO> result = paymentViService.partialUpdate(paymentViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentViDTO.getId().toString())
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
    public ResponseEntity<List<PaymentViDTO>> getAllPaymentVis(
        PaymentViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentVis by criteria: {}", criteria);

        Page<PaymentViDTO> page = paymentViQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the paymentViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentViDTO> getPaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentVi : {}", id);
        Optional<PaymentViDTO> paymentViDTO = paymentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentViDTO);
    }

    /**
     * {@code DELETE  /payment-vis/:id} : delete the "id" paymentVi.
     *
     * @param id the id of the paymentViDTO to delete.
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

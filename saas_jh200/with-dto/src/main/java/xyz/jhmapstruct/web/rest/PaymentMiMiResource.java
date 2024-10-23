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
import xyz.jhmapstruct.repository.PaymentMiMiRepository;
import xyz.jhmapstruct.service.PaymentMiMiQueryService;
import xyz.jhmapstruct.service.PaymentMiMiService;
import xyz.jhmapstruct.service.criteria.PaymentMiMiCriteria;
import xyz.jhmapstruct.service.dto.PaymentMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.PaymentMiMi}.
 */
@RestController
@RequestMapping("/api/payment-mi-mis")
public class PaymentMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentMiMiResource.class);

    private static final String ENTITY_NAME = "paymentMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentMiMiService paymentMiMiService;

    private final PaymentMiMiRepository paymentMiMiRepository;

    private final PaymentMiMiQueryService paymentMiMiQueryService;

    public PaymentMiMiResource(
        PaymentMiMiService paymentMiMiService,
        PaymentMiMiRepository paymentMiMiRepository,
        PaymentMiMiQueryService paymentMiMiQueryService
    ) {
        this.paymentMiMiService = paymentMiMiService;
        this.paymentMiMiRepository = paymentMiMiRepository;
        this.paymentMiMiQueryService = paymentMiMiQueryService;
    }

    /**
     * {@code POST  /payment-mi-mis} : Create a new paymentMiMi.
     *
     * @param paymentMiMiDTO the paymentMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMiMiDTO, or with status {@code 400 (Bad Request)} if the paymentMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentMiMiDTO> createPaymentMiMi(@Valid @RequestBody PaymentMiMiDTO paymentMiMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save PaymentMiMi : {}", paymentMiMiDTO);
        if (paymentMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentMiMiDTO = paymentMiMiService.save(paymentMiMiDTO);
        return ResponseEntity.created(new URI("/api/payment-mi-mis/" + paymentMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentMiMiDTO.getId().toString()))
            .body(paymentMiMiDTO);
    }

    /**
     * {@code PUT  /payment-mi-mis/:id} : Updates an existing paymentMiMi.
     *
     * @param id the id of the paymentMiMiDTO to save.
     * @param paymentMiMiDTO the paymentMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMiMiDTO> updatePaymentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentMiMiDTO paymentMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentMiMi : {}, {}", id, paymentMiMiDTO);
        if (paymentMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentMiMiDTO = paymentMiMiService.update(paymentMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMiMiDTO.getId().toString()))
            .body(paymentMiMiDTO);
    }

    /**
     * {@code PATCH  /payment-mi-mis/:id} : Partial updates given fields of an existing paymentMiMi, field will ignore if it is null
     *
     * @param id the id of the paymentMiMiDTO to save.
     * @param paymentMiMiDTO the paymentMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentMiMiDTO> partialUpdatePaymentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentMiMiDTO paymentMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentMiMi partially : {}, {}", id, paymentMiMiDTO);
        if (paymentMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMiMiDTO> result = paymentMiMiService.partialUpdate(paymentMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-mi-mis} : get all the paymentMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PaymentMiMiDTO>> getAllPaymentMiMis(
        PaymentMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PaymentMiMis by criteria: {}", criteria);

        Page<PaymentMiMiDTO> page = paymentMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-mi-mis/count} : count all the paymentMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPaymentMiMis(PaymentMiMiCriteria criteria) {
        LOG.debug("REST request to count PaymentMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-mi-mis/:id} : get the "id" paymentMiMi.
     *
     * @param id the id of the paymentMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMiMiDTO> getPaymentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentMiMi : {}", id);
        Optional<PaymentMiMiDTO> paymentMiMiDTO = paymentMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMiMiDTO);
    }

    /**
     * {@code DELETE  /payment-mi-mis/:id} : delete the "id" paymentMiMi.
     *
     * @param id the id of the paymentMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PaymentMiMi : {}", id);
        paymentMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

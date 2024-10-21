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
import xyz.jhmapstruct.repository.PaymentMiRepository;
import xyz.jhmapstruct.service.PaymentMiService;
import xyz.jhmapstruct.service.dto.PaymentMiDTO;
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
     * @param paymentMiDTO the paymentMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentMiDTO, or with status {@code 400 (Bad Request)} if the paymentMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PaymentMiDTO> createPaymentMi(@Valid @RequestBody PaymentMiDTO paymentMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save PaymentMi : {}", paymentMiDTO);
        if (paymentMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        paymentMiDTO = paymentMiService.save(paymentMiDTO);
        return ResponseEntity.created(new URI("/api/payment-mis/" + paymentMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, paymentMiDTO.getId().toString()))
            .body(paymentMiDTO);
    }

    /**
     * {@code PUT  /payment-mis/:id} : Updates an existing paymentMi.
     *
     * @param id the id of the paymentMiDTO to save.
     * @param paymentMiDTO the paymentMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMiDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMiDTO> updatePaymentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentMiDTO paymentMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PaymentMi : {}, {}", id, paymentMiDTO);
        if (paymentMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        paymentMiDTO = paymentMiService.update(paymentMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMiDTO.getId().toString()))
            .body(paymentMiDTO);
    }

    /**
     * {@code PATCH  /payment-mis/:id} : Partial updates given fields of an existing paymentMi, field will ignore if it is null
     *
     * @param id the id of the paymentMiDTO to save.
     * @param paymentMiDTO the paymentMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentMiDTO,
     * or with status {@code 400 (Bad Request)} if the paymentMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentMiDTO> partialUpdatePaymentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentMiDTO paymentMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PaymentMi partially : {}, {}", id, paymentMiDTO);
        if (paymentMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentMiDTO> result = paymentMiService.partialUpdate(paymentMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, paymentMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-mis} : get all the paymentMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentMis in body.
     */
    @GetMapping("")
    public List<PaymentMiDTO> getAllPaymentMis() {
        LOG.debug("REST request to get all PaymentMis");
        return paymentMiService.findAll();
    }

    /**
     * {@code GET  /payment-mis/:id} : get the "id" paymentMi.
     *
     * @param id the id of the paymentMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMiDTO> getPaymentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PaymentMi : {}", id);
        Optional<PaymentMiDTO> paymentMiDTO = paymentMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentMiDTO);
    }

    /**
     * {@code DELETE  /payment-mis/:id} : delete the "id" paymentMi.
     *
     * @param id the id of the paymentMiDTO to delete.
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

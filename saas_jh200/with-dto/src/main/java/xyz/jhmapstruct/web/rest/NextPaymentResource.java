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
import xyz.jhmapstruct.repository.NextPaymentRepository;
import xyz.jhmapstruct.service.NextPaymentQueryService;
import xyz.jhmapstruct.service.NextPaymentService;
import xyz.jhmapstruct.service.criteria.NextPaymentCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPayment}.
 */
@RestController
@RequestMapping("/api/next-payments")
public class NextPaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentResource.class);

    private static final String ENTITY_NAME = "nextPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentService nextPaymentService;

    private final NextPaymentRepository nextPaymentRepository;

    private final NextPaymentQueryService nextPaymentQueryService;

    public NextPaymentResource(
        NextPaymentService nextPaymentService,
        NextPaymentRepository nextPaymentRepository,
        NextPaymentQueryService nextPaymentQueryService
    ) {
        this.nextPaymentService = nextPaymentService;
        this.nextPaymentRepository = nextPaymentRepository;
        this.nextPaymentQueryService = nextPaymentQueryService;
    }

    /**
     * {@code POST  /next-payments} : Create a new nextPayment.
     *
     * @param nextPaymentDTO the nextPaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentDTO, or with status {@code 400 (Bad Request)} if the nextPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentDTO> createNextPayment(@Valid @RequestBody NextPaymentDTO nextPaymentDTO) throws URISyntaxException {
        LOG.debug("REST request to save NextPayment : {}", nextPaymentDTO);
        if (nextPaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentDTO = nextPaymentService.save(nextPaymentDTO);
        return ResponseEntity.created(new URI("/api/next-payments/" + nextPaymentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentDTO.getId().toString()))
            .body(nextPaymentDTO);
    }

    /**
     * {@code PUT  /next-payments/:id} : Updates an existing nextPayment.
     *
     * @param id the id of the nextPaymentDTO to save.
     * @param nextPaymentDTO the nextPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the nextPaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentDTO> updateNextPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentDTO nextPaymentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPayment : {}, {}", id, nextPaymentDTO);
        if (nextPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentDTO = nextPaymentService.update(nextPaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentDTO.getId().toString()))
            .body(nextPaymentDTO);
    }

    /**
     * {@code PATCH  /next-payments/:id} : Partial updates given fields of an existing nextPayment, field will ignore if it is null
     *
     * @param id the id of the nextPaymentDTO to save.
     * @param nextPaymentDTO the nextPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the nextPaymentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentDTO> partialUpdateNextPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentDTO nextPaymentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPayment partially : {}, {}", id, nextPaymentDTO);
        if (nextPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentDTO> result = nextPaymentService.partialUpdate(nextPaymentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payments} : get all the nextPayments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPayments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentDTO>> getAllNextPayments(
        NextPaymentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPayments by criteria: {}", criteria);

        Page<NextPaymentDTO> page = nextPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payments/count} : count all the nextPayments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPayments(NextPaymentCriteria criteria) {
        LOG.debug("REST request to count NextPayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payments/:id} : get the "id" nextPayment.
     *
     * @param id the id of the nextPaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentDTO> getNextPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPayment : {}", id);
        Optional<NextPaymentDTO> nextPaymentDTO = nextPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentDTO);
    }

    /**
     * {@code DELETE  /next-payments/:id} : delete the "id" nextPayment.
     *
     * @param id the id of the nextPaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPayment : {}", id);
        nextPaymentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

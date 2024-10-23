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
import xyz.jhmapstruct.repository.NextShipmentRepository;
import xyz.jhmapstruct.service.NextShipmentQueryService;
import xyz.jhmapstruct.service.NextShipmentService;
import xyz.jhmapstruct.service.criteria.NextShipmentCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipment}.
 */
@RestController
@RequestMapping("/api/next-shipments")
public class NextShipmentResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentResource.class);

    private static final String ENTITY_NAME = "nextShipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentService nextShipmentService;

    private final NextShipmentRepository nextShipmentRepository;

    private final NextShipmentQueryService nextShipmentQueryService;

    public NextShipmentResource(
        NextShipmentService nextShipmentService,
        NextShipmentRepository nextShipmentRepository,
        NextShipmentQueryService nextShipmentQueryService
    ) {
        this.nextShipmentService = nextShipmentService;
        this.nextShipmentRepository = nextShipmentRepository;
        this.nextShipmentQueryService = nextShipmentQueryService;
    }

    /**
     * {@code POST  /next-shipments} : Create a new nextShipment.
     *
     * @param nextShipmentDTO the nextShipmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentDTO, or with status {@code 400 (Bad Request)} if the nextShipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentDTO> createNextShipment(@Valid @RequestBody NextShipmentDTO nextShipmentDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipment : {}", nextShipmentDTO);
        if (nextShipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextShipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentDTO = nextShipmentService.save(nextShipmentDTO);
        return ResponseEntity.created(new URI("/api/next-shipments/" + nextShipmentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentDTO.getId().toString()))
            .body(nextShipmentDTO);
    }

    /**
     * {@code PUT  /next-shipments/:id} : Updates an existing nextShipment.
     *
     * @param id the id of the nextShipmentDTO to save.
     * @param nextShipmentDTO the nextShipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentDTO> updateNextShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentDTO nextShipmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipment : {}, {}", id, nextShipmentDTO);
        if (nextShipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentDTO = nextShipmentService.update(nextShipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentDTO.getId().toString()))
            .body(nextShipmentDTO);
    }

    /**
     * {@code PATCH  /next-shipments/:id} : Partial updates given fields of an existing nextShipment, field will ignore if it is null
     *
     * @param id the id of the nextShipmentDTO to save.
     * @param nextShipmentDTO the nextShipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentDTO> partialUpdateNextShipment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentDTO nextShipmentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipment partially : {}, {}", id, nextShipmentDTO);
        if (nextShipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentDTO> result = nextShipmentService.partialUpdate(nextShipmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipments} : get all the nextShipments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipments in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentDTO>> getAllNextShipments(
        NextShipmentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipments by criteria: {}", criteria);

        Page<NextShipmentDTO> page = nextShipmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipments/count} : count all the nextShipments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipments(NextShipmentCriteria criteria) {
        LOG.debug("REST request to count NextShipments by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipments/:id} : get the "id" nextShipment.
     *
     * @param id the id of the nextShipmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentDTO> getNextShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipment : {}", id);
        Optional<NextShipmentDTO> nextShipmentDTO = nextShipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentDTO);
    }

    /**
     * {@code DELETE  /next-shipments/:id} : delete the "id" nextShipment.
     *
     * @param id the id of the nextShipmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipment : {}", id);
        nextShipmentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

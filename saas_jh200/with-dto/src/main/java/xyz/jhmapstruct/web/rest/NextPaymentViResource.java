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
import xyz.jhmapstruct.repository.NextPaymentViRepository;
import xyz.jhmapstruct.service.NextPaymentViQueryService;
import xyz.jhmapstruct.service.NextPaymentViService;
import xyz.jhmapstruct.service.criteria.NextPaymentViCriteria;
import xyz.jhmapstruct.service.dto.NextPaymentViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentVi}.
 */
@RestController
@RequestMapping("/api/next-payment-vis")
public class NextPaymentViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViResource.class);

    private static final String ENTITY_NAME = "nextPaymentVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentViService nextPaymentViService;

    private final NextPaymentViRepository nextPaymentViRepository;

    private final NextPaymentViQueryService nextPaymentViQueryService;

    public NextPaymentViResource(
        NextPaymentViService nextPaymentViService,
        NextPaymentViRepository nextPaymentViRepository,
        NextPaymentViQueryService nextPaymentViQueryService
    ) {
        this.nextPaymentViService = nextPaymentViService;
        this.nextPaymentViRepository = nextPaymentViRepository;
        this.nextPaymentViQueryService = nextPaymentViQueryService;
    }

    /**
     * {@code POST  /next-payment-vis} : Create a new nextPaymentVi.
     *
     * @param nextPaymentViDTO the nextPaymentViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentViDTO, or with status {@code 400 (Bad Request)} if the nextPaymentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentViDTO> createNextPaymentVi(@Valid @RequestBody NextPaymentViDTO nextPaymentViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentVi : {}", nextPaymentViDTO);
        if (nextPaymentViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentViDTO = nextPaymentViService.save(nextPaymentViDTO);
        return ResponseEntity.created(new URI("/api/next-payment-vis/" + nextPaymentViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentViDTO.getId().toString()))
            .body(nextPaymentViDTO);
    }

    /**
     * {@code PUT  /next-payment-vis/:id} : Updates an existing nextPaymentVi.
     *
     * @param id the id of the nextPaymentViDTO to save.
     * @param nextPaymentViDTO the nextPaymentViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentViDTO,
     * or with status {@code 400 (Bad Request)} if the nextPaymentViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentViDTO> updateNextPaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentViDTO nextPaymentViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentVi : {}, {}", id, nextPaymentViDTO);
        if (nextPaymentViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentViDTO = nextPaymentViService.update(nextPaymentViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentViDTO.getId().toString()))
            .body(nextPaymentViDTO);
    }

    /**
     * {@code PATCH  /next-payment-vis/:id} : Partial updates given fields of an existing nextPaymentVi, field will ignore if it is null
     *
     * @param id the id of the nextPaymentViDTO to save.
     * @param nextPaymentViDTO the nextPaymentViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentViDTO,
     * or with status {@code 400 (Bad Request)} if the nextPaymentViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentViDTO> partialUpdateNextPaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentViDTO nextPaymentViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentVi partially : {}, {}", id, nextPaymentViDTO);
        if (nextPaymentViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentViDTO> result = nextPaymentViService.partialUpdate(nextPaymentViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-vis} : get all the nextPaymentVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentViDTO>> getAllNextPaymentVis(
        NextPaymentViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentVis by criteria: {}", criteria);

        Page<NextPaymentViDTO> page = nextPaymentViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-vis/count} : count all the nextPaymentVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentVis(NextPaymentViCriteria criteria) {
        LOG.debug("REST request to count NextPaymentVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-vis/:id} : get the "id" nextPaymentVi.
     *
     * @param id the id of the nextPaymentViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentViDTO> getNextPaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentVi : {}", id);
        Optional<NextPaymentViDTO> nextPaymentViDTO = nextPaymentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentViDTO);
    }

    /**
     * {@code DELETE  /next-payment-vis/:id} : delete the "id" nextPaymentVi.
     *
     * @param id the id of the nextPaymentViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentVi : {}", id);
        nextPaymentViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

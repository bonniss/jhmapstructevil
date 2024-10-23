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
import xyz.jhmapstruct.repository.NextCustomerViViRepository;
import xyz.jhmapstruct.service.NextCustomerViViQueryService;
import xyz.jhmapstruct.service.NextCustomerViViService;
import xyz.jhmapstruct.service.criteria.NextCustomerViViCriteria;
import xyz.jhmapstruct.service.dto.NextCustomerViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerViVi}.
 */
@RestController
@RequestMapping("/api/next-customer-vi-vis")
public class NextCustomerViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViViResource.class);

    private static final String ENTITY_NAME = "nextCustomerViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerViViService nextCustomerViViService;

    private final NextCustomerViViRepository nextCustomerViViRepository;

    private final NextCustomerViViQueryService nextCustomerViViQueryService;

    public NextCustomerViViResource(
        NextCustomerViViService nextCustomerViViService,
        NextCustomerViViRepository nextCustomerViViRepository,
        NextCustomerViViQueryService nextCustomerViViQueryService
    ) {
        this.nextCustomerViViService = nextCustomerViViService;
        this.nextCustomerViViRepository = nextCustomerViViRepository;
        this.nextCustomerViViQueryService = nextCustomerViViQueryService;
    }

    /**
     * {@code POST  /next-customer-vi-vis} : Create a new nextCustomerViVi.
     *
     * @param nextCustomerViViDTO the nextCustomerViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerViViDTO, or with status {@code 400 (Bad Request)} if the nextCustomerViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerViViDTO> createNextCustomerViVi(@Valid @RequestBody NextCustomerViViDTO nextCustomerViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerViVi : {}", nextCustomerViViDTO);
        if (nextCustomerViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerViViDTO = nextCustomerViViService.save(nextCustomerViViDTO);
        return ResponseEntity.created(new URI("/api/next-customer-vi-vis/" + nextCustomerViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerViViDTO.getId().toString()))
            .body(nextCustomerViViDTO);
    }

    /**
     * {@code PUT  /next-customer-vi-vis/:id} : Updates an existing nextCustomerViVi.
     *
     * @param id the id of the nextCustomerViViDTO to save.
     * @param nextCustomerViViDTO the nextCustomerViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerViViDTO> updateNextCustomerViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerViViDTO nextCustomerViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerViVi : {}, {}", id, nextCustomerViViDTO);
        if (nextCustomerViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerViViDTO = nextCustomerViViService.update(nextCustomerViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerViViDTO.getId().toString()))
            .body(nextCustomerViViDTO);
    }

    /**
     * {@code PATCH  /next-customer-vi-vis/:id} : Partial updates given fields of an existing nextCustomerViVi, field will ignore if it is null
     *
     * @param id the id of the nextCustomerViViDTO to save.
     * @param nextCustomerViViDTO the nextCustomerViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextCustomerViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerViViDTO> partialUpdateNextCustomerViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerViViDTO nextCustomerViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerViVi partially : {}, {}", id, nextCustomerViViDTO);
        if (nextCustomerViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerViViDTO> result = nextCustomerViViService.partialUpdate(nextCustomerViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-vi-vis} : get all the nextCustomerViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerViViDTO>> getAllNextCustomerViVis(
        NextCustomerViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerViVis by criteria: {}", criteria);

        Page<NextCustomerViViDTO> page = nextCustomerViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-vi-vis/count} : count all the nextCustomerViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerViVis(NextCustomerViViCriteria criteria) {
        LOG.debug("REST request to count NextCustomerViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-vi-vis/:id} : get the "id" nextCustomerViVi.
     *
     * @param id the id of the nextCustomerViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerViViDTO> getNextCustomerViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerViVi : {}", id);
        Optional<NextCustomerViViDTO> nextCustomerViViDTO = nextCustomerViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerViViDTO);
    }

    /**
     * {@code DELETE  /next-customer-vi-vis/:id} : delete the "id" nextCustomerViVi.
     *
     * @param id the id of the nextCustomerViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerViVi : {}", id);
        nextCustomerViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

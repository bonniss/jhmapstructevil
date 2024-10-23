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
import xyz.jhmapstruct.repository.NextEmployeeViViRepository;
import xyz.jhmapstruct.service.NextEmployeeViViQueryService;
import xyz.jhmapstruct.service.NextEmployeeViViService;
import xyz.jhmapstruct.service.criteria.NextEmployeeViViCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeViVi}.
 */
@RestController
@RequestMapping("/api/next-employee-vi-vis")
public class NextEmployeeViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViViResource.class);

    private static final String ENTITY_NAME = "nextEmployeeViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeViViService nextEmployeeViViService;

    private final NextEmployeeViViRepository nextEmployeeViViRepository;

    private final NextEmployeeViViQueryService nextEmployeeViViQueryService;

    public NextEmployeeViViResource(
        NextEmployeeViViService nextEmployeeViViService,
        NextEmployeeViViRepository nextEmployeeViViRepository,
        NextEmployeeViViQueryService nextEmployeeViViQueryService
    ) {
        this.nextEmployeeViViService = nextEmployeeViViService;
        this.nextEmployeeViViRepository = nextEmployeeViViRepository;
        this.nextEmployeeViViQueryService = nextEmployeeViViQueryService;
    }

    /**
     * {@code POST  /next-employee-vi-vis} : Create a new nextEmployeeViVi.
     *
     * @param nextEmployeeViViDTO the nextEmployeeViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeViViDTO, or with status {@code 400 (Bad Request)} if the nextEmployeeViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeViViDTO> createNextEmployeeViVi(@Valid @RequestBody NextEmployeeViViDTO nextEmployeeViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeViVi : {}", nextEmployeeViViDTO);
        if (nextEmployeeViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeViViDTO = nextEmployeeViViService.save(nextEmployeeViViDTO);
        return ResponseEntity.created(new URI("/api/next-employee-vi-vis/" + nextEmployeeViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeViViDTO.getId().toString()))
            .body(nextEmployeeViViDTO);
    }

    /**
     * {@code PUT  /next-employee-vi-vis/:id} : Updates an existing nextEmployeeViVi.
     *
     * @param id the id of the nextEmployeeViViDTO to save.
     * @param nextEmployeeViViDTO the nextEmployeeViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeViViDTO> updateNextEmployeeViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeViViDTO nextEmployeeViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeViVi : {}, {}", id, nextEmployeeViViDTO);
        if (nextEmployeeViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeViViDTO = nextEmployeeViViService.update(nextEmployeeViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeViViDTO.getId().toString()))
            .body(nextEmployeeViViDTO);
    }

    /**
     * {@code PATCH  /next-employee-vi-vis/:id} : Partial updates given fields of an existing nextEmployeeViVi, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeViViDTO to save.
     * @param nextEmployeeViViDTO the nextEmployeeViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeViViDTO> partialUpdateNextEmployeeViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeViViDTO nextEmployeeViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeViVi partially : {}, {}", id, nextEmployeeViViDTO);
        if (nextEmployeeViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeViViDTO> result = nextEmployeeViViService.partialUpdate(nextEmployeeViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-vi-vis} : get all the nextEmployeeViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeViViDTO>> getAllNextEmployeeViVis(
        NextEmployeeViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeViVis by criteria: {}", criteria);

        Page<NextEmployeeViViDTO> page = nextEmployeeViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-vi-vis/count} : count all the nextEmployeeViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeViVis(NextEmployeeViViCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-vi-vis/:id} : get the "id" nextEmployeeViVi.
     *
     * @param id the id of the nextEmployeeViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeViViDTO> getNextEmployeeViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeViVi : {}", id);
        Optional<NextEmployeeViViDTO> nextEmployeeViViDTO = nextEmployeeViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeViViDTO);
    }

    /**
     * {@code DELETE  /next-employee-vi-vis/:id} : delete the "id" nextEmployeeViVi.
     *
     * @param id the id of the nextEmployeeViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeViVi : {}", id);
        nextEmployeeViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

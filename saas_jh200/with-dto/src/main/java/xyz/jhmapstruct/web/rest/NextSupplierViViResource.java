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
import xyz.jhmapstruct.repository.NextSupplierViViRepository;
import xyz.jhmapstruct.service.NextSupplierViViQueryService;
import xyz.jhmapstruct.service.NextSupplierViViService;
import xyz.jhmapstruct.service.criteria.NextSupplierViViCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierViVi}.
 */
@RestController
@RequestMapping("/api/next-supplier-vi-vis")
public class NextSupplierViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViViResource.class);

    private static final String ENTITY_NAME = "nextSupplierViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierViViService nextSupplierViViService;

    private final NextSupplierViViRepository nextSupplierViViRepository;

    private final NextSupplierViViQueryService nextSupplierViViQueryService;

    public NextSupplierViViResource(
        NextSupplierViViService nextSupplierViViService,
        NextSupplierViViRepository nextSupplierViViRepository,
        NextSupplierViViQueryService nextSupplierViViQueryService
    ) {
        this.nextSupplierViViService = nextSupplierViViService;
        this.nextSupplierViViRepository = nextSupplierViViRepository;
        this.nextSupplierViViQueryService = nextSupplierViViQueryService;
    }

    /**
     * {@code POST  /next-supplier-vi-vis} : Create a new nextSupplierViVi.
     *
     * @param nextSupplierViViDTO the nextSupplierViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierViViDTO, or with status {@code 400 (Bad Request)} if the nextSupplierViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierViViDTO> createNextSupplierViVi(@Valid @RequestBody NextSupplierViViDTO nextSupplierViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierViVi : {}", nextSupplierViViDTO);
        if (nextSupplierViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierViViDTO = nextSupplierViViService.save(nextSupplierViViDTO);
        return ResponseEntity.created(new URI("/api/next-supplier-vi-vis/" + nextSupplierViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierViViDTO.getId().toString()))
            .body(nextSupplierViViDTO);
    }

    /**
     * {@code PUT  /next-supplier-vi-vis/:id} : Updates an existing nextSupplierViVi.
     *
     * @param id the id of the nextSupplierViViDTO to save.
     * @param nextSupplierViViDTO the nextSupplierViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierViViDTO> updateNextSupplierViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierViViDTO nextSupplierViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierViVi : {}, {}", id, nextSupplierViViDTO);
        if (nextSupplierViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierViViDTO = nextSupplierViViService.update(nextSupplierViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierViViDTO.getId().toString()))
            .body(nextSupplierViViDTO);
    }

    /**
     * {@code PATCH  /next-supplier-vi-vis/:id} : Partial updates given fields of an existing nextSupplierViVi, field will ignore if it is null
     *
     * @param id the id of the nextSupplierViViDTO to save.
     * @param nextSupplierViViDTO the nextSupplierViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierViViDTO> partialUpdateNextSupplierViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierViViDTO nextSupplierViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierViVi partially : {}, {}", id, nextSupplierViViDTO);
        if (nextSupplierViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierViViDTO> result = nextSupplierViViService.partialUpdate(nextSupplierViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-vi-vis} : get all the nextSupplierViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierViViDTO>> getAllNextSupplierViVis(
        NextSupplierViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierViVis by criteria: {}", criteria);

        Page<NextSupplierViViDTO> page = nextSupplierViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-vi-vis/count} : count all the nextSupplierViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierViVis(NextSupplierViViCriteria criteria) {
        LOG.debug("REST request to count NextSupplierViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-vi-vis/:id} : get the "id" nextSupplierViVi.
     *
     * @param id the id of the nextSupplierViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierViViDTO> getNextSupplierViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierViVi : {}", id);
        Optional<NextSupplierViViDTO> nextSupplierViViDTO = nextSupplierViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierViViDTO);
    }

    /**
     * {@code DELETE  /next-supplier-vi-vis/:id} : delete the "id" nextSupplierViVi.
     *
     * @param id the id of the nextSupplierViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierViVi : {}", id);
        nextSupplierViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

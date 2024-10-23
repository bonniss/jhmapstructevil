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
import xyz.jhmapstruct.repository.NextSupplierViRepository;
import xyz.jhmapstruct.service.NextSupplierViQueryService;
import xyz.jhmapstruct.service.NextSupplierViService;
import xyz.jhmapstruct.service.criteria.NextSupplierViCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierVi}.
 */
@RestController
@RequestMapping("/api/next-supplier-vis")
public class NextSupplierViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierViResource.class);

    private static final String ENTITY_NAME = "nextSupplierVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierViService nextSupplierViService;

    private final NextSupplierViRepository nextSupplierViRepository;

    private final NextSupplierViQueryService nextSupplierViQueryService;

    public NextSupplierViResource(
        NextSupplierViService nextSupplierViService,
        NextSupplierViRepository nextSupplierViRepository,
        NextSupplierViQueryService nextSupplierViQueryService
    ) {
        this.nextSupplierViService = nextSupplierViService;
        this.nextSupplierViRepository = nextSupplierViRepository;
        this.nextSupplierViQueryService = nextSupplierViQueryService;
    }

    /**
     * {@code POST  /next-supplier-vis} : Create a new nextSupplierVi.
     *
     * @param nextSupplierViDTO the nextSupplierViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierViDTO, or with status {@code 400 (Bad Request)} if the nextSupplierVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierViDTO> createNextSupplierVi(@Valid @RequestBody NextSupplierViDTO nextSupplierViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierVi : {}", nextSupplierViDTO);
        if (nextSupplierViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierViDTO = nextSupplierViService.save(nextSupplierViDTO);
        return ResponseEntity.created(new URI("/api/next-supplier-vis/" + nextSupplierViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierViDTO.getId().toString()))
            .body(nextSupplierViDTO);
    }

    /**
     * {@code PUT  /next-supplier-vis/:id} : Updates an existing nextSupplierVi.
     *
     * @param id the id of the nextSupplierViDTO to save.
     * @param nextSupplierViDTO the nextSupplierViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierViDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierViDTO> updateNextSupplierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierViDTO nextSupplierViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierVi : {}, {}", id, nextSupplierViDTO);
        if (nextSupplierViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierViDTO = nextSupplierViService.update(nextSupplierViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierViDTO.getId().toString()))
            .body(nextSupplierViDTO);
    }

    /**
     * {@code PATCH  /next-supplier-vis/:id} : Partial updates given fields of an existing nextSupplierVi, field will ignore if it is null
     *
     * @param id the id of the nextSupplierViDTO to save.
     * @param nextSupplierViDTO the nextSupplierViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierViDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierViDTO> partialUpdateNextSupplierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierViDTO nextSupplierViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierVi partially : {}, {}", id, nextSupplierViDTO);
        if (nextSupplierViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierViDTO> result = nextSupplierViService.partialUpdate(nextSupplierViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-vis} : get all the nextSupplierVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierViDTO>> getAllNextSupplierVis(
        NextSupplierViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierVis by criteria: {}", criteria);

        Page<NextSupplierViDTO> page = nextSupplierViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-vis/count} : count all the nextSupplierVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierVis(NextSupplierViCriteria criteria) {
        LOG.debug("REST request to count NextSupplierVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-vis/:id} : get the "id" nextSupplierVi.
     *
     * @param id the id of the nextSupplierViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierViDTO> getNextSupplierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierVi : {}", id);
        Optional<NextSupplierViDTO> nextSupplierViDTO = nextSupplierViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierViDTO);
    }

    /**
     * {@code DELETE  /next-supplier-vis/:id} : delete the "id" nextSupplierVi.
     *
     * @param id the id of the nextSupplierViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierVi : {}", id);
        nextSupplierViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

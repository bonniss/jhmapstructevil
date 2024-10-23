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
import xyz.jhmapstruct.repository.NextProductViRepository;
import xyz.jhmapstruct.service.NextProductViQueryService;
import xyz.jhmapstruct.service.NextProductViService;
import xyz.jhmapstruct.service.criteria.NextProductViCriteria;
import xyz.jhmapstruct.service.dto.NextProductViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductVi}.
 */
@RestController
@RequestMapping("/api/next-product-vis")
public class NextProductViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViResource.class);

    private static final String ENTITY_NAME = "nextProductVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductViService nextProductViService;

    private final NextProductViRepository nextProductViRepository;

    private final NextProductViQueryService nextProductViQueryService;

    public NextProductViResource(
        NextProductViService nextProductViService,
        NextProductViRepository nextProductViRepository,
        NextProductViQueryService nextProductViQueryService
    ) {
        this.nextProductViService = nextProductViService;
        this.nextProductViRepository = nextProductViRepository;
        this.nextProductViQueryService = nextProductViQueryService;
    }

    /**
     * {@code POST  /next-product-vis} : Create a new nextProductVi.
     *
     * @param nextProductViDTO the nextProductViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductViDTO, or with status {@code 400 (Bad Request)} if the nextProductVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductViDTO> createNextProductVi(@Valid @RequestBody NextProductViDTO nextProductViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductVi : {}", nextProductViDTO);
        if (nextProductViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductViDTO = nextProductViService.save(nextProductViDTO);
        return ResponseEntity.created(new URI("/api/next-product-vis/" + nextProductViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductViDTO.getId().toString()))
            .body(nextProductViDTO);
    }

    /**
     * {@code PUT  /next-product-vis/:id} : Updates an existing nextProductVi.
     *
     * @param id the id of the nextProductViDTO to save.
     * @param nextProductViDTO the nextProductViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductViDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductViDTO> updateNextProductVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductViDTO nextProductViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductVi : {}, {}", id, nextProductViDTO);
        if (nextProductViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductViDTO = nextProductViService.update(nextProductViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductViDTO.getId().toString()))
            .body(nextProductViDTO);
    }

    /**
     * {@code PATCH  /next-product-vis/:id} : Partial updates given fields of an existing nextProductVi, field will ignore if it is null
     *
     * @param id the id of the nextProductViDTO to save.
     * @param nextProductViDTO the nextProductViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductViDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductViDTO> partialUpdateNextProductVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductViDTO nextProductViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductVi partially : {}, {}", id, nextProductViDTO);
        if (nextProductViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductViDTO> result = nextProductViService.partialUpdate(nextProductViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-vis} : get all the nextProductVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductViDTO>> getAllNextProductVis(
        NextProductViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductVis by criteria: {}", criteria);

        Page<NextProductViDTO> page = nextProductViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-vis/count} : count all the nextProductVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductVis(NextProductViCriteria criteria) {
        LOG.debug("REST request to count NextProductVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-vis/:id} : get the "id" nextProductVi.
     *
     * @param id the id of the nextProductViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductViDTO> getNextProductVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductVi : {}", id);
        Optional<NextProductViDTO> nextProductViDTO = nextProductViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductViDTO);
    }

    /**
     * {@code DELETE  /next-product-vis/:id} : delete the "id" nextProductVi.
     *
     * @param id the id of the nextProductViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductVi : {}", id);
        nextProductViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

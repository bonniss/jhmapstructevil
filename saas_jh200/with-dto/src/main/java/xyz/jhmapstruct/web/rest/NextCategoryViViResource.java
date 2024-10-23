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
import xyz.jhmapstruct.repository.NextCategoryViViRepository;
import xyz.jhmapstruct.service.NextCategoryViViQueryService;
import xyz.jhmapstruct.service.NextCategoryViViService;
import xyz.jhmapstruct.service.criteria.NextCategoryViViCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryViVi}.
 */
@RestController
@RequestMapping("/api/next-category-vi-vis")
public class NextCategoryViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViViResource.class);

    private static final String ENTITY_NAME = "nextCategoryViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryViViService nextCategoryViViService;

    private final NextCategoryViViRepository nextCategoryViViRepository;

    private final NextCategoryViViQueryService nextCategoryViViQueryService;

    public NextCategoryViViResource(
        NextCategoryViViService nextCategoryViViService,
        NextCategoryViViRepository nextCategoryViViRepository,
        NextCategoryViViQueryService nextCategoryViViQueryService
    ) {
        this.nextCategoryViViService = nextCategoryViViService;
        this.nextCategoryViViRepository = nextCategoryViViRepository;
        this.nextCategoryViViQueryService = nextCategoryViViQueryService;
    }

    /**
     * {@code POST  /next-category-vi-vis} : Create a new nextCategoryViVi.
     *
     * @param nextCategoryViViDTO the nextCategoryViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryViViDTO, or with status {@code 400 (Bad Request)} if the nextCategoryViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryViViDTO> createNextCategoryViVi(@Valid @RequestBody NextCategoryViViDTO nextCategoryViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryViVi : {}", nextCategoryViViDTO);
        if (nextCategoryViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryViViDTO = nextCategoryViViService.save(nextCategoryViViDTO);
        return ResponseEntity.created(new URI("/api/next-category-vi-vis/" + nextCategoryViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryViViDTO.getId().toString()))
            .body(nextCategoryViViDTO);
    }

    /**
     * {@code PUT  /next-category-vi-vis/:id} : Updates an existing nextCategoryViVi.
     *
     * @param id the id of the nextCategoryViViDTO to save.
     * @param nextCategoryViViDTO the nextCategoryViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryViViDTO> updateNextCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryViViDTO nextCategoryViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryViVi : {}, {}", id, nextCategoryViViDTO);
        if (nextCategoryViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryViViDTO = nextCategoryViViService.update(nextCategoryViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryViViDTO.getId().toString()))
            .body(nextCategoryViViDTO);
    }

    /**
     * {@code PATCH  /next-category-vi-vis/:id} : Partial updates given fields of an existing nextCategoryViVi, field will ignore if it is null
     *
     * @param id the id of the nextCategoryViViDTO to save.
     * @param nextCategoryViViDTO the nextCategoryViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryViViDTO> partialUpdateNextCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryViViDTO nextCategoryViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryViVi partially : {}, {}", id, nextCategoryViViDTO);
        if (nextCategoryViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryViViDTO> result = nextCategoryViViService.partialUpdate(nextCategoryViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-vi-vis} : get all the nextCategoryViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryViViDTO>> getAllNextCategoryViVis(
        NextCategoryViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryViVis by criteria: {}", criteria);

        Page<NextCategoryViViDTO> page = nextCategoryViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-vi-vis/count} : count all the nextCategoryViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryViVis(NextCategoryViViCriteria criteria) {
        LOG.debug("REST request to count NextCategoryViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-vi-vis/:id} : get the "id" nextCategoryViVi.
     *
     * @param id the id of the nextCategoryViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryViViDTO> getNextCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryViVi : {}", id);
        Optional<NextCategoryViViDTO> nextCategoryViViDTO = nextCategoryViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryViViDTO);
    }

    /**
     * {@code DELETE  /next-category-vi-vis/:id} : delete the "id" nextCategoryViVi.
     *
     * @param id the id of the nextCategoryViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryViVi : {}", id);
        nextCategoryViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

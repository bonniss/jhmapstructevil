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
import xyz.jhmapstruct.repository.NextReviewViViRepository;
import xyz.jhmapstruct.service.NextReviewViViQueryService;
import xyz.jhmapstruct.service.NextReviewViViService;
import xyz.jhmapstruct.service.criteria.NextReviewViViCriteria;
import xyz.jhmapstruct.service.dto.NextReviewViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewViVi}.
 */
@RestController
@RequestMapping("/api/next-review-vi-vis")
public class NextReviewViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViViResource.class);

    private static final String ENTITY_NAME = "nextReviewViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewViViService nextReviewViViService;

    private final NextReviewViViRepository nextReviewViViRepository;

    private final NextReviewViViQueryService nextReviewViViQueryService;

    public NextReviewViViResource(
        NextReviewViViService nextReviewViViService,
        NextReviewViViRepository nextReviewViViRepository,
        NextReviewViViQueryService nextReviewViViQueryService
    ) {
        this.nextReviewViViService = nextReviewViViService;
        this.nextReviewViViRepository = nextReviewViViRepository;
        this.nextReviewViViQueryService = nextReviewViViQueryService;
    }

    /**
     * {@code POST  /next-review-vi-vis} : Create a new nextReviewViVi.
     *
     * @param nextReviewViViDTO the nextReviewViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewViViDTO, or with status {@code 400 (Bad Request)} if the nextReviewViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewViViDTO> createNextReviewViVi(@Valid @RequestBody NextReviewViViDTO nextReviewViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewViVi : {}", nextReviewViViDTO);
        if (nextReviewViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewViViDTO = nextReviewViViService.save(nextReviewViViDTO);
        return ResponseEntity.created(new URI("/api/next-review-vi-vis/" + nextReviewViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewViViDTO.getId().toString()))
            .body(nextReviewViViDTO);
    }

    /**
     * {@code PUT  /next-review-vi-vis/:id} : Updates an existing nextReviewViVi.
     *
     * @param id the id of the nextReviewViViDTO to save.
     * @param nextReviewViViDTO the nextReviewViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewViViDTO> updateNextReviewViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewViViDTO nextReviewViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewViVi : {}, {}", id, nextReviewViViDTO);
        if (nextReviewViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewViViDTO = nextReviewViViService.update(nextReviewViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewViViDTO.getId().toString()))
            .body(nextReviewViViDTO);
    }

    /**
     * {@code PATCH  /next-review-vi-vis/:id} : Partial updates given fields of an existing nextReviewViVi, field will ignore if it is null
     *
     * @param id the id of the nextReviewViViDTO to save.
     * @param nextReviewViViDTO the nextReviewViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewViViDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewViViDTO> partialUpdateNextReviewViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewViViDTO nextReviewViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewViVi partially : {}, {}", id, nextReviewViViDTO);
        if (nextReviewViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewViViDTO> result = nextReviewViViService.partialUpdate(nextReviewViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-vi-vis} : get all the nextReviewViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewViViDTO>> getAllNextReviewViVis(
        NextReviewViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewViVis by criteria: {}", criteria);

        Page<NextReviewViViDTO> page = nextReviewViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-vi-vis/count} : count all the nextReviewViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewViVis(NextReviewViViCriteria criteria) {
        LOG.debug("REST request to count NextReviewViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-vi-vis/:id} : get the "id" nextReviewViVi.
     *
     * @param id the id of the nextReviewViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewViViDTO> getNextReviewViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewViVi : {}", id);
        Optional<NextReviewViViDTO> nextReviewViViDTO = nextReviewViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewViViDTO);
    }

    /**
     * {@code DELETE  /next-review-vi-vis/:id} : delete the "id" nextReviewViVi.
     *
     * @param id the id of the nextReviewViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewViVi : {}", id);
        nextReviewViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
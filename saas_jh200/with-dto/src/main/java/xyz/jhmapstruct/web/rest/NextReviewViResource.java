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
import xyz.jhmapstruct.repository.NextReviewViRepository;
import xyz.jhmapstruct.service.NextReviewViQueryService;
import xyz.jhmapstruct.service.NextReviewViService;
import xyz.jhmapstruct.service.criteria.NextReviewViCriteria;
import xyz.jhmapstruct.service.dto.NextReviewViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewVi}.
 */
@RestController
@RequestMapping("/api/next-review-vis")
public class NextReviewViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewViResource.class);

    private static final String ENTITY_NAME = "nextReviewVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewViService nextReviewViService;

    private final NextReviewViRepository nextReviewViRepository;

    private final NextReviewViQueryService nextReviewViQueryService;

    public NextReviewViResource(
        NextReviewViService nextReviewViService,
        NextReviewViRepository nextReviewViRepository,
        NextReviewViQueryService nextReviewViQueryService
    ) {
        this.nextReviewViService = nextReviewViService;
        this.nextReviewViRepository = nextReviewViRepository;
        this.nextReviewViQueryService = nextReviewViQueryService;
    }

    /**
     * {@code POST  /next-review-vis} : Create a new nextReviewVi.
     *
     * @param nextReviewViDTO the nextReviewViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewViDTO, or with status {@code 400 (Bad Request)} if the nextReviewVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewViDTO> createNextReviewVi(@Valid @RequestBody NextReviewViDTO nextReviewViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewVi : {}", nextReviewViDTO);
        if (nextReviewViDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewViDTO = nextReviewViService.save(nextReviewViDTO);
        return ResponseEntity.created(new URI("/api/next-review-vis/" + nextReviewViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewViDTO.getId().toString()))
            .body(nextReviewViDTO);
    }

    /**
     * {@code PUT  /next-review-vis/:id} : Updates an existing nextReviewVi.
     *
     * @param id the id of the nextReviewViDTO to save.
     * @param nextReviewViDTO the nextReviewViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewViDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewViDTO> updateNextReviewVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewViDTO nextReviewViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewVi : {}, {}", id, nextReviewViDTO);
        if (nextReviewViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewViDTO = nextReviewViService.update(nextReviewViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewViDTO.getId().toString()))
            .body(nextReviewViDTO);
    }

    /**
     * {@code PATCH  /next-review-vis/:id} : Partial updates given fields of an existing nextReviewVi, field will ignore if it is null
     *
     * @param id the id of the nextReviewViDTO to save.
     * @param nextReviewViDTO the nextReviewViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewViDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewViDTO> partialUpdateNextReviewVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewViDTO nextReviewViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewVi partially : {}, {}", id, nextReviewViDTO);
        if (nextReviewViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewViDTO> result = nextReviewViService.partialUpdate(nextReviewViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-vis} : get all the nextReviewVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewViDTO>> getAllNextReviewVis(
        NextReviewViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewVis by criteria: {}", criteria);

        Page<NextReviewViDTO> page = nextReviewViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-vis/count} : count all the nextReviewVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewVis(NextReviewViCriteria criteria) {
        LOG.debug("REST request to count NextReviewVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-vis/:id} : get the "id" nextReviewVi.
     *
     * @param id the id of the nextReviewViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewViDTO> getNextReviewVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewVi : {}", id);
        Optional<NextReviewViDTO> nextReviewViDTO = nextReviewViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewViDTO);
    }

    /**
     * {@code DELETE  /next-review-vis/:id} : delete the "id" nextReviewVi.
     *
     * @param id the id of the nextReviewViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewVi : {}", id);
        nextReviewViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

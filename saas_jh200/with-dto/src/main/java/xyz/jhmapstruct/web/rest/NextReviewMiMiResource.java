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
import xyz.jhmapstruct.repository.NextReviewMiMiRepository;
import xyz.jhmapstruct.service.NextReviewMiMiQueryService;
import xyz.jhmapstruct.service.NextReviewMiMiService;
import xyz.jhmapstruct.service.criteria.NextReviewMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextReviewMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewMiMi}.
 */
@RestController
@RequestMapping("/api/next-review-mi-mis")
public class NextReviewMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiMiResource.class);

    private static final String ENTITY_NAME = "nextReviewMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewMiMiService nextReviewMiMiService;

    private final NextReviewMiMiRepository nextReviewMiMiRepository;

    private final NextReviewMiMiQueryService nextReviewMiMiQueryService;

    public NextReviewMiMiResource(
        NextReviewMiMiService nextReviewMiMiService,
        NextReviewMiMiRepository nextReviewMiMiRepository,
        NextReviewMiMiQueryService nextReviewMiMiQueryService
    ) {
        this.nextReviewMiMiService = nextReviewMiMiService;
        this.nextReviewMiMiRepository = nextReviewMiMiRepository;
        this.nextReviewMiMiQueryService = nextReviewMiMiQueryService;
    }

    /**
     * {@code POST  /next-review-mi-mis} : Create a new nextReviewMiMi.
     *
     * @param nextReviewMiMiDTO the nextReviewMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewMiMiDTO, or with status {@code 400 (Bad Request)} if the nextReviewMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewMiMiDTO> createNextReviewMiMi(@Valid @RequestBody NextReviewMiMiDTO nextReviewMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewMiMi : {}", nextReviewMiMiDTO);
        if (nextReviewMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewMiMiDTO = nextReviewMiMiService.save(nextReviewMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-review-mi-mis/" + nextReviewMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewMiMiDTO.getId().toString()))
            .body(nextReviewMiMiDTO);
    }

    /**
     * {@code PUT  /next-review-mi-mis/:id} : Updates an existing nextReviewMiMi.
     *
     * @param id the id of the nextReviewMiMiDTO to save.
     * @param nextReviewMiMiDTO the nextReviewMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewMiMiDTO> updateNextReviewMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewMiMiDTO nextReviewMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewMiMi : {}, {}", id, nextReviewMiMiDTO);
        if (nextReviewMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewMiMiDTO = nextReviewMiMiService.update(nextReviewMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewMiMiDTO.getId().toString()))
            .body(nextReviewMiMiDTO);
    }

    /**
     * {@code PATCH  /next-review-mi-mis/:id} : Partial updates given fields of an existing nextReviewMiMi, field will ignore if it is null
     *
     * @param id the id of the nextReviewMiMiDTO to save.
     * @param nextReviewMiMiDTO the nextReviewMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewMiMiDTO> partialUpdateNextReviewMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewMiMiDTO nextReviewMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewMiMi partially : {}, {}", id, nextReviewMiMiDTO);
        if (nextReviewMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewMiMiDTO> result = nextReviewMiMiService.partialUpdate(nextReviewMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-mi-mis} : get all the nextReviewMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewMiMiDTO>> getAllNextReviewMiMis(
        NextReviewMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewMiMis by criteria: {}", criteria);

        Page<NextReviewMiMiDTO> page = nextReviewMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-mi-mis/count} : count all the nextReviewMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewMiMis(NextReviewMiMiCriteria criteria) {
        LOG.debug("REST request to count NextReviewMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-mi-mis/:id} : get the "id" nextReviewMiMi.
     *
     * @param id the id of the nextReviewMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewMiMiDTO> getNextReviewMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewMiMi : {}", id);
        Optional<NextReviewMiMiDTO> nextReviewMiMiDTO = nextReviewMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewMiMiDTO);
    }

    /**
     * {@code DELETE  /next-review-mi-mis/:id} : delete the "id" nextReviewMiMi.
     *
     * @param id the id of the nextReviewMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewMiMi : {}", id);
        nextReviewMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

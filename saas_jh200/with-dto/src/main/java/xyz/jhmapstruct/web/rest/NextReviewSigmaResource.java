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
import xyz.jhmapstruct.repository.NextReviewSigmaRepository;
import xyz.jhmapstruct.service.NextReviewSigmaQueryService;
import xyz.jhmapstruct.service.NextReviewSigmaService;
import xyz.jhmapstruct.service.criteria.NextReviewSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextReviewSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewSigma}.
 */
@RestController
@RequestMapping("/api/next-review-sigmas")
public class NextReviewSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewSigmaResource.class);

    private static final String ENTITY_NAME = "nextReviewSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewSigmaService nextReviewSigmaService;

    private final NextReviewSigmaRepository nextReviewSigmaRepository;

    private final NextReviewSigmaQueryService nextReviewSigmaQueryService;

    public NextReviewSigmaResource(
        NextReviewSigmaService nextReviewSigmaService,
        NextReviewSigmaRepository nextReviewSigmaRepository,
        NextReviewSigmaQueryService nextReviewSigmaQueryService
    ) {
        this.nextReviewSigmaService = nextReviewSigmaService;
        this.nextReviewSigmaRepository = nextReviewSigmaRepository;
        this.nextReviewSigmaQueryService = nextReviewSigmaQueryService;
    }

    /**
     * {@code POST  /next-review-sigmas} : Create a new nextReviewSigma.
     *
     * @param nextReviewSigmaDTO the nextReviewSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewSigmaDTO, or with status {@code 400 (Bad Request)} if the nextReviewSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewSigmaDTO> createNextReviewSigma(@Valid @RequestBody NextReviewSigmaDTO nextReviewSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewSigma : {}", nextReviewSigmaDTO);
        if (nextReviewSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewSigmaDTO = nextReviewSigmaService.save(nextReviewSigmaDTO);
        return ResponseEntity.created(new URI("/api/next-review-sigmas/" + nextReviewSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewSigmaDTO.getId().toString()))
            .body(nextReviewSigmaDTO);
    }

    /**
     * {@code PUT  /next-review-sigmas/:id} : Updates an existing nextReviewSigma.
     *
     * @param id the id of the nextReviewSigmaDTO to save.
     * @param nextReviewSigmaDTO the nextReviewSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewSigmaDTO> updateNextReviewSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewSigmaDTO nextReviewSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewSigma : {}, {}", id, nextReviewSigmaDTO);
        if (nextReviewSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewSigmaDTO = nextReviewSigmaService.update(nextReviewSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewSigmaDTO.getId().toString()))
            .body(nextReviewSigmaDTO);
    }

    /**
     * {@code PATCH  /next-review-sigmas/:id} : Partial updates given fields of an existing nextReviewSigma, field will ignore if it is null
     *
     * @param id the id of the nextReviewSigmaDTO to save.
     * @param nextReviewSigmaDTO the nextReviewSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewSigmaDTO> partialUpdateNextReviewSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewSigmaDTO nextReviewSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewSigma partially : {}, {}", id, nextReviewSigmaDTO);
        if (nextReviewSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewSigmaDTO> result = nextReviewSigmaService.partialUpdate(nextReviewSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-sigmas} : get all the nextReviewSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewSigmaDTO>> getAllNextReviewSigmas(
        NextReviewSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewSigmas by criteria: {}", criteria);

        Page<NextReviewSigmaDTO> page = nextReviewSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-sigmas/count} : count all the nextReviewSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewSigmas(NextReviewSigmaCriteria criteria) {
        LOG.debug("REST request to count NextReviewSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-sigmas/:id} : get the "id" nextReviewSigma.
     *
     * @param id the id of the nextReviewSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewSigmaDTO> getNextReviewSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewSigma : {}", id);
        Optional<NextReviewSigmaDTO> nextReviewSigmaDTO = nextReviewSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewSigmaDTO);
    }

    /**
     * {@code DELETE  /next-review-sigmas/:id} : delete the "id" nextReviewSigma.
     *
     * @param id the id of the nextReviewSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewSigma : {}", id);
        nextReviewSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

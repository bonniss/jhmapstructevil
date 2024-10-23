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
import xyz.jhmapstruct.repository.NextReviewAlphaRepository;
import xyz.jhmapstruct.service.NextReviewAlphaQueryService;
import xyz.jhmapstruct.service.NextReviewAlphaService;
import xyz.jhmapstruct.service.criteria.NextReviewAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextReviewAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewAlpha}.
 */
@RestController
@RequestMapping("/api/next-review-alphas")
public class NextReviewAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewAlphaResource.class);

    private static final String ENTITY_NAME = "nextReviewAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewAlphaService nextReviewAlphaService;

    private final NextReviewAlphaRepository nextReviewAlphaRepository;

    private final NextReviewAlphaQueryService nextReviewAlphaQueryService;

    public NextReviewAlphaResource(
        NextReviewAlphaService nextReviewAlphaService,
        NextReviewAlphaRepository nextReviewAlphaRepository,
        NextReviewAlphaQueryService nextReviewAlphaQueryService
    ) {
        this.nextReviewAlphaService = nextReviewAlphaService;
        this.nextReviewAlphaRepository = nextReviewAlphaRepository;
        this.nextReviewAlphaQueryService = nextReviewAlphaQueryService;
    }

    /**
     * {@code POST  /next-review-alphas} : Create a new nextReviewAlpha.
     *
     * @param nextReviewAlphaDTO the nextReviewAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewAlphaDTO, or with status {@code 400 (Bad Request)} if the nextReviewAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewAlphaDTO> createNextReviewAlpha(@Valid @RequestBody NextReviewAlphaDTO nextReviewAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewAlpha : {}", nextReviewAlphaDTO);
        if (nextReviewAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewAlphaDTO = nextReviewAlphaService.save(nextReviewAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-review-alphas/" + nextReviewAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewAlphaDTO.getId().toString()))
            .body(nextReviewAlphaDTO);
    }

    /**
     * {@code PUT  /next-review-alphas/:id} : Updates an existing nextReviewAlpha.
     *
     * @param id the id of the nextReviewAlphaDTO to save.
     * @param nextReviewAlphaDTO the nextReviewAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewAlphaDTO> updateNextReviewAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewAlphaDTO nextReviewAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewAlpha : {}, {}", id, nextReviewAlphaDTO);
        if (nextReviewAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewAlphaDTO = nextReviewAlphaService.update(nextReviewAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewAlphaDTO.getId().toString()))
            .body(nextReviewAlphaDTO);
    }

    /**
     * {@code PATCH  /next-review-alphas/:id} : Partial updates given fields of an existing nextReviewAlpha, field will ignore if it is null
     *
     * @param id the id of the nextReviewAlphaDTO to save.
     * @param nextReviewAlphaDTO the nextReviewAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextReviewAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewAlphaDTO> partialUpdateNextReviewAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewAlphaDTO nextReviewAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewAlpha partially : {}, {}", id, nextReviewAlphaDTO);
        if (nextReviewAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewAlphaDTO> result = nextReviewAlphaService.partialUpdate(nextReviewAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-alphas} : get all the nextReviewAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewAlphaDTO>> getAllNextReviewAlphas(
        NextReviewAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewAlphas by criteria: {}", criteria);

        Page<NextReviewAlphaDTO> page = nextReviewAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-alphas/count} : count all the nextReviewAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewAlphas(NextReviewAlphaCriteria criteria) {
        LOG.debug("REST request to count NextReviewAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-alphas/:id} : get the "id" nextReviewAlpha.
     *
     * @param id the id of the nextReviewAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewAlphaDTO> getNextReviewAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewAlpha : {}", id);
        Optional<NextReviewAlphaDTO> nextReviewAlphaDTO = nextReviewAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewAlphaDTO);
    }

    /**
     * {@code DELETE  /next-review-alphas/:id} : delete the "id" nextReviewAlpha.
     *
     * @param id the id of the nextReviewAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewAlpha : {}", id);
        nextReviewAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

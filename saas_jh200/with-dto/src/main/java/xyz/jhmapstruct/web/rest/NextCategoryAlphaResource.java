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
import xyz.jhmapstruct.repository.NextCategoryAlphaRepository;
import xyz.jhmapstruct.service.NextCategoryAlphaQueryService;
import xyz.jhmapstruct.service.NextCategoryAlphaService;
import xyz.jhmapstruct.service.criteria.NextCategoryAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryAlpha}.
 */
@RestController
@RequestMapping("/api/next-category-alphas")
public class NextCategoryAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryAlphaResource.class);

    private static final String ENTITY_NAME = "nextCategoryAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryAlphaService nextCategoryAlphaService;

    private final NextCategoryAlphaRepository nextCategoryAlphaRepository;

    private final NextCategoryAlphaQueryService nextCategoryAlphaQueryService;

    public NextCategoryAlphaResource(
        NextCategoryAlphaService nextCategoryAlphaService,
        NextCategoryAlphaRepository nextCategoryAlphaRepository,
        NextCategoryAlphaQueryService nextCategoryAlphaQueryService
    ) {
        this.nextCategoryAlphaService = nextCategoryAlphaService;
        this.nextCategoryAlphaRepository = nextCategoryAlphaRepository;
        this.nextCategoryAlphaQueryService = nextCategoryAlphaQueryService;
    }

    /**
     * {@code POST  /next-category-alphas} : Create a new nextCategoryAlpha.
     *
     * @param nextCategoryAlphaDTO the nextCategoryAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryAlphaDTO, or with status {@code 400 (Bad Request)} if the nextCategoryAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryAlphaDTO> createNextCategoryAlpha(@Valid @RequestBody NextCategoryAlphaDTO nextCategoryAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryAlpha : {}", nextCategoryAlphaDTO);
        if (nextCategoryAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryAlphaDTO = nextCategoryAlphaService.save(nextCategoryAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-category-alphas/" + nextCategoryAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryAlphaDTO.getId().toString()))
            .body(nextCategoryAlphaDTO);
    }

    /**
     * {@code PUT  /next-category-alphas/:id} : Updates an existing nextCategoryAlpha.
     *
     * @param id the id of the nextCategoryAlphaDTO to save.
     * @param nextCategoryAlphaDTO the nextCategoryAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryAlphaDTO> updateNextCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryAlphaDTO nextCategoryAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryAlpha : {}, {}", id, nextCategoryAlphaDTO);
        if (nextCategoryAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryAlphaDTO = nextCategoryAlphaService.update(nextCategoryAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryAlphaDTO.getId().toString()))
            .body(nextCategoryAlphaDTO);
    }

    /**
     * {@code PATCH  /next-category-alphas/:id} : Partial updates given fields of an existing nextCategoryAlpha, field will ignore if it is null
     *
     * @param id the id of the nextCategoryAlphaDTO to save.
     * @param nextCategoryAlphaDTO the nextCategoryAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryAlphaDTO> partialUpdateNextCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryAlphaDTO nextCategoryAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryAlpha partially : {}, {}", id, nextCategoryAlphaDTO);
        if (nextCategoryAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryAlphaDTO> result = nextCategoryAlphaService.partialUpdate(nextCategoryAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-alphas} : get all the nextCategoryAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryAlphaDTO>> getAllNextCategoryAlphas(
        NextCategoryAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryAlphas by criteria: {}", criteria);

        Page<NextCategoryAlphaDTO> page = nextCategoryAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-alphas/count} : count all the nextCategoryAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryAlphas(NextCategoryAlphaCriteria criteria) {
        LOG.debug("REST request to count NextCategoryAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-alphas/:id} : get the "id" nextCategoryAlpha.
     *
     * @param id the id of the nextCategoryAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryAlphaDTO> getNextCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryAlpha : {}", id);
        Optional<NextCategoryAlphaDTO> nextCategoryAlphaDTO = nextCategoryAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryAlphaDTO);
    }

    /**
     * {@code DELETE  /next-category-alphas/:id} : delete the "id" nextCategoryAlpha.
     *
     * @param id the id of the nextCategoryAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryAlpha : {}", id);
        nextCategoryAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

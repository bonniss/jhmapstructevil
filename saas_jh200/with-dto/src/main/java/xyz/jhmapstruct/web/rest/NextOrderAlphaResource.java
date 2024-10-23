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
import xyz.jhmapstruct.repository.NextOrderAlphaRepository;
import xyz.jhmapstruct.service.NextOrderAlphaQueryService;
import xyz.jhmapstruct.service.NextOrderAlphaService;
import xyz.jhmapstruct.service.criteria.NextOrderAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderAlpha}.
 */
@RestController
@RequestMapping("/api/next-order-alphas")
public class NextOrderAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderAlphaResource.class);

    private static final String ENTITY_NAME = "nextOrderAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderAlphaService nextOrderAlphaService;

    private final NextOrderAlphaRepository nextOrderAlphaRepository;

    private final NextOrderAlphaQueryService nextOrderAlphaQueryService;

    public NextOrderAlphaResource(
        NextOrderAlphaService nextOrderAlphaService,
        NextOrderAlphaRepository nextOrderAlphaRepository,
        NextOrderAlphaQueryService nextOrderAlphaQueryService
    ) {
        this.nextOrderAlphaService = nextOrderAlphaService;
        this.nextOrderAlphaRepository = nextOrderAlphaRepository;
        this.nextOrderAlphaQueryService = nextOrderAlphaQueryService;
    }

    /**
     * {@code POST  /next-order-alphas} : Create a new nextOrderAlpha.
     *
     * @param nextOrderAlphaDTO the nextOrderAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderAlphaDTO, or with status {@code 400 (Bad Request)} if the nextOrderAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderAlphaDTO> createNextOrderAlpha(@Valid @RequestBody NextOrderAlphaDTO nextOrderAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextOrderAlpha : {}", nextOrderAlphaDTO);
        if (nextOrderAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderAlphaDTO = nextOrderAlphaService.save(nextOrderAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-order-alphas/" + nextOrderAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderAlphaDTO.getId().toString()))
            .body(nextOrderAlphaDTO);
    }

    /**
     * {@code PUT  /next-order-alphas/:id} : Updates an existing nextOrderAlpha.
     *
     * @param id the id of the nextOrderAlphaDTO to save.
     * @param nextOrderAlphaDTO the nextOrderAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderAlphaDTO> updateNextOrderAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderAlphaDTO nextOrderAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderAlpha : {}, {}", id, nextOrderAlphaDTO);
        if (nextOrderAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderAlphaDTO = nextOrderAlphaService.update(nextOrderAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderAlphaDTO.getId().toString()))
            .body(nextOrderAlphaDTO);
    }

    /**
     * {@code PATCH  /next-order-alphas/:id} : Partial updates given fields of an existing nextOrderAlpha, field will ignore if it is null
     *
     * @param id the id of the nextOrderAlphaDTO to save.
     * @param nextOrderAlphaDTO the nextOrderAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderAlphaDTO> partialUpdateNextOrderAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderAlphaDTO nextOrderAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderAlpha partially : {}, {}", id, nextOrderAlphaDTO);
        if (nextOrderAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderAlphaDTO> result = nextOrderAlphaService.partialUpdate(nextOrderAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-alphas} : get all the nextOrderAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderAlphaDTO>> getAllNextOrderAlphas(
        NextOrderAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderAlphas by criteria: {}", criteria);

        Page<NextOrderAlphaDTO> page = nextOrderAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-alphas/count} : count all the nextOrderAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderAlphas(NextOrderAlphaCriteria criteria) {
        LOG.debug("REST request to count NextOrderAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-alphas/:id} : get the "id" nextOrderAlpha.
     *
     * @param id the id of the nextOrderAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderAlphaDTO> getNextOrderAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderAlpha : {}", id);
        Optional<NextOrderAlphaDTO> nextOrderAlphaDTO = nextOrderAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderAlphaDTO);
    }

    /**
     * {@code DELETE  /next-order-alphas/:id} : delete the "id" nextOrderAlpha.
     *
     * @param id the id of the nextOrderAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderAlpha : {}", id);
        nextOrderAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

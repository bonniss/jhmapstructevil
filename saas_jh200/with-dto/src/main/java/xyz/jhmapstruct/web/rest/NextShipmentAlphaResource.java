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
import xyz.jhmapstruct.repository.NextShipmentAlphaRepository;
import xyz.jhmapstruct.service.NextShipmentAlphaQueryService;
import xyz.jhmapstruct.service.NextShipmentAlphaService;
import xyz.jhmapstruct.service.criteria.NextShipmentAlphaCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentAlpha}.
 */
@RestController
@RequestMapping("/api/next-shipment-alphas")
public class NextShipmentAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentAlphaResource.class);

    private static final String ENTITY_NAME = "nextShipmentAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentAlphaService nextShipmentAlphaService;

    private final NextShipmentAlphaRepository nextShipmentAlphaRepository;

    private final NextShipmentAlphaQueryService nextShipmentAlphaQueryService;

    public NextShipmentAlphaResource(
        NextShipmentAlphaService nextShipmentAlphaService,
        NextShipmentAlphaRepository nextShipmentAlphaRepository,
        NextShipmentAlphaQueryService nextShipmentAlphaQueryService
    ) {
        this.nextShipmentAlphaService = nextShipmentAlphaService;
        this.nextShipmentAlphaRepository = nextShipmentAlphaRepository;
        this.nextShipmentAlphaQueryService = nextShipmentAlphaQueryService;
    }

    /**
     * {@code POST  /next-shipment-alphas} : Create a new nextShipmentAlpha.
     *
     * @param nextShipmentAlphaDTO the nextShipmentAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentAlphaDTO, or with status {@code 400 (Bad Request)} if the nextShipmentAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentAlphaDTO> createNextShipmentAlpha(@Valid @RequestBody NextShipmentAlphaDTO nextShipmentAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentAlpha : {}", nextShipmentAlphaDTO);
        if (nextShipmentAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentAlphaDTO = nextShipmentAlphaService.save(nextShipmentAlphaDTO);
        return ResponseEntity.created(new URI("/api/next-shipment-alphas/" + nextShipmentAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentAlphaDTO.getId().toString()))
            .body(nextShipmentAlphaDTO);
    }

    /**
     * {@code PUT  /next-shipment-alphas/:id} : Updates an existing nextShipmentAlpha.
     *
     * @param id the id of the nextShipmentAlphaDTO to save.
     * @param nextShipmentAlphaDTO the nextShipmentAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentAlphaDTO> updateNextShipmentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentAlphaDTO nextShipmentAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentAlpha : {}, {}", id, nextShipmentAlphaDTO);
        if (nextShipmentAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentAlphaDTO = nextShipmentAlphaService.update(nextShipmentAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentAlphaDTO.getId().toString()))
            .body(nextShipmentAlphaDTO);
    }

    /**
     * {@code PATCH  /next-shipment-alphas/:id} : Partial updates given fields of an existing nextShipmentAlpha, field will ignore if it is null
     *
     * @param id the id of the nextShipmentAlphaDTO to save.
     * @param nextShipmentAlphaDTO the nextShipmentAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentAlphaDTO> partialUpdateNextShipmentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentAlphaDTO nextShipmentAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentAlpha partially : {}, {}", id, nextShipmentAlphaDTO);
        if (nextShipmentAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentAlphaDTO> result = nextShipmentAlphaService.partialUpdate(nextShipmentAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-alphas} : get all the nextShipmentAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentAlphaDTO>> getAllNextShipmentAlphas(
        NextShipmentAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentAlphas by criteria: {}", criteria);

        Page<NextShipmentAlphaDTO> page = nextShipmentAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-alphas/count} : count all the nextShipmentAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentAlphas(NextShipmentAlphaCriteria criteria) {
        LOG.debug("REST request to count NextShipmentAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-alphas/:id} : get the "id" nextShipmentAlpha.
     *
     * @param id the id of the nextShipmentAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentAlphaDTO> getNextShipmentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentAlpha : {}", id);
        Optional<NextShipmentAlphaDTO> nextShipmentAlphaDTO = nextShipmentAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentAlphaDTO);
    }

    /**
     * {@code DELETE  /next-shipment-alphas/:id} : delete the "id" nextShipmentAlpha.
     *
     * @param id the id of the nextShipmentAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentAlpha : {}", id);
        nextShipmentAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

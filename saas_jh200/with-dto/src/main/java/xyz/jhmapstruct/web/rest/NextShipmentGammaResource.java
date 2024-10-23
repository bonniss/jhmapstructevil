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
import xyz.jhmapstruct.repository.NextShipmentGammaRepository;
import xyz.jhmapstruct.service.NextShipmentGammaQueryService;
import xyz.jhmapstruct.service.NextShipmentGammaService;
import xyz.jhmapstruct.service.criteria.NextShipmentGammaCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentGamma}.
 */
@RestController
@RequestMapping("/api/next-shipment-gammas")
public class NextShipmentGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentGammaResource.class);

    private static final String ENTITY_NAME = "nextShipmentGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentGammaService nextShipmentGammaService;

    private final NextShipmentGammaRepository nextShipmentGammaRepository;

    private final NextShipmentGammaQueryService nextShipmentGammaQueryService;

    public NextShipmentGammaResource(
        NextShipmentGammaService nextShipmentGammaService,
        NextShipmentGammaRepository nextShipmentGammaRepository,
        NextShipmentGammaQueryService nextShipmentGammaQueryService
    ) {
        this.nextShipmentGammaService = nextShipmentGammaService;
        this.nextShipmentGammaRepository = nextShipmentGammaRepository;
        this.nextShipmentGammaQueryService = nextShipmentGammaQueryService;
    }

    /**
     * {@code POST  /next-shipment-gammas} : Create a new nextShipmentGamma.
     *
     * @param nextShipmentGammaDTO the nextShipmentGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentGammaDTO, or with status {@code 400 (Bad Request)} if the nextShipmentGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentGammaDTO> createNextShipmentGamma(@Valid @RequestBody NextShipmentGammaDTO nextShipmentGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentGamma : {}", nextShipmentGammaDTO);
        if (nextShipmentGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentGammaDTO = nextShipmentGammaService.save(nextShipmentGammaDTO);
        return ResponseEntity.created(new URI("/api/next-shipment-gammas/" + nextShipmentGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentGammaDTO.getId().toString()))
            .body(nextShipmentGammaDTO);
    }

    /**
     * {@code PUT  /next-shipment-gammas/:id} : Updates an existing nextShipmentGamma.
     *
     * @param id the id of the nextShipmentGammaDTO to save.
     * @param nextShipmentGammaDTO the nextShipmentGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentGammaDTO> updateNextShipmentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentGammaDTO nextShipmentGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentGamma : {}, {}", id, nextShipmentGammaDTO);
        if (nextShipmentGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentGammaDTO = nextShipmentGammaService.update(nextShipmentGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentGammaDTO.getId().toString()))
            .body(nextShipmentGammaDTO);
    }

    /**
     * {@code PATCH  /next-shipment-gammas/:id} : Partial updates given fields of an existing nextShipmentGamma, field will ignore if it is null
     *
     * @param id the id of the nextShipmentGammaDTO to save.
     * @param nextShipmentGammaDTO the nextShipmentGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentGammaDTO> partialUpdateNextShipmentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentGammaDTO nextShipmentGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentGamma partially : {}, {}", id, nextShipmentGammaDTO);
        if (nextShipmentGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentGammaDTO> result = nextShipmentGammaService.partialUpdate(nextShipmentGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-gammas} : get all the nextShipmentGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentGammaDTO>> getAllNextShipmentGammas(
        NextShipmentGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentGammas by criteria: {}", criteria);

        Page<NextShipmentGammaDTO> page = nextShipmentGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-gammas/count} : count all the nextShipmentGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentGammas(NextShipmentGammaCriteria criteria) {
        LOG.debug("REST request to count NextShipmentGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-gammas/:id} : get the "id" nextShipmentGamma.
     *
     * @param id the id of the nextShipmentGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentGammaDTO> getNextShipmentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentGamma : {}", id);
        Optional<NextShipmentGammaDTO> nextShipmentGammaDTO = nextShipmentGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentGammaDTO);
    }

    /**
     * {@code DELETE  /next-shipment-gammas/:id} : delete the "id" nextShipmentGamma.
     *
     * @param id the id of the nextShipmentGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentGamma : {}", id);
        nextShipmentGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

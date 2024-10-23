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
import xyz.jhmapstruct.repository.NextShipmentMiMiRepository;
import xyz.jhmapstruct.service.NextShipmentMiMiQueryService;
import xyz.jhmapstruct.service.NextShipmentMiMiService;
import xyz.jhmapstruct.service.criteria.NextShipmentMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextShipmentMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentMiMi}.
 */
@RestController
@RequestMapping("/api/next-shipment-mi-mis")
public class NextShipmentMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentMiMiResource.class);

    private static final String ENTITY_NAME = "nextShipmentMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentMiMiService nextShipmentMiMiService;

    private final NextShipmentMiMiRepository nextShipmentMiMiRepository;

    private final NextShipmentMiMiQueryService nextShipmentMiMiQueryService;

    public NextShipmentMiMiResource(
        NextShipmentMiMiService nextShipmentMiMiService,
        NextShipmentMiMiRepository nextShipmentMiMiRepository,
        NextShipmentMiMiQueryService nextShipmentMiMiQueryService
    ) {
        this.nextShipmentMiMiService = nextShipmentMiMiService;
        this.nextShipmentMiMiRepository = nextShipmentMiMiRepository;
        this.nextShipmentMiMiQueryService = nextShipmentMiMiQueryService;
    }

    /**
     * {@code POST  /next-shipment-mi-mis} : Create a new nextShipmentMiMi.
     *
     * @param nextShipmentMiMiDTO the nextShipmentMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentMiMiDTO, or with status {@code 400 (Bad Request)} if the nextShipmentMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentMiMiDTO> createNextShipmentMiMi(@Valid @RequestBody NextShipmentMiMiDTO nextShipmentMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentMiMi : {}", nextShipmentMiMiDTO);
        if (nextShipmentMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentMiMiDTO = nextShipmentMiMiService.save(nextShipmentMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-shipment-mi-mis/" + nextShipmentMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentMiMiDTO.getId().toString()))
            .body(nextShipmentMiMiDTO);
    }

    /**
     * {@code PUT  /next-shipment-mi-mis/:id} : Updates an existing nextShipmentMiMi.
     *
     * @param id the id of the nextShipmentMiMiDTO to save.
     * @param nextShipmentMiMiDTO the nextShipmentMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentMiMiDTO> updateNextShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentMiMiDTO nextShipmentMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentMiMi : {}, {}", id, nextShipmentMiMiDTO);
        if (nextShipmentMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentMiMiDTO = nextShipmentMiMiService.update(nextShipmentMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentMiMiDTO.getId().toString()))
            .body(nextShipmentMiMiDTO);
    }

    /**
     * {@code PATCH  /next-shipment-mi-mis/:id} : Partial updates given fields of an existing nextShipmentMiMi, field will ignore if it is null
     *
     * @param id the id of the nextShipmentMiMiDTO to save.
     * @param nextShipmentMiMiDTO the nextShipmentMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextShipmentMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentMiMiDTO> partialUpdateNextShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentMiMiDTO nextShipmentMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentMiMi partially : {}, {}", id, nextShipmentMiMiDTO);
        if (nextShipmentMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentMiMiDTO> result = nextShipmentMiMiService.partialUpdate(nextShipmentMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-mi-mis} : get all the nextShipmentMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentMiMiDTO>> getAllNextShipmentMiMis(
        NextShipmentMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentMiMis by criteria: {}", criteria);

        Page<NextShipmentMiMiDTO> page = nextShipmentMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-mi-mis/count} : count all the nextShipmentMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentMiMis(NextShipmentMiMiCriteria criteria) {
        LOG.debug("REST request to count NextShipmentMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-mi-mis/:id} : get the "id" nextShipmentMiMi.
     *
     * @param id the id of the nextShipmentMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentMiMiDTO> getNextShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentMiMi : {}", id);
        Optional<NextShipmentMiMiDTO> nextShipmentMiMiDTO = nextShipmentMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentMiMiDTO);
    }

    /**
     * {@code DELETE  /next-shipment-mi-mis/:id} : delete the "id" nextShipmentMiMi.
     *
     * @param id the id of the nextShipmentMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentMiMi : {}", id);
        nextShipmentMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

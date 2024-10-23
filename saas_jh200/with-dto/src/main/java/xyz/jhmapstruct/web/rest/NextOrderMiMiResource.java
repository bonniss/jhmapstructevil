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
import xyz.jhmapstruct.repository.NextOrderMiMiRepository;
import xyz.jhmapstruct.service.NextOrderMiMiQueryService;
import xyz.jhmapstruct.service.NextOrderMiMiService;
import xyz.jhmapstruct.service.criteria.NextOrderMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextOrderMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderMiMi}.
 */
@RestController
@RequestMapping("/api/next-order-mi-mis")
public class NextOrderMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderMiMiResource.class);

    private static final String ENTITY_NAME = "nextOrderMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderMiMiService nextOrderMiMiService;

    private final NextOrderMiMiRepository nextOrderMiMiRepository;

    private final NextOrderMiMiQueryService nextOrderMiMiQueryService;

    public NextOrderMiMiResource(
        NextOrderMiMiService nextOrderMiMiService,
        NextOrderMiMiRepository nextOrderMiMiRepository,
        NextOrderMiMiQueryService nextOrderMiMiQueryService
    ) {
        this.nextOrderMiMiService = nextOrderMiMiService;
        this.nextOrderMiMiRepository = nextOrderMiMiRepository;
        this.nextOrderMiMiQueryService = nextOrderMiMiQueryService;
    }

    /**
     * {@code POST  /next-order-mi-mis} : Create a new nextOrderMiMi.
     *
     * @param nextOrderMiMiDTO the nextOrderMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderMiMiDTO, or with status {@code 400 (Bad Request)} if the nextOrderMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderMiMiDTO> createNextOrderMiMi(@Valid @RequestBody NextOrderMiMiDTO nextOrderMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextOrderMiMi : {}", nextOrderMiMiDTO);
        if (nextOrderMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderMiMiDTO = nextOrderMiMiService.save(nextOrderMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-order-mi-mis/" + nextOrderMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderMiMiDTO.getId().toString()))
            .body(nextOrderMiMiDTO);
    }

    /**
     * {@code PUT  /next-order-mi-mis/:id} : Updates an existing nextOrderMiMi.
     *
     * @param id the id of the nextOrderMiMiDTO to save.
     * @param nextOrderMiMiDTO the nextOrderMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderMiMiDTO> updateNextOrderMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderMiMiDTO nextOrderMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderMiMi : {}, {}", id, nextOrderMiMiDTO);
        if (nextOrderMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderMiMiDTO = nextOrderMiMiService.update(nextOrderMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderMiMiDTO.getId().toString()))
            .body(nextOrderMiMiDTO);
    }

    /**
     * {@code PATCH  /next-order-mi-mis/:id} : Partial updates given fields of an existing nextOrderMiMi, field will ignore if it is null
     *
     * @param id the id of the nextOrderMiMiDTO to save.
     * @param nextOrderMiMiDTO the nextOrderMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderMiMiDTO> partialUpdateNextOrderMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderMiMiDTO nextOrderMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderMiMi partially : {}, {}", id, nextOrderMiMiDTO);
        if (nextOrderMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderMiMiDTO> result = nextOrderMiMiService.partialUpdate(nextOrderMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-mi-mis} : get all the nextOrderMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderMiMiDTO>> getAllNextOrderMiMis(
        NextOrderMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderMiMis by criteria: {}", criteria);

        Page<NextOrderMiMiDTO> page = nextOrderMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-mi-mis/count} : count all the nextOrderMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderMiMis(NextOrderMiMiCriteria criteria) {
        LOG.debug("REST request to count NextOrderMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-mi-mis/:id} : get the "id" nextOrderMiMi.
     *
     * @param id the id of the nextOrderMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderMiMiDTO> getNextOrderMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderMiMi : {}", id);
        Optional<NextOrderMiMiDTO> nextOrderMiMiDTO = nextOrderMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderMiMiDTO);
    }

    /**
     * {@code DELETE  /next-order-mi-mis/:id} : delete the "id" nextOrderMiMi.
     *
     * @param id the id of the nextOrderMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderMiMi : {}", id);
        nextOrderMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

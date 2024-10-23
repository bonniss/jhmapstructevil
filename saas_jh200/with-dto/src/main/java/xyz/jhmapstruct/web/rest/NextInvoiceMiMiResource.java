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
import xyz.jhmapstruct.repository.NextInvoiceMiMiRepository;
import xyz.jhmapstruct.service.NextInvoiceMiMiQueryService;
import xyz.jhmapstruct.service.NextInvoiceMiMiService;
import xyz.jhmapstruct.service.criteria.NextInvoiceMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceMiMi}.
 */
@RestController
@RequestMapping("/api/next-invoice-mi-mis")
public class NextInvoiceMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiMiResource.class);

    private static final String ENTITY_NAME = "nextInvoiceMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceMiMiService nextInvoiceMiMiService;

    private final NextInvoiceMiMiRepository nextInvoiceMiMiRepository;

    private final NextInvoiceMiMiQueryService nextInvoiceMiMiQueryService;

    public NextInvoiceMiMiResource(
        NextInvoiceMiMiService nextInvoiceMiMiService,
        NextInvoiceMiMiRepository nextInvoiceMiMiRepository,
        NextInvoiceMiMiQueryService nextInvoiceMiMiQueryService
    ) {
        this.nextInvoiceMiMiService = nextInvoiceMiMiService;
        this.nextInvoiceMiMiRepository = nextInvoiceMiMiRepository;
        this.nextInvoiceMiMiQueryService = nextInvoiceMiMiQueryService;
    }

    /**
     * {@code POST  /next-invoice-mi-mis} : Create a new nextInvoiceMiMi.
     *
     * @param nextInvoiceMiMiDTO the nextInvoiceMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceMiMiDTO, or with status {@code 400 (Bad Request)} if the nextInvoiceMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceMiMiDTO> createNextInvoiceMiMi(@Valid @RequestBody NextInvoiceMiMiDTO nextInvoiceMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceMiMi : {}", nextInvoiceMiMiDTO);
        if (nextInvoiceMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceMiMiDTO = nextInvoiceMiMiService.save(nextInvoiceMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-invoice-mi-mis/" + nextInvoiceMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceMiMiDTO.getId().toString()))
            .body(nextInvoiceMiMiDTO);
    }

    /**
     * {@code PUT  /next-invoice-mi-mis/:id} : Updates an existing nextInvoiceMiMi.
     *
     * @param id the id of the nextInvoiceMiMiDTO to save.
     * @param nextInvoiceMiMiDTO the nextInvoiceMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceMiMiDTO> updateNextInvoiceMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceMiMiDTO nextInvoiceMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceMiMi : {}, {}", id, nextInvoiceMiMiDTO);
        if (nextInvoiceMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceMiMiDTO = nextInvoiceMiMiService.update(nextInvoiceMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceMiMiDTO.getId().toString()))
            .body(nextInvoiceMiMiDTO);
    }

    /**
     * {@code PATCH  /next-invoice-mi-mis/:id} : Partial updates given fields of an existing nextInvoiceMiMi, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceMiMiDTO to save.
     * @param nextInvoiceMiMiDTO the nextInvoiceMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceMiMiDTO> partialUpdateNextInvoiceMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceMiMiDTO nextInvoiceMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceMiMi partially : {}, {}", id, nextInvoiceMiMiDTO);
        if (nextInvoiceMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceMiMiDTO> result = nextInvoiceMiMiService.partialUpdate(nextInvoiceMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-mi-mis} : get all the nextInvoiceMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceMiMiDTO>> getAllNextInvoiceMiMis(
        NextInvoiceMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceMiMis by criteria: {}", criteria);

        Page<NextInvoiceMiMiDTO> page = nextInvoiceMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-mi-mis/count} : count all the nextInvoiceMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceMiMis(NextInvoiceMiMiCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-mi-mis/:id} : get the "id" nextInvoiceMiMi.
     *
     * @param id the id of the nextInvoiceMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceMiMiDTO> getNextInvoiceMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceMiMi : {}", id);
        Optional<NextInvoiceMiMiDTO> nextInvoiceMiMiDTO = nextInvoiceMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceMiMiDTO);
    }

    /**
     * {@code DELETE  /next-invoice-mi-mis/:id} : delete the "id" nextInvoiceMiMi.
     *
     * @param id the id of the nextInvoiceMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceMiMi : {}", id);
        nextInvoiceMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

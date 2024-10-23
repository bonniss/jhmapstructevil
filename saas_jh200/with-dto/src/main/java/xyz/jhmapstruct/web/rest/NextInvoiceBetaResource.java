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
import xyz.jhmapstruct.repository.NextInvoiceBetaRepository;
import xyz.jhmapstruct.service.NextInvoiceBetaQueryService;
import xyz.jhmapstruct.service.NextInvoiceBetaService;
import xyz.jhmapstruct.service.criteria.NextInvoiceBetaCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceBeta}.
 */
@RestController
@RequestMapping("/api/next-invoice-betas")
public class NextInvoiceBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceBetaResource.class);

    private static final String ENTITY_NAME = "nextInvoiceBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceBetaService nextInvoiceBetaService;

    private final NextInvoiceBetaRepository nextInvoiceBetaRepository;

    private final NextInvoiceBetaQueryService nextInvoiceBetaQueryService;

    public NextInvoiceBetaResource(
        NextInvoiceBetaService nextInvoiceBetaService,
        NextInvoiceBetaRepository nextInvoiceBetaRepository,
        NextInvoiceBetaQueryService nextInvoiceBetaQueryService
    ) {
        this.nextInvoiceBetaService = nextInvoiceBetaService;
        this.nextInvoiceBetaRepository = nextInvoiceBetaRepository;
        this.nextInvoiceBetaQueryService = nextInvoiceBetaQueryService;
    }

    /**
     * {@code POST  /next-invoice-betas} : Create a new nextInvoiceBeta.
     *
     * @param nextInvoiceBetaDTO the nextInvoiceBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceBetaDTO, or with status {@code 400 (Bad Request)} if the nextInvoiceBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceBetaDTO> createNextInvoiceBeta(@Valid @RequestBody NextInvoiceBetaDTO nextInvoiceBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceBeta : {}", nextInvoiceBetaDTO);
        if (nextInvoiceBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceBetaDTO = nextInvoiceBetaService.save(nextInvoiceBetaDTO);
        return ResponseEntity.created(new URI("/api/next-invoice-betas/" + nextInvoiceBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceBetaDTO.getId().toString()))
            .body(nextInvoiceBetaDTO);
    }

    /**
     * {@code PUT  /next-invoice-betas/:id} : Updates an existing nextInvoiceBeta.
     *
     * @param id the id of the nextInvoiceBetaDTO to save.
     * @param nextInvoiceBetaDTO the nextInvoiceBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceBetaDTO> updateNextInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceBetaDTO nextInvoiceBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceBeta : {}, {}", id, nextInvoiceBetaDTO);
        if (nextInvoiceBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceBetaDTO = nextInvoiceBetaService.update(nextInvoiceBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceBetaDTO.getId().toString()))
            .body(nextInvoiceBetaDTO);
    }

    /**
     * {@code PATCH  /next-invoice-betas/:id} : Partial updates given fields of an existing nextInvoiceBeta, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceBetaDTO to save.
     * @param nextInvoiceBetaDTO the nextInvoiceBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceBetaDTO> partialUpdateNextInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceBetaDTO nextInvoiceBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceBeta partially : {}, {}", id, nextInvoiceBetaDTO);
        if (nextInvoiceBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceBetaDTO> result = nextInvoiceBetaService.partialUpdate(nextInvoiceBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-betas} : get all the nextInvoiceBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceBetaDTO>> getAllNextInvoiceBetas(
        NextInvoiceBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceBetas by criteria: {}", criteria);

        Page<NextInvoiceBetaDTO> page = nextInvoiceBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-betas/count} : count all the nextInvoiceBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceBetas(NextInvoiceBetaCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-betas/:id} : get the "id" nextInvoiceBeta.
     *
     * @param id the id of the nextInvoiceBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceBetaDTO> getNextInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceBeta : {}", id);
        Optional<NextInvoiceBetaDTO> nextInvoiceBetaDTO = nextInvoiceBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceBetaDTO);
    }

    /**
     * {@code DELETE  /next-invoice-betas/:id} : delete the "id" nextInvoiceBeta.
     *
     * @param id the id of the nextInvoiceBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceBeta : {}", id);
        nextInvoiceBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

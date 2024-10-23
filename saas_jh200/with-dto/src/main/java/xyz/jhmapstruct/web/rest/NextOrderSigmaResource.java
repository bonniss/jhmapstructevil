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
import xyz.jhmapstruct.repository.NextOrderSigmaRepository;
import xyz.jhmapstruct.service.NextOrderSigmaQueryService;
import xyz.jhmapstruct.service.NextOrderSigmaService;
import xyz.jhmapstruct.service.criteria.NextOrderSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderSigma}.
 */
@RestController
@RequestMapping("/api/next-order-sigmas")
public class NextOrderSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderSigmaResource.class);

    private static final String ENTITY_NAME = "nextOrderSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderSigmaService nextOrderSigmaService;

    private final NextOrderSigmaRepository nextOrderSigmaRepository;

    private final NextOrderSigmaQueryService nextOrderSigmaQueryService;

    public NextOrderSigmaResource(
        NextOrderSigmaService nextOrderSigmaService,
        NextOrderSigmaRepository nextOrderSigmaRepository,
        NextOrderSigmaQueryService nextOrderSigmaQueryService
    ) {
        this.nextOrderSigmaService = nextOrderSigmaService;
        this.nextOrderSigmaRepository = nextOrderSigmaRepository;
        this.nextOrderSigmaQueryService = nextOrderSigmaQueryService;
    }

    /**
     * {@code POST  /next-order-sigmas} : Create a new nextOrderSigma.
     *
     * @param nextOrderSigmaDTO the nextOrderSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderSigmaDTO, or with status {@code 400 (Bad Request)} if the nextOrderSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderSigmaDTO> createNextOrderSigma(@Valid @RequestBody NextOrderSigmaDTO nextOrderSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextOrderSigma : {}", nextOrderSigmaDTO);
        if (nextOrderSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderSigmaDTO = nextOrderSigmaService.save(nextOrderSigmaDTO);
        return ResponseEntity.created(new URI("/api/next-order-sigmas/" + nextOrderSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderSigmaDTO.getId().toString()))
            .body(nextOrderSigmaDTO);
    }

    /**
     * {@code PUT  /next-order-sigmas/:id} : Updates an existing nextOrderSigma.
     *
     * @param id the id of the nextOrderSigmaDTO to save.
     * @param nextOrderSigmaDTO the nextOrderSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderSigmaDTO> updateNextOrderSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderSigmaDTO nextOrderSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderSigma : {}, {}", id, nextOrderSigmaDTO);
        if (nextOrderSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderSigmaDTO = nextOrderSigmaService.update(nextOrderSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderSigmaDTO.getId().toString()))
            .body(nextOrderSigmaDTO);
    }

    /**
     * {@code PATCH  /next-order-sigmas/:id} : Partial updates given fields of an existing nextOrderSigma, field will ignore if it is null
     *
     * @param id the id of the nextOrderSigmaDTO to save.
     * @param nextOrderSigmaDTO the nextOrderSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderSigmaDTO> partialUpdateNextOrderSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderSigmaDTO nextOrderSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderSigma partially : {}, {}", id, nextOrderSigmaDTO);
        if (nextOrderSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderSigmaDTO> result = nextOrderSigmaService.partialUpdate(nextOrderSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-sigmas} : get all the nextOrderSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderSigmaDTO>> getAllNextOrderSigmas(
        NextOrderSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderSigmas by criteria: {}", criteria);

        Page<NextOrderSigmaDTO> page = nextOrderSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-sigmas/count} : count all the nextOrderSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderSigmas(NextOrderSigmaCriteria criteria) {
        LOG.debug("REST request to count NextOrderSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-sigmas/:id} : get the "id" nextOrderSigma.
     *
     * @param id the id of the nextOrderSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderSigmaDTO> getNextOrderSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderSigma : {}", id);
        Optional<NextOrderSigmaDTO> nextOrderSigmaDTO = nextOrderSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderSigmaDTO);
    }

    /**
     * {@code DELETE  /next-order-sigmas/:id} : delete the "id" nextOrderSigma.
     *
     * @param id the id of the nextOrderSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderSigma : {}", id);
        nextOrderSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

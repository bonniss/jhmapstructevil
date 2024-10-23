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
import xyz.jhmapstruct.repository.NextProductSigmaRepository;
import xyz.jhmapstruct.service.NextProductSigmaQueryService;
import xyz.jhmapstruct.service.NextProductSigmaService;
import xyz.jhmapstruct.service.criteria.NextProductSigmaCriteria;
import xyz.jhmapstruct.service.dto.NextProductSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductSigma}.
 */
@RestController
@RequestMapping("/api/next-product-sigmas")
public class NextProductSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductSigmaResource.class);

    private static final String ENTITY_NAME = "nextProductSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductSigmaService nextProductSigmaService;

    private final NextProductSigmaRepository nextProductSigmaRepository;

    private final NextProductSigmaQueryService nextProductSigmaQueryService;

    public NextProductSigmaResource(
        NextProductSigmaService nextProductSigmaService,
        NextProductSigmaRepository nextProductSigmaRepository,
        NextProductSigmaQueryService nextProductSigmaQueryService
    ) {
        this.nextProductSigmaService = nextProductSigmaService;
        this.nextProductSigmaRepository = nextProductSigmaRepository;
        this.nextProductSigmaQueryService = nextProductSigmaQueryService;
    }

    /**
     * {@code POST  /next-product-sigmas} : Create a new nextProductSigma.
     *
     * @param nextProductSigmaDTO the nextProductSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductSigmaDTO, or with status {@code 400 (Bad Request)} if the nextProductSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductSigmaDTO> createNextProductSigma(@Valid @RequestBody NextProductSigmaDTO nextProductSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductSigma : {}", nextProductSigmaDTO);
        if (nextProductSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductSigmaDTO = nextProductSigmaService.save(nextProductSigmaDTO);
        return ResponseEntity.created(new URI("/api/next-product-sigmas/" + nextProductSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductSigmaDTO.getId().toString()))
            .body(nextProductSigmaDTO);
    }

    /**
     * {@code PUT  /next-product-sigmas/:id} : Updates an existing nextProductSigma.
     *
     * @param id the id of the nextProductSigmaDTO to save.
     * @param nextProductSigmaDTO the nextProductSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductSigmaDTO> updateNextProductSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductSigmaDTO nextProductSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductSigma : {}, {}", id, nextProductSigmaDTO);
        if (nextProductSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductSigmaDTO = nextProductSigmaService.update(nextProductSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductSigmaDTO.getId().toString()))
            .body(nextProductSigmaDTO);
    }

    /**
     * {@code PATCH  /next-product-sigmas/:id} : Partial updates given fields of an existing nextProductSigma, field will ignore if it is null
     *
     * @param id the id of the nextProductSigmaDTO to save.
     * @param nextProductSigmaDTO the nextProductSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductSigmaDTO> partialUpdateNextProductSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductSigmaDTO nextProductSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductSigma partially : {}, {}", id, nextProductSigmaDTO);
        if (nextProductSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductSigmaDTO> result = nextProductSigmaService.partialUpdate(nextProductSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-sigmas} : get all the nextProductSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductSigmaDTO>> getAllNextProductSigmas(
        NextProductSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductSigmas by criteria: {}", criteria);

        Page<NextProductSigmaDTO> page = nextProductSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-sigmas/count} : count all the nextProductSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductSigmas(NextProductSigmaCriteria criteria) {
        LOG.debug("REST request to count NextProductSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-sigmas/:id} : get the "id" nextProductSigma.
     *
     * @param id the id of the nextProductSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductSigmaDTO> getNextProductSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductSigma : {}", id);
        Optional<NextProductSigmaDTO> nextProductSigmaDTO = nextProductSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductSigmaDTO);
    }

    /**
     * {@code DELETE  /next-product-sigmas/:id} : delete the "id" nextProductSigma.
     *
     * @param id the id of the nextProductSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductSigma : {}", id);
        nextProductSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

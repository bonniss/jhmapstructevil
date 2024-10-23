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
import xyz.jhmapstruct.repository.NextSupplierMiRepository;
import xyz.jhmapstruct.service.NextSupplierMiQueryService;
import xyz.jhmapstruct.service.NextSupplierMiService;
import xyz.jhmapstruct.service.criteria.NextSupplierMiCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierMi}.
 */
@RestController
@RequestMapping("/api/next-supplier-mis")
public class NextSupplierMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiResource.class);

    private static final String ENTITY_NAME = "nextSupplierMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierMiService nextSupplierMiService;

    private final NextSupplierMiRepository nextSupplierMiRepository;

    private final NextSupplierMiQueryService nextSupplierMiQueryService;

    public NextSupplierMiResource(
        NextSupplierMiService nextSupplierMiService,
        NextSupplierMiRepository nextSupplierMiRepository,
        NextSupplierMiQueryService nextSupplierMiQueryService
    ) {
        this.nextSupplierMiService = nextSupplierMiService;
        this.nextSupplierMiRepository = nextSupplierMiRepository;
        this.nextSupplierMiQueryService = nextSupplierMiQueryService;
    }

    /**
     * {@code POST  /next-supplier-mis} : Create a new nextSupplierMi.
     *
     * @param nextSupplierMiDTO the nextSupplierMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierMiDTO, or with status {@code 400 (Bad Request)} if the nextSupplierMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierMiDTO> createNextSupplierMi(@Valid @RequestBody NextSupplierMiDTO nextSupplierMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierMi : {}", nextSupplierMiDTO);
        if (nextSupplierMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierMiDTO = nextSupplierMiService.save(nextSupplierMiDTO);
        return ResponseEntity.created(new URI("/api/next-supplier-mis/" + nextSupplierMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierMiDTO.getId().toString()))
            .body(nextSupplierMiDTO);
    }

    /**
     * {@code PUT  /next-supplier-mis/:id} : Updates an existing nextSupplierMi.
     *
     * @param id the id of the nextSupplierMiDTO to save.
     * @param nextSupplierMiDTO the nextSupplierMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierMiDTO> updateNextSupplierMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierMiDTO nextSupplierMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierMi : {}, {}", id, nextSupplierMiDTO);
        if (nextSupplierMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierMiDTO = nextSupplierMiService.update(nextSupplierMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierMiDTO.getId().toString()))
            .body(nextSupplierMiDTO);
    }

    /**
     * {@code PATCH  /next-supplier-mis/:id} : Partial updates given fields of an existing nextSupplierMi, field will ignore if it is null
     *
     * @param id the id of the nextSupplierMiDTO to save.
     * @param nextSupplierMiDTO the nextSupplierMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierMiDTO> partialUpdateNextSupplierMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierMiDTO nextSupplierMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierMi partially : {}, {}", id, nextSupplierMiDTO);
        if (nextSupplierMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierMiDTO> result = nextSupplierMiService.partialUpdate(nextSupplierMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-mis} : get all the nextSupplierMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierMiDTO>> getAllNextSupplierMis(
        NextSupplierMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierMis by criteria: {}", criteria);

        Page<NextSupplierMiDTO> page = nextSupplierMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-mis/count} : count all the nextSupplierMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierMis(NextSupplierMiCriteria criteria) {
        LOG.debug("REST request to count NextSupplierMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-mis/:id} : get the "id" nextSupplierMi.
     *
     * @param id the id of the nextSupplierMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierMiDTO> getNextSupplierMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierMi : {}", id);
        Optional<NextSupplierMiDTO> nextSupplierMiDTO = nextSupplierMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierMiDTO);
    }

    /**
     * {@code DELETE  /next-supplier-mis/:id} : delete the "id" nextSupplierMi.
     *
     * @param id the id of the nextSupplierMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierMi : {}", id);
        nextSupplierMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

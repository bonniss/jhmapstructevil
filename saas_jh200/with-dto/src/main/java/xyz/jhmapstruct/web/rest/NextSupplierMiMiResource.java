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
import xyz.jhmapstruct.repository.NextSupplierMiMiRepository;
import xyz.jhmapstruct.service.NextSupplierMiMiQueryService;
import xyz.jhmapstruct.service.NextSupplierMiMiService;
import xyz.jhmapstruct.service.criteria.NextSupplierMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierMiMi}.
 */
@RestController
@RequestMapping("/api/next-supplier-mi-mis")
public class NextSupplierMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierMiMiResource.class);

    private static final String ENTITY_NAME = "nextSupplierMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierMiMiService nextSupplierMiMiService;

    private final NextSupplierMiMiRepository nextSupplierMiMiRepository;

    private final NextSupplierMiMiQueryService nextSupplierMiMiQueryService;

    public NextSupplierMiMiResource(
        NextSupplierMiMiService nextSupplierMiMiService,
        NextSupplierMiMiRepository nextSupplierMiMiRepository,
        NextSupplierMiMiQueryService nextSupplierMiMiQueryService
    ) {
        this.nextSupplierMiMiService = nextSupplierMiMiService;
        this.nextSupplierMiMiRepository = nextSupplierMiMiRepository;
        this.nextSupplierMiMiQueryService = nextSupplierMiMiQueryService;
    }

    /**
     * {@code POST  /next-supplier-mi-mis} : Create a new nextSupplierMiMi.
     *
     * @param nextSupplierMiMiDTO the nextSupplierMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierMiMiDTO, or with status {@code 400 (Bad Request)} if the nextSupplierMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierMiMiDTO> createNextSupplierMiMi(@Valid @RequestBody NextSupplierMiMiDTO nextSupplierMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierMiMi : {}", nextSupplierMiMiDTO);
        if (nextSupplierMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierMiMiDTO = nextSupplierMiMiService.save(nextSupplierMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-supplier-mi-mis/" + nextSupplierMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierMiMiDTO.getId().toString()))
            .body(nextSupplierMiMiDTO);
    }

    /**
     * {@code PUT  /next-supplier-mi-mis/:id} : Updates an existing nextSupplierMiMi.
     *
     * @param id the id of the nextSupplierMiMiDTO to save.
     * @param nextSupplierMiMiDTO the nextSupplierMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierMiMiDTO> updateNextSupplierMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierMiMiDTO nextSupplierMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierMiMi : {}, {}", id, nextSupplierMiMiDTO);
        if (nextSupplierMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierMiMiDTO = nextSupplierMiMiService.update(nextSupplierMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierMiMiDTO.getId().toString()))
            .body(nextSupplierMiMiDTO);
    }

    /**
     * {@code PATCH  /next-supplier-mi-mis/:id} : Partial updates given fields of an existing nextSupplierMiMi, field will ignore if it is null
     *
     * @param id the id of the nextSupplierMiMiDTO to save.
     * @param nextSupplierMiMiDTO the nextSupplierMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierMiMiDTO> partialUpdateNextSupplierMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierMiMiDTO nextSupplierMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierMiMi partially : {}, {}", id, nextSupplierMiMiDTO);
        if (nextSupplierMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierMiMiDTO> result = nextSupplierMiMiService.partialUpdate(nextSupplierMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-mi-mis} : get all the nextSupplierMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierMiMiDTO>> getAllNextSupplierMiMis(
        NextSupplierMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierMiMis by criteria: {}", criteria);

        Page<NextSupplierMiMiDTO> page = nextSupplierMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-mi-mis/count} : count all the nextSupplierMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierMiMis(NextSupplierMiMiCriteria criteria) {
        LOG.debug("REST request to count NextSupplierMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-mi-mis/:id} : get the "id" nextSupplierMiMi.
     *
     * @param id the id of the nextSupplierMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierMiMiDTO> getNextSupplierMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierMiMi : {}", id);
        Optional<NextSupplierMiMiDTO> nextSupplierMiMiDTO = nextSupplierMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierMiMiDTO);
    }

    /**
     * {@code DELETE  /next-supplier-mi-mis/:id} : delete the "id" nextSupplierMiMi.
     *
     * @param id the id of the nextSupplierMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierMiMi : {}", id);
        nextSupplierMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

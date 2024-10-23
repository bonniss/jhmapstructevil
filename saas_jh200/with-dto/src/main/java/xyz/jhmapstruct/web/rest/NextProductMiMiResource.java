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
import xyz.jhmapstruct.repository.NextProductMiMiRepository;
import xyz.jhmapstruct.service.NextProductMiMiQueryService;
import xyz.jhmapstruct.service.NextProductMiMiService;
import xyz.jhmapstruct.service.criteria.NextProductMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextProductMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductMiMi}.
 */
@RestController
@RequestMapping("/api/next-product-mi-mis")
public class NextProductMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiMiResource.class);

    private static final String ENTITY_NAME = "nextProductMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductMiMiService nextProductMiMiService;

    private final NextProductMiMiRepository nextProductMiMiRepository;

    private final NextProductMiMiQueryService nextProductMiMiQueryService;

    public NextProductMiMiResource(
        NextProductMiMiService nextProductMiMiService,
        NextProductMiMiRepository nextProductMiMiRepository,
        NextProductMiMiQueryService nextProductMiMiQueryService
    ) {
        this.nextProductMiMiService = nextProductMiMiService;
        this.nextProductMiMiRepository = nextProductMiMiRepository;
        this.nextProductMiMiQueryService = nextProductMiMiQueryService;
    }

    /**
     * {@code POST  /next-product-mi-mis} : Create a new nextProductMiMi.
     *
     * @param nextProductMiMiDTO the nextProductMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductMiMiDTO, or with status {@code 400 (Bad Request)} if the nextProductMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductMiMiDTO> createNextProductMiMi(@Valid @RequestBody NextProductMiMiDTO nextProductMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductMiMi : {}", nextProductMiMiDTO);
        if (nextProductMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductMiMiDTO = nextProductMiMiService.save(nextProductMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-product-mi-mis/" + nextProductMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductMiMiDTO.getId().toString()))
            .body(nextProductMiMiDTO);
    }

    /**
     * {@code PUT  /next-product-mi-mis/:id} : Updates an existing nextProductMiMi.
     *
     * @param id the id of the nextProductMiMiDTO to save.
     * @param nextProductMiMiDTO the nextProductMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductMiMiDTO> updateNextProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductMiMiDTO nextProductMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductMiMi : {}, {}", id, nextProductMiMiDTO);
        if (nextProductMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductMiMiDTO = nextProductMiMiService.update(nextProductMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductMiMiDTO.getId().toString()))
            .body(nextProductMiMiDTO);
    }

    /**
     * {@code PATCH  /next-product-mi-mis/:id} : Partial updates given fields of an existing nextProductMiMi, field will ignore if it is null
     *
     * @param id the id of the nextProductMiMiDTO to save.
     * @param nextProductMiMiDTO the nextProductMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductMiMiDTO> partialUpdateNextProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductMiMiDTO nextProductMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductMiMi partially : {}, {}", id, nextProductMiMiDTO);
        if (nextProductMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductMiMiDTO> result = nextProductMiMiService.partialUpdate(nextProductMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-mi-mis} : get all the nextProductMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductMiMiDTO>> getAllNextProductMiMis(
        NextProductMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductMiMis by criteria: {}", criteria);

        Page<NextProductMiMiDTO> page = nextProductMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-mi-mis/count} : count all the nextProductMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductMiMis(NextProductMiMiCriteria criteria) {
        LOG.debug("REST request to count NextProductMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-mi-mis/:id} : get the "id" nextProductMiMi.
     *
     * @param id the id of the nextProductMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductMiMiDTO> getNextProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductMiMi : {}", id);
        Optional<NextProductMiMiDTO> nextProductMiMiDTO = nextProductMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductMiMiDTO);
    }

    /**
     * {@code DELETE  /next-product-mi-mis/:id} : delete the "id" nextProductMiMi.
     *
     * @param id the id of the nextProductMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductMiMi : {}", id);
        nextProductMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

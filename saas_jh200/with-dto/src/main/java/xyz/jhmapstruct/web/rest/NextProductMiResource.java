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
import xyz.jhmapstruct.repository.NextProductMiRepository;
import xyz.jhmapstruct.service.NextProductMiQueryService;
import xyz.jhmapstruct.service.NextProductMiService;
import xyz.jhmapstruct.service.criteria.NextProductMiCriteria;
import xyz.jhmapstruct.service.dto.NextProductMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductMi}.
 */
@RestController
@RequestMapping("/api/next-product-mis")
public class NextProductMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductMiResource.class);

    private static final String ENTITY_NAME = "nextProductMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductMiService nextProductMiService;

    private final NextProductMiRepository nextProductMiRepository;

    private final NextProductMiQueryService nextProductMiQueryService;

    public NextProductMiResource(
        NextProductMiService nextProductMiService,
        NextProductMiRepository nextProductMiRepository,
        NextProductMiQueryService nextProductMiQueryService
    ) {
        this.nextProductMiService = nextProductMiService;
        this.nextProductMiRepository = nextProductMiRepository;
        this.nextProductMiQueryService = nextProductMiQueryService;
    }

    /**
     * {@code POST  /next-product-mis} : Create a new nextProductMi.
     *
     * @param nextProductMiDTO the nextProductMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductMiDTO, or with status {@code 400 (Bad Request)} if the nextProductMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductMiDTO> createNextProductMi(@Valid @RequestBody NextProductMiDTO nextProductMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductMi : {}", nextProductMiDTO);
        if (nextProductMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductMiDTO = nextProductMiService.save(nextProductMiDTO);
        return ResponseEntity.created(new URI("/api/next-product-mis/" + nextProductMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductMiDTO.getId().toString()))
            .body(nextProductMiDTO);
    }

    /**
     * {@code PUT  /next-product-mis/:id} : Updates an existing nextProductMi.
     *
     * @param id the id of the nextProductMiDTO to save.
     * @param nextProductMiDTO the nextProductMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductMiDTO> updateNextProductMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductMiDTO nextProductMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductMi : {}, {}", id, nextProductMiDTO);
        if (nextProductMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductMiDTO = nextProductMiService.update(nextProductMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductMiDTO.getId().toString()))
            .body(nextProductMiDTO);
    }

    /**
     * {@code PATCH  /next-product-mis/:id} : Partial updates given fields of an existing nextProductMi, field will ignore if it is null
     *
     * @param id the id of the nextProductMiDTO to save.
     * @param nextProductMiDTO the nextProductMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductMiDTO> partialUpdateNextProductMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductMiDTO nextProductMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductMi partially : {}, {}", id, nextProductMiDTO);
        if (nextProductMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductMiDTO> result = nextProductMiService.partialUpdate(nextProductMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-mis} : get all the nextProductMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductMiDTO>> getAllNextProductMis(
        NextProductMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductMis by criteria: {}", criteria);

        Page<NextProductMiDTO> page = nextProductMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-mis/count} : count all the nextProductMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductMis(NextProductMiCriteria criteria) {
        LOG.debug("REST request to count NextProductMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-mis/:id} : get the "id" nextProductMi.
     *
     * @param id the id of the nextProductMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductMiDTO> getNextProductMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductMi : {}", id);
        Optional<NextProductMiDTO> nextProductMiDTO = nextProductMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductMiDTO);
    }

    /**
     * {@code DELETE  /next-product-mis/:id} : delete the "id" nextProductMi.
     *
     * @param id the id of the nextProductMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductMi : {}", id);
        nextProductMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
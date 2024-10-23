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
import xyz.jhmapstruct.repository.NextCategoryMiMiRepository;
import xyz.jhmapstruct.service.NextCategoryMiMiQueryService;
import xyz.jhmapstruct.service.NextCategoryMiMiService;
import xyz.jhmapstruct.service.criteria.NextCategoryMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryMiMi}.
 */
@RestController
@RequestMapping("/api/next-category-mi-mis")
public class NextCategoryMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiMiResource.class);

    private static final String ENTITY_NAME = "nextCategoryMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryMiMiService nextCategoryMiMiService;

    private final NextCategoryMiMiRepository nextCategoryMiMiRepository;

    private final NextCategoryMiMiQueryService nextCategoryMiMiQueryService;

    public NextCategoryMiMiResource(
        NextCategoryMiMiService nextCategoryMiMiService,
        NextCategoryMiMiRepository nextCategoryMiMiRepository,
        NextCategoryMiMiQueryService nextCategoryMiMiQueryService
    ) {
        this.nextCategoryMiMiService = nextCategoryMiMiService;
        this.nextCategoryMiMiRepository = nextCategoryMiMiRepository;
        this.nextCategoryMiMiQueryService = nextCategoryMiMiQueryService;
    }

    /**
     * {@code POST  /next-category-mi-mis} : Create a new nextCategoryMiMi.
     *
     * @param nextCategoryMiMiDTO the nextCategoryMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryMiMiDTO, or with status {@code 400 (Bad Request)} if the nextCategoryMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryMiMiDTO> createNextCategoryMiMi(@Valid @RequestBody NextCategoryMiMiDTO nextCategoryMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryMiMi : {}", nextCategoryMiMiDTO);
        if (nextCategoryMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryMiMiDTO = nextCategoryMiMiService.save(nextCategoryMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-category-mi-mis/" + nextCategoryMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryMiMiDTO.getId().toString()))
            .body(nextCategoryMiMiDTO);
    }

    /**
     * {@code PUT  /next-category-mi-mis/:id} : Updates an existing nextCategoryMiMi.
     *
     * @param id the id of the nextCategoryMiMiDTO to save.
     * @param nextCategoryMiMiDTO the nextCategoryMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryMiMiDTO> updateNextCategoryMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryMiMiDTO nextCategoryMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryMiMi : {}, {}", id, nextCategoryMiMiDTO);
        if (nextCategoryMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryMiMiDTO = nextCategoryMiMiService.update(nextCategoryMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryMiMiDTO.getId().toString()))
            .body(nextCategoryMiMiDTO);
    }

    /**
     * {@code PATCH  /next-category-mi-mis/:id} : Partial updates given fields of an existing nextCategoryMiMi, field will ignore if it is null
     *
     * @param id the id of the nextCategoryMiMiDTO to save.
     * @param nextCategoryMiMiDTO the nextCategoryMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryMiMiDTO> partialUpdateNextCategoryMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryMiMiDTO nextCategoryMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryMiMi partially : {}, {}", id, nextCategoryMiMiDTO);
        if (nextCategoryMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryMiMiDTO> result = nextCategoryMiMiService.partialUpdate(nextCategoryMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-mi-mis} : get all the nextCategoryMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryMiMiDTO>> getAllNextCategoryMiMis(
        NextCategoryMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryMiMis by criteria: {}", criteria);

        Page<NextCategoryMiMiDTO> page = nextCategoryMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-mi-mis/count} : count all the nextCategoryMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryMiMis(NextCategoryMiMiCriteria criteria) {
        LOG.debug("REST request to count NextCategoryMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-mi-mis/:id} : get the "id" nextCategoryMiMi.
     *
     * @param id the id of the nextCategoryMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryMiMiDTO> getNextCategoryMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryMiMi : {}", id);
        Optional<NextCategoryMiMiDTO> nextCategoryMiMiDTO = nextCategoryMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryMiMiDTO);
    }

    /**
     * {@code DELETE  /next-category-mi-mis/:id} : delete the "id" nextCategoryMiMi.
     *
     * @param id the id of the nextCategoryMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryMiMi : {}", id);
        nextCategoryMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

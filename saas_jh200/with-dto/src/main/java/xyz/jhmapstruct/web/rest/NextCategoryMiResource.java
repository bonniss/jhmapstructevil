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
import xyz.jhmapstruct.repository.NextCategoryMiRepository;
import xyz.jhmapstruct.service.NextCategoryMiQueryService;
import xyz.jhmapstruct.service.NextCategoryMiService;
import xyz.jhmapstruct.service.criteria.NextCategoryMiCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryMi}.
 */
@RestController
@RequestMapping("/api/next-category-mis")
public class NextCategoryMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryMiResource.class);

    private static final String ENTITY_NAME = "nextCategoryMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryMiService nextCategoryMiService;

    private final NextCategoryMiRepository nextCategoryMiRepository;

    private final NextCategoryMiQueryService nextCategoryMiQueryService;

    public NextCategoryMiResource(
        NextCategoryMiService nextCategoryMiService,
        NextCategoryMiRepository nextCategoryMiRepository,
        NextCategoryMiQueryService nextCategoryMiQueryService
    ) {
        this.nextCategoryMiService = nextCategoryMiService;
        this.nextCategoryMiRepository = nextCategoryMiRepository;
        this.nextCategoryMiQueryService = nextCategoryMiQueryService;
    }

    /**
     * {@code POST  /next-category-mis} : Create a new nextCategoryMi.
     *
     * @param nextCategoryMiDTO the nextCategoryMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryMiDTO, or with status {@code 400 (Bad Request)} if the nextCategoryMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryMiDTO> createNextCategoryMi(@Valid @RequestBody NextCategoryMiDTO nextCategoryMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryMi : {}", nextCategoryMiDTO);
        if (nextCategoryMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryMiDTO = nextCategoryMiService.save(nextCategoryMiDTO);
        return ResponseEntity.created(new URI("/api/next-category-mis/" + nextCategoryMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryMiDTO.getId().toString()))
            .body(nextCategoryMiDTO);
    }

    /**
     * {@code PUT  /next-category-mis/:id} : Updates an existing nextCategoryMi.
     *
     * @param id the id of the nextCategoryMiDTO to save.
     * @param nextCategoryMiDTO the nextCategoryMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryMiDTO> updateNextCategoryMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryMiDTO nextCategoryMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryMi : {}, {}", id, nextCategoryMiDTO);
        if (nextCategoryMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryMiDTO = nextCategoryMiService.update(nextCategoryMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryMiDTO.getId().toString()))
            .body(nextCategoryMiDTO);
    }

    /**
     * {@code PATCH  /next-category-mis/:id} : Partial updates given fields of an existing nextCategoryMi, field will ignore if it is null
     *
     * @param id the id of the nextCategoryMiDTO to save.
     * @param nextCategoryMiDTO the nextCategoryMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryMiDTO> partialUpdateNextCategoryMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryMiDTO nextCategoryMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryMi partially : {}, {}", id, nextCategoryMiDTO);
        if (nextCategoryMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryMiDTO> result = nextCategoryMiService.partialUpdate(nextCategoryMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-mis} : get all the nextCategoryMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryMiDTO>> getAllNextCategoryMis(
        NextCategoryMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryMis by criteria: {}", criteria);

        Page<NextCategoryMiDTO> page = nextCategoryMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-mis/count} : count all the nextCategoryMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryMis(NextCategoryMiCriteria criteria) {
        LOG.debug("REST request to count NextCategoryMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-mis/:id} : get the "id" nextCategoryMi.
     *
     * @param id the id of the nextCategoryMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryMiDTO> getNextCategoryMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryMi : {}", id);
        Optional<NextCategoryMiDTO> nextCategoryMiDTO = nextCategoryMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryMiDTO);
    }

    /**
     * {@code DELETE  /next-category-mis/:id} : delete the "id" nextCategoryMi.
     *
     * @param id the id of the nextCategoryMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryMi : {}", id);
        nextCategoryMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

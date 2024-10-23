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
import xyz.jhmapstruct.repository.NextCategoryThetaRepository;
import xyz.jhmapstruct.service.NextCategoryThetaQueryService;
import xyz.jhmapstruct.service.NextCategoryThetaService;
import xyz.jhmapstruct.service.criteria.NextCategoryThetaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryTheta}.
 */
@RestController
@RequestMapping("/api/next-category-thetas")
public class NextCategoryThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryThetaResource.class);

    private static final String ENTITY_NAME = "nextCategoryTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryThetaService nextCategoryThetaService;

    private final NextCategoryThetaRepository nextCategoryThetaRepository;

    private final NextCategoryThetaQueryService nextCategoryThetaQueryService;

    public NextCategoryThetaResource(
        NextCategoryThetaService nextCategoryThetaService,
        NextCategoryThetaRepository nextCategoryThetaRepository,
        NextCategoryThetaQueryService nextCategoryThetaQueryService
    ) {
        this.nextCategoryThetaService = nextCategoryThetaService;
        this.nextCategoryThetaRepository = nextCategoryThetaRepository;
        this.nextCategoryThetaQueryService = nextCategoryThetaQueryService;
    }

    /**
     * {@code POST  /next-category-thetas} : Create a new nextCategoryTheta.
     *
     * @param nextCategoryThetaDTO the nextCategoryThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryThetaDTO, or with status {@code 400 (Bad Request)} if the nextCategoryTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryThetaDTO> createNextCategoryTheta(@Valid @RequestBody NextCategoryThetaDTO nextCategoryThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryTheta : {}", nextCategoryThetaDTO);
        if (nextCategoryThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryThetaDTO = nextCategoryThetaService.save(nextCategoryThetaDTO);
        return ResponseEntity.created(new URI("/api/next-category-thetas/" + nextCategoryThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryThetaDTO.getId().toString()))
            .body(nextCategoryThetaDTO);
    }

    /**
     * {@code PUT  /next-category-thetas/:id} : Updates an existing nextCategoryTheta.
     *
     * @param id the id of the nextCategoryThetaDTO to save.
     * @param nextCategoryThetaDTO the nextCategoryThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryThetaDTO> updateNextCategoryTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryThetaDTO nextCategoryThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryTheta : {}, {}", id, nextCategoryThetaDTO);
        if (nextCategoryThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryThetaDTO = nextCategoryThetaService.update(nextCategoryThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryThetaDTO.getId().toString()))
            .body(nextCategoryThetaDTO);
    }

    /**
     * {@code PATCH  /next-category-thetas/:id} : Partial updates given fields of an existing nextCategoryTheta, field will ignore if it is null
     *
     * @param id the id of the nextCategoryThetaDTO to save.
     * @param nextCategoryThetaDTO the nextCategoryThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryThetaDTO> partialUpdateNextCategoryTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryThetaDTO nextCategoryThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryTheta partially : {}, {}", id, nextCategoryThetaDTO);
        if (nextCategoryThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryThetaDTO> result = nextCategoryThetaService.partialUpdate(nextCategoryThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-thetas} : get all the nextCategoryThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryThetaDTO>> getAllNextCategoryThetas(
        NextCategoryThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryThetas by criteria: {}", criteria);

        Page<NextCategoryThetaDTO> page = nextCategoryThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-thetas/count} : count all the nextCategoryThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryThetas(NextCategoryThetaCriteria criteria) {
        LOG.debug("REST request to count NextCategoryThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-thetas/:id} : get the "id" nextCategoryTheta.
     *
     * @param id the id of the nextCategoryThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryThetaDTO> getNextCategoryTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryTheta : {}", id);
        Optional<NextCategoryThetaDTO> nextCategoryThetaDTO = nextCategoryThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryThetaDTO);
    }

    /**
     * {@code DELETE  /next-category-thetas/:id} : delete the "id" nextCategoryTheta.
     *
     * @param id the id of the nextCategoryThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryTheta : {}", id);
        nextCategoryThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

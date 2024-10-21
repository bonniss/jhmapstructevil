package ai.realworld.web.rest;

import ai.realworld.repository.AlPacinoAndreiRightHandRepository;
import ai.realworld.service.AlPacinoAndreiRightHandQueryService;
import ai.realworld.service.AlPacinoAndreiRightHandService;
import ai.realworld.service.criteria.AlPacinoAndreiRightHandCriteria;
import ai.realworld.service.dto.AlPacinoAndreiRightHandDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AlPacinoAndreiRightHand}.
 */
@RestController
@RequestMapping("/api/al-pacino-andrei-right-hands")
public class AlPacinoAndreiRightHandResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandResource.class);

    private static final String ENTITY_NAME = "alPacinoAndreiRightHand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoAndreiRightHandService alPacinoAndreiRightHandService;

    private final AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository;

    private final AlPacinoAndreiRightHandQueryService alPacinoAndreiRightHandQueryService;

    public AlPacinoAndreiRightHandResource(
        AlPacinoAndreiRightHandService alPacinoAndreiRightHandService,
        AlPacinoAndreiRightHandRepository alPacinoAndreiRightHandRepository,
        AlPacinoAndreiRightHandQueryService alPacinoAndreiRightHandQueryService
    ) {
        this.alPacinoAndreiRightHandService = alPacinoAndreiRightHandService;
        this.alPacinoAndreiRightHandRepository = alPacinoAndreiRightHandRepository;
        this.alPacinoAndreiRightHandQueryService = alPacinoAndreiRightHandQueryService;
    }

    /**
     * {@code POST  /al-pacino-andrei-right-hands} : Create a new alPacinoAndreiRightHand.
     *
     * @param alPacinoAndreiRightHandDTO the alPacinoAndreiRightHandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoAndreiRightHandDTO, or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoAndreiRightHandDTO> createAlPacinoAndreiRightHand(
        @Valid @RequestBody AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoAndreiRightHand : {}", alPacinoAndreiRightHandDTO);
        if (alPacinoAndreiRightHandDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoAndreiRightHand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandService.save(alPacinoAndreiRightHandDTO);
        return ResponseEntity.created(new URI("/api/al-pacino-andrei-right-hands/" + alPacinoAndreiRightHandDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandDTO.getId().toString())
            )
            .body(alPacinoAndreiRightHandDTO);
    }

    /**
     * {@code PUT  /al-pacino-andrei-right-hands/:id} : Updates an existing alPacinoAndreiRightHand.
     *
     * @param id the id of the alPacinoAndreiRightHandDTO to save.
     * @param alPacinoAndreiRightHandDTO the alPacinoAndreiRightHandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoAndreiRightHandDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoAndreiRightHandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoAndreiRightHandDTO> updateAlPacinoAndreiRightHand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoAndreiRightHand : {}, {}", id, alPacinoAndreiRightHandDTO);
        if (alPacinoAndreiRightHandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoAndreiRightHandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoAndreiRightHandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandService.update(alPacinoAndreiRightHandDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandDTO.getId().toString()))
            .body(alPacinoAndreiRightHandDTO);
    }

    /**
     * {@code PATCH  /al-pacino-andrei-right-hands/:id} : Partial updates given fields of an existing alPacinoAndreiRightHand, field will ignore if it is null
     *
     * @param id the id of the alPacinoAndreiRightHandDTO to save.
     * @param alPacinoAndreiRightHandDTO the alPacinoAndreiRightHandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoAndreiRightHandDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoAndreiRightHandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoAndreiRightHandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoAndreiRightHandDTO> partialUpdateAlPacinoAndreiRightHand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPacinoAndreiRightHandDTO alPacinoAndreiRightHandDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoAndreiRightHand partially : {}, {}", id, alPacinoAndreiRightHandDTO);
        if (alPacinoAndreiRightHandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoAndreiRightHandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoAndreiRightHandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoAndreiRightHandDTO> result = alPacinoAndreiRightHandService.partialUpdate(alPacinoAndreiRightHandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hands} : get all the alPacinoAndreiRightHands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoAndreiRightHands in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoAndreiRightHandDTO>> getAllAlPacinoAndreiRightHands(
        AlPacinoAndreiRightHandCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoAndreiRightHands by criteria: {}", criteria);

        Page<AlPacinoAndreiRightHandDTO> page = alPacinoAndreiRightHandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hands/count} : count all the alPacinoAndreiRightHands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoAndreiRightHands(AlPacinoAndreiRightHandCriteria criteria) {
        LOG.debug("REST request to count AlPacinoAndreiRightHands by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoAndreiRightHandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hands/:id} : get the "id" alPacinoAndreiRightHand.
     *
     * @param id the id of the alPacinoAndreiRightHandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoAndreiRightHandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoAndreiRightHandDTO> getAlPacinoAndreiRightHand(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPacinoAndreiRightHand : {}", id);
        Optional<AlPacinoAndreiRightHandDTO> alPacinoAndreiRightHandDTO = alPacinoAndreiRightHandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoAndreiRightHandDTO);
    }

    /**
     * {@code DELETE  /al-pacino-andrei-right-hands/:id} : delete the "id" alPacinoAndreiRightHand.
     *
     * @param id the id of the alPacinoAndreiRightHandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoAndreiRightHand(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPacinoAndreiRightHand : {}", id);
        alPacinoAndreiRightHandService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

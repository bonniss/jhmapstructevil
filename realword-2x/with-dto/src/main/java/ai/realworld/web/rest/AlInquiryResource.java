package ai.realworld.web.rest;

import ai.realworld.repository.AlInquiryRepository;
import ai.realworld.service.AlInquiryQueryService;
import ai.realworld.service.AlInquiryService;
import ai.realworld.service.criteria.AlInquiryCriteria;
import ai.realworld.service.dto.AlInquiryDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link ai.realworld.domain.AlInquiry}.
 */
@RestController
@RequestMapping("/api/al-inquiries")
public class AlInquiryResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryResource.class);

    private static final String ENTITY_NAME = "alInquiry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlInquiryService alInquiryService;

    private final AlInquiryRepository alInquiryRepository;

    private final AlInquiryQueryService alInquiryQueryService;

    public AlInquiryResource(
        AlInquiryService alInquiryService,
        AlInquiryRepository alInquiryRepository,
        AlInquiryQueryService alInquiryQueryService
    ) {
        this.alInquiryService = alInquiryService;
        this.alInquiryRepository = alInquiryRepository;
        this.alInquiryQueryService = alInquiryQueryService;
    }

    /**
     * {@code POST  /al-inquiries} : Create a new alInquiry.
     *
     * @param alInquiryDTO the alInquiryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alInquiryDTO, or with status {@code 400 (Bad Request)} if the alInquiry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlInquiryDTO> createAlInquiry(@Valid @RequestBody AlInquiryDTO alInquiryDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlInquiry : {}", alInquiryDTO);
        if (alInquiryDTO.getId() != null) {
            throw new BadRequestAlertException("A new alInquiry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alInquiryDTO = alInquiryService.save(alInquiryDTO);
        return ResponseEntity.created(new URI("/api/al-inquiries/" + alInquiryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alInquiryDTO.getId().toString()))
            .body(alInquiryDTO);
    }

    /**
     * {@code PUT  /al-inquiries/:id} : Updates an existing alInquiry.
     *
     * @param id the id of the alInquiryDTO to save.
     * @param alInquiryDTO the alInquiryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alInquiryDTO,
     * or with status {@code 400 (Bad Request)} if the alInquiryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alInquiryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlInquiryDTO> updateAlInquiry(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlInquiryDTO alInquiryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlInquiry : {}, {}", id, alInquiryDTO);
        if (alInquiryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alInquiryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alInquiryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alInquiryDTO = alInquiryService.update(alInquiryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alInquiryDTO.getId().toString()))
            .body(alInquiryDTO);
    }

    /**
     * {@code PATCH  /al-inquiries/:id} : Partial updates given fields of an existing alInquiry, field will ignore if it is null
     *
     * @param id the id of the alInquiryDTO to save.
     * @param alInquiryDTO the alInquiryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alInquiryDTO,
     * or with status {@code 400 (Bad Request)} if the alInquiryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alInquiryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alInquiryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlInquiryDTO> partialUpdateAlInquiry(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlInquiryDTO alInquiryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlInquiry partially : {}, {}", id, alInquiryDTO);
        if (alInquiryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alInquiryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alInquiryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlInquiryDTO> result = alInquiryService.partialUpdate(alInquiryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alInquiryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-inquiries} : get all the alInquiries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alInquiries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlInquiryDTO>> getAllAlInquiries(
        AlInquiryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlInquiries by criteria: {}", criteria);

        Page<AlInquiryDTO> page = alInquiryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-inquiries/count} : count all the alInquiries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlInquiries(AlInquiryCriteria criteria) {
        LOG.debug("REST request to count AlInquiries by criteria: {}", criteria);
        return ResponseEntity.ok().body(alInquiryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-inquiries/:id} : get the "id" alInquiry.
     *
     * @param id the id of the alInquiryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alInquiryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlInquiryDTO> getAlInquiry(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlInquiry : {}", id);
        Optional<AlInquiryDTO> alInquiryDTO = alInquiryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alInquiryDTO);
    }

    /**
     * {@code DELETE  /al-inquiries/:id} : delete the "id" alInquiry.
     *
     * @param id the id of the alInquiryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlInquiry(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlInquiry : {}", id);
        alInquiryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

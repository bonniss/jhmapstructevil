package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentViViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.repository.NextShipmentViViRepository;
import xyz.jhmapstruct.service.dto.NextShipmentViViDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentViViMapper;

/**
 * Integration tests for the {@link NextShipmentViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentViViResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentViViRepository nextShipmentViViRepository;

    @Autowired
    private NextShipmentViViMapper nextShipmentViViMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentViViMockMvc;

    private NextShipmentViVi nextShipmentViVi;

    private NextShipmentViVi insertedNextShipmentViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentViVi createEntity() {
        return new NextShipmentViVi()
            .trackingNumber(DEFAULT_TRACKING_NUMBER)
            .shippedDate(DEFAULT_SHIPPED_DATE)
            .deliveryDate(DEFAULT_DELIVERY_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentViVi createUpdatedEntity() {
        return new NextShipmentViVi()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentViVi != null) {
            nextShipmentViViRepository.delete(insertedNextShipmentViVi);
            insertedNextShipmentViVi = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);
        var returnedNextShipmentViViDTO = om.readValue(
            restNextShipmentViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentViViDTO.class
        );

        // Validate the NextShipmentViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentViVi = nextShipmentViViMapper.toEntity(returnedNextShipmentViViDTO);
        assertNextShipmentViViUpdatableFieldsEquals(returnedNextShipmentViVi, getPersistedNextShipmentViVi(returnedNextShipmentViVi));

        insertedNextShipmentViVi = returnedNextShipmentViVi;
    }

    @Test
    @Transactional
    void createNextShipmentViViWithExistingId() throws Exception {
        // Create the NextShipmentViVi with an existing ID
        nextShipmentViVi.setId(1L);
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentViVi.setTrackingNumber(null);

        // Create the NextShipmentViVi, which fails.
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        restNextShipmentViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentViVis() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList
        restNextShipmentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentViVi() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get the nextShipmentViVi
        restNextShipmentViViMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentViVi.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentViVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        Long id = nextShipmentViVi.getId();

        defaultNextShipmentViViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentViViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentViViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where trackingNumber equals to
        defaultNextShipmentViViFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where trackingNumber in
        defaultNextShipmentViViFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where trackingNumber is not null
        defaultNextShipmentViViFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where trackingNumber contains
        defaultNextShipmentViViFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where trackingNumber does not contain
        defaultNextShipmentViViFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where shippedDate equals to
        defaultNextShipmentViViFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where shippedDate in
        defaultNextShipmentViViFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where shippedDate is not null
        defaultNextShipmentViViFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where deliveryDate equals to
        defaultNextShipmentViViFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where deliveryDate in
        defaultNextShipmentViViFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        // Get all the nextShipmentViViList where deliveryDate is not null
        defaultNextShipmentViViFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentViVisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentViVi.setTenant(tenant);
        nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentViViList where tenant equals to tenantId
        defaultNextShipmentViViShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentViViList where tenant equals to (tenantId + 1)
        defaultNextShipmentViViShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentViViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentViViShouldBeFound(shouldBeFound);
        defaultNextShipmentViViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentViViShouldBeFound(String filter) throws Exception {
        restNextShipmentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentViViShouldNotBeFound(String filter) throws Exception {
        restNextShipmentViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentViViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentViVi() throws Exception {
        // Get the nextShipmentViVi
        restNextShipmentViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentViVi() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentViVi
        NextShipmentViVi updatedNextShipmentViVi = nextShipmentViViRepository.findById(nextShipmentViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentViVi are not directly saved in db
        em.detach(updatedNextShipmentViVi);
        updatedNextShipmentViVi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(updatedNextShipmentViVi);

        restNextShipmentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentViViToMatchAllProperties(updatedNextShipmentViVi);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentViVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentViVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentViVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentViVi using partial update
        NextShipmentViVi partialUpdatedNextShipmentViVi = new NextShipmentViVi();
        partialUpdatedNextShipmentViVi.setId(nextShipmentViVi.getId());

        partialUpdatedNextShipmentViVi.trackingNumber(UPDATED_TRACKING_NUMBER);

        restNextShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentViVi, nextShipmentViVi),
            getPersistedNextShipmentViVi(nextShipmentViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentViViWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentViVi using partial update
        NextShipmentViVi partialUpdatedNextShipmentViVi = new NextShipmentViVi();
        partialUpdatedNextShipmentViVi.setId(nextShipmentViVi.getId());

        partialUpdatedNextShipmentViVi
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentViVi))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentViViUpdatableFieldsEquals(
            partialUpdatedNextShipmentViVi,
            getPersistedNextShipmentViVi(partialUpdatedNextShipmentViVi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentViVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentViVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentViVi.setId(longCount.incrementAndGet());

        // Create the NextShipmentViVi
        NextShipmentViViDTO nextShipmentViViDTO = nextShipmentViViMapper.toDto(nextShipmentViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentViVi() throws Exception {
        // Initialize the database
        insertedNextShipmentViVi = nextShipmentViViRepository.saveAndFlush(nextShipmentViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentViVi
        restNextShipmentViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentViViRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected NextShipmentViVi getPersistedNextShipmentViVi(NextShipmentViVi nextShipmentViVi) {
        return nextShipmentViViRepository.findById(nextShipmentViVi.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentViViToMatchAllProperties(NextShipmentViVi expectedNextShipmentViVi) {
        assertNextShipmentViViAllPropertiesEquals(expectedNextShipmentViVi, getPersistedNextShipmentViVi(expectedNextShipmentViVi));
    }

    protected void assertPersistedNextShipmentViViToMatchUpdatableProperties(NextShipmentViVi expectedNextShipmentViVi) {
        assertNextShipmentViViAllUpdatablePropertiesEquals(
            expectedNextShipmentViVi,
            getPersistedNextShipmentViVi(expectedNextShipmentViVi)
        );
    }
}

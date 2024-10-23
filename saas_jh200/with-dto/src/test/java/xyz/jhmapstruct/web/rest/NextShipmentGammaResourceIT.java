package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentGammaAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentGamma;
import xyz.jhmapstruct.repository.NextShipmentGammaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentGammaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentGammaMapper;

/**
 * Integration tests for the {@link NextShipmentGammaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentGammaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-gammas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentGammaRepository nextShipmentGammaRepository;

    @Autowired
    private NextShipmentGammaMapper nextShipmentGammaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentGammaMockMvc;

    private NextShipmentGamma nextShipmentGamma;

    private NextShipmentGamma insertedNextShipmentGamma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentGamma createEntity() {
        return new NextShipmentGamma()
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
    public static NextShipmentGamma createUpdatedEntity() {
        return new NextShipmentGamma()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentGamma = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentGamma != null) {
            nextShipmentGammaRepository.delete(insertedNextShipmentGamma);
            insertedNextShipmentGamma = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentGamma() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);
        var returnedNextShipmentGammaDTO = om.readValue(
            restNextShipmentGammaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentGammaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentGammaDTO.class
        );

        // Validate the NextShipmentGamma in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentGamma = nextShipmentGammaMapper.toEntity(returnedNextShipmentGammaDTO);
        assertNextShipmentGammaUpdatableFieldsEquals(returnedNextShipmentGamma, getPersistedNextShipmentGamma(returnedNextShipmentGamma));

        insertedNextShipmentGamma = returnedNextShipmentGamma;
    }

    @Test
    @Transactional
    void createNextShipmentGammaWithExistingId() throws Exception {
        // Create the NextShipmentGamma with an existing ID
        nextShipmentGamma.setId(1L);
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentGammaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentGamma.setTrackingNumber(null);

        // Create the NextShipmentGamma, which fails.
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        restNextShipmentGammaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentGammaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentGammas() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList
        restNextShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentGamma() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get the nextShipmentGamma
        restNextShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentGamma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentGamma.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentGammasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        Long id = nextShipmentGamma.getId();

        defaultNextShipmentGammaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentGammaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentGammaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where trackingNumber equals to
        defaultNextShipmentGammaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where trackingNumber in
        defaultNextShipmentGammaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where trackingNumber is not null
        defaultNextShipmentGammaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where trackingNumber contains
        defaultNextShipmentGammaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where trackingNumber does not contain
        defaultNextShipmentGammaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where shippedDate equals to
        defaultNextShipmentGammaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where shippedDate in
        defaultNextShipmentGammaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where shippedDate is not null
        defaultNextShipmentGammaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where deliveryDate equals to
        defaultNextShipmentGammaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where deliveryDate in
        defaultNextShipmentGammaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        // Get all the nextShipmentGammaList where deliveryDate is not null
        defaultNextShipmentGammaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentGammasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentGamma.setTenant(tenant);
        nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentGammaList where tenant equals to tenantId
        defaultNextShipmentGammaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentGammaList where tenant equals to (tenantId + 1)
        defaultNextShipmentGammaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentGammaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentGammaShouldBeFound(shouldBeFound);
        defaultNextShipmentGammaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentGammaShouldBeFound(String filter) throws Exception {
        restNextShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentGamma.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentGammaShouldNotBeFound(String filter) throws Exception {
        restNextShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentGammaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentGamma() throws Exception {
        // Get the nextShipmentGamma
        restNextShipmentGammaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentGamma() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentGamma
        NextShipmentGamma updatedNextShipmentGamma = nextShipmentGammaRepository.findById(nextShipmentGamma.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentGamma are not directly saved in db
        em.detach(updatedNextShipmentGamma);
        updatedNextShipmentGamma
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(updatedNextShipmentGamma);

        restNextShipmentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentGammaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentGammaToMatchAllProperties(updatedNextShipmentGamma);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentGamma.setId(longCount.incrementAndGet());

        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentGammaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentGamma.setId(longCount.incrementAndGet());

        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentGammaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentGamma.setId(longCount.incrementAndGet());

        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentGammaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentGamma using partial update
        NextShipmentGamma partialUpdatedNextShipmentGamma = new NextShipmentGamma();
        partialUpdatedNextShipmentGamma.setId(nextShipmentGamma.getId());

        partialUpdatedNextShipmentGamma.shippedDate(UPDATED_SHIPPED_DATE);

        restNextShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentGammaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentGamma, nextShipmentGamma),
            getPersistedNextShipmentGamma(nextShipmentGamma)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentGammaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentGamma using partial update
        NextShipmentGamma partialUpdatedNextShipmentGamma = new NextShipmentGamma();
        partialUpdatedNextShipmentGamma.setId(nextShipmentGamma.getId());

        partialUpdatedNextShipmentGamma
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentGamma.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentGamma))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentGamma in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentGammaUpdatableFieldsEquals(
            partialUpdatedNextShipmentGamma,
            getPersistedNextShipmentGamma(partialUpdatedNextShipmentGamma)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentGamma.setId(longCount.incrementAndGet());

        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentGammaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentGamma.setId(longCount.incrementAndGet());

        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentGammaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentGammaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentGamma() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentGamma.setId(longCount.incrementAndGet());

        // Create the NextShipmentGamma
        NextShipmentGammaDTO nextShipmentGammaDTO = nextShipmentGammaMapper.toDto(nextShipmentGamma);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentGammaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentGammaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentGamma in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentGamma() throws Exception {
        // Initialize the database
        insertedNextShipmentGamma = nextShipmentGammaRepository.saveAndFlush(nextShipmentGamma);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentGamma
        restNextShipmentGammaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentGamma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentGammaRepository.count();
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

    protected NextShipmentGamma getPersistedNextShipmentGamma(NextShipmentGamma nextShipmentGamma) {
        return nextShipmentGammaRepository.findById(nextShipmentGamma.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentGammaToMatchAllProperties(NextShipmentGamma expectedNextShipmentGamma) {
        assertNextShipmentGammaAllPropertiesEquals(expectedNextShipmentGamma, getPersistedNextShipmentGamma(expectedNextShipmentGamma));
    }

    protected void assertPersistedNextShipmentGammaToMatchUpdatableProperties(NextShipmentGamma expectedNextShipmentGamma) {
        assertNextShipmentGammaAllUpdatablePropertiesEquals(
            expectedNextShipmentGamma,
            getPersistedNextShipmentGamma(expectedNextShipmentGamma)
        );
    }
}

package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextShipmentAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.NextShipmentAlpha;
import xyz.jhmapstruct.repository.NextShipmentAlphaRepository;
import xyz.jhmapstruct.service.dto.NextShipmentAlphaDTO;
import xyz.jhmapstruct.service.mapper.NextShipmentAlphaMapper;

/**
 * Integration tests for the {@link NextShipmentAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextShipmentAlphaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/next-shipment-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextShipmentAlphaRepository nextShipmentAlphaRepository;

    @Autowired
    private NextShipmentAlphaMapper nextShipmentAlphaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextShipmentAlphaMockMvc;

    private NextShipmentAlpha nextShipmentAlpha;

    private NextShipmentAlpha insertedNextShipmentAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextShipmentAlpha createEntity() {
        return new NextShipmentAlpha()
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
    public static NextShipmentAlpha createUpdatedEntity() {
        return new NextShipmentAlpha()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        nextShipmentAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextShipmentAlpha != null) {
            nextShipmentAlphaRepository.delete(insertedNextShipmentAlpha);
            insertedNextShipmentAlpha = null;
        }
    }

    @Test
    @Transactional
    void createNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);
        var returnedNextShipmentAlphaDTO = om.readValue(
            restNextShipmentAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextShipmentAlphaDTO.class
        );

        // Validate the NextShipmentAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextShipmentAlpha = nextShipmentAlphaMapper.toEntity(returnedNextShipmentAlphaDTO);
        assertNextShipmentAlphaUpdatableFieldsEquals(returnedNextShipmentAlpha, getPersistedNextShipmentAlpha(returnedNextShipmentAlpha));

        insertedNextShipmentAlpha = returnedNextShipmentAlpha;
    }

    @Test
    @Transactional
    void createNextShipmentAlphaWithExistingId() throws Exception {
        // Create the NextShipmentAlpha with an existing ID
        nextShipmentAlpha.setId(1L);
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextShipmentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextShipmentAlpha.setTrackingNumber(null);

        // Create the NextShipmentAlpha, which fails.
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        restNextShipmentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphas() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList
        restNextShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getNextShipmentAlpha() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get the nextShipmentAlpha
        restNextShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, nextShipmentAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextShipmentAlpha.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNextShipmentAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        Long id = nextShipmentAlpha.getId();

        defaultNextShipmentAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextShipmentAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextShipmentAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where trackingNumber equals to
        defaultNextShipmentAlphaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where trackingNumber in
        defaultNextShipmentAlphaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where trackingNumber is not null
        defaultNextShipmentAlphaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where trackingNumber contains
        defaultNextShipmentAlphaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where trackingNumber does not contain
        defaultNextShipmentAlphaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where shippedDate equals to
        defaultNextShipmentAlphaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where shippedDate in
        defaultNextShipmentAlphaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where shippedDate is not null
        defaultNextShipmentAlphaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where deliveryDate equals to
        defaultNextShipmentAlphaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where deliveryDate in
        defaultNextShipmentAlphaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        // Get all the nextShipmentAlphaList where deliveryDate is not null
        defaultNextShipmentAlphaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNextShipmentAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextShipmentAlpha.setTenant(tenant);
        nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);
        Long tenantId = tenant.getId();
        // Get all the nextShipmentAlphaList where tenant equals to tenantId
        defaultNextShipmentAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextShipmentAlphaList where tenant equals to (tenantId + 1)
        defaultNextShipmentAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextShipmentAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextShipmentAlphaShouldBeFound(shouldBeFound);
        defaultNextShipmentAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextShipmentAlphaShouldBeFound(String filter) throws Exception {
        restNextShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextShipmentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restNextShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextShipmentAlphaShouldNotBeFound(String filter) throws Exception {
        restNextShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextShipmentAlpha() throws Exception {
        // Get the nextShipmentAlpha
        restNextShipmentAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextShipmentAlpha() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentAlpha
        NextShipmentAlpha updatedNextShipmentAlpha = nextShipmentAlphaRepository.findById(nextShipmentAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextShipmentAlpha are not directly saved in db
        em.detach(updatedNextShipmentAlpha);
        updatedNextShipmentAlpha
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(updatedNextShipmentAlpha);

        restNextShipmentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextShipmentAlphaToMatchAllProperties(updatedNextShipmentAlpha);
    }

    @Test
    @Transactional
    void putNonExistingNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentAlpha.setId(longCount.incrementAndGet());

        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextShipmentAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentAlpha.setId(longCount.incrementAndGet());

        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextShipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentAlpha.setId(longCount.incrementAndGet());

        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextShipmentAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextShipmentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentAlpha using partial update
        NextShipmentAlpha partialUpdatedNextShipmentAlpha = new NextShipmentAlpha();
        partialUpdatedNextShipmentAlpha.setId(nextShipmentAlpha.getId());

        partialUpdatedNextShipmentAlpha.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE);

        restNextShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextShipmentAlpha, nextShipmentAlpha),
            getPersistedNextShipmentAlpha(nextShipmentAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextShipmentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextShipmentAlpha using partial update
        NextShipmentAlpha partialUpdatedNextShipmentAlpha = new NextShipmentAlpha();
        partialUpdatedNextShipmentAlpha.setId(nextShipmentAlpha.getId());

        partialUpdatedNextShipmentAlpha
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restNextShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextShipmentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextShipmentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the NextShipmentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextShipmentAlphaUpdatableFieldsEquals(
            partialUpdatedNextShipmentAlpha,
            getPersistedNextShipmentAlpha(partialUpdatedNextShipmentAlpha)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentAlpha.setId(longCount.incrementAndGet());

        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextShipmentAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentAlpha.setId(longCount.incrementAndGet());

        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextShipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextShipmentAlpha.setId(longCount.incrementAndGet());

        // Create the NextShipmentAlpha
        NextShipmentAlphaDTO nextShipmentAlphaDTO = nextShipmentAlphaMapper.toDto(nextShipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextShipmentAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextShipmentAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextShipmentAlpha() throws Exception {
        // Initialize the database
        insertedNextShipmentAlpha = nextShipmentAlphaRepository.saveAndFlush(nextShipmentAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextShipmentAlpha
        restNextShipmentAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextShipmentAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextShipmentAlphaRepository.count();
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

    protected NextShipmentAlpha getPersistedNextShipmentAlpha(NextShipmentAlpha nextShipmentAlpha) {
        return nextShipmentAlphaRepository.findById(nextShipmentAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedNextShipmentAlphaToMatchAllProperties(NextShipmentAlpha expectedNextShipmentAlpha) {
        assertNextShipmentAlphaAllPropertiesEquals(expectedNextShipmentAlpha, getPersistedNextShipmentAlpha(expectedNextShipmentAlpha));
    }

    protected void assertPersistedNextShipmentAlphaToMatchUpdatableProperties(NextShipmentAlpha expectedNextShipmentAlpha) {
        assertNextShipmentAlphaAllUpdatablePropertiesEquals(
            expectedNextShipmentAlpha,
            getPersistedNextShipmentAlpha(expectedNextShipmentAlpha)
        );
    }
}

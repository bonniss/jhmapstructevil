package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentAlphaAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentAlpha;
import xyz.jhmapstruct.repository.ShipmentAlphaRepository;
import xyz.jhmapstruct.service.dto.ShipmentAlphaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentAlphaMapper;

/**
 * Integration tests for the {@link ShipmentAlphaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentAlphaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-alphas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentAlphaRepository shipmentAlphaRepository;

    @Autowired
    private ShipmentAlphaMapper shipmentAlphaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentAlphaMockMvc;

    private ShipmentAlpha shipmentAlpha;

    private ShipmentAlpha insertedShipmentAlpha;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentAlpha createEntity() {
        return new ShipmentAlpha()
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
    public static ShipmentAlpha createUpdatedEntity() {
        return new ShipmentAlpha()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentAlpha = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentAlpha != null) {
            shipmentAlphaRepository.delete(insertedShipmentAlpha);
            insertedShipmentAlpha = null;
        }
    }

    @Test
    @Transactional
    void createShipmentAlpha() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);
        var returnedShipmentAlphaDTO = om.readValue(
            restShipmentAlphaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAlphaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentAlphaDTO.class
        );

        // Validate the ShipmentAlpha in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipmentAlpha = shipmentAlphaMapper.toEntity(returnedShipmentAlphaDTO);
        assertShipmentAlphaUpdatableFieldsEquals(returnedShipmentAlpha, getPersistedShipmentAlpha(returnedShipmentAlpha));

        insertedShipmentAlpha = returnedShipmentAlpha;
    }

    @Test
    @Transactional
    void createShipmentAlphaWithExistingId() throws Exception {
        // Create the ShipmentAlpha with an existing ID
        shipmentAlpha.setId(1L);
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAlphaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentAlpha.setTrackingNumber(null);

        // Create the ShipmentAlpha, which fails.
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        restShipmentAlphaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAlphaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentAlphas() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList
        restShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentAlpha() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get the shipmentAlpha
        restShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentAlpha.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentAlpha.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentAlphasByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        Long id = shipmentAlpha.getId();

        defaultShipmentAlphaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentAlphaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentAlphaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where trackingNumber equals to
        defaultShipmentAlphaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where trackingNumber in
        defaultShipmentAlphaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where trackingNumber is not null
        defaultShipmentAlphaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where trackingNumber contains
        defaultShipmentAlphaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where trackingNumber does not contain
        defaultShipmentAlphaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where shippedDate equals to
        defaultShipmentAlphaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where shippedDate in
        defaultShipmentAlphaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where shippedDate is not null
        defaultShipmentAlphaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where deliveryDate equals to
        defaultShipmentAlphaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where deliveryDate in
        defaultShipmentAlphaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        // Get all the shipmentAlphaList where deliveryDate is not null
        defaultShipmentAlphaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentAlphasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentAlphaRepository.saveAndFlush(shipmentAlpha);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentAlpha.setTenant(tenant);
        shipmentAlphaRepository.saveAndFlush(shipmentAlpha);
        Long tenantId = tenant.getId();
        // Get all the shipmentAlphaList where tenant equals to tenantId
        defaultShipmentAlphaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentAlphaList where tenant equals to (tenantId + 1)
        defaultShipmentAlphaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentAlphaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentAlphaShouldBeFound(shouldBeFound);
        defaultShipmentAlphaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentAlphaShouldBeFound(String filter) throws Exception {
        restShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentAlpha.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentAlphaShouldNotBeFound(String filter) throws Exception {
        restShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentAlphaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentAlpha() throws Exception {
        // Get the shipmentAlpha
        restShipmentAlphaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentAlpha() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentAlpha
        ShipmentAlpha updatedShipmentAlpha = shipmentAlphaRepository.findById(shipmentAlpha.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentAlpha are not directly saved in db
        em.detach(updatedShipmentAlpha);
        updatedShipmentAlpha.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(updatedShipmentAlpha);

        restShipmentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentAlphaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentAlphaToMatchAllProperties(updatedShipmentAlpha);
    }

    @Test
    @Transactional
    void putNonExistingShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAlpha.setId(longCount.incrementAndGet());

        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentAlphaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAlpha.setId(longCount.incrementAndGet());

        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAlphaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAlpha.setId(longCount.incrementAndGet());

        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAlphaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentAlpha using partial update
        ShipmentAlpha partialUpdatedShipmentAlpha = new ShipmentAlpha();
        partialUpdatedShipmentAlpha.setId(shipmentAlpha.getId());

        partialUpdatedShipmentAlpha
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentAlphaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentAlpha, shipmentAlpha),
            getPersistedShipmentAlpha(shipmentAlpha)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentAlphaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentAlpha using partial update
        ShipmentAlpha partialUpdatedShipmentAlpha = new ShipmentAlpha();
        partialUpdatedShipmentAlpha.setId(shipmentAlpha.getId());

        partialUpdatedShipmentAlpha
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentAlpha.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentAlpha))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentAlpha in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentAlphaUpdatableFieldsEquals(partialUpdatedShipmentAlpha, getPersistedShipmentAlpha(partialUpdatedShipmentAlpha));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAlpha.setId(longCount.incrementAndGet());

        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentAlphaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAlpha.setId(longCount.incrementAndGet());

        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAlphaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentAlphaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentAlpha() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentAlpha.setId(longCount.incrementAndGet());

        // Create the ShipmentAlpha
        ShipmentAlphaDTO shipmentAlphaDTO = shipmentAlphaMapper.toDto(shipmentAlpha);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentAlphaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentAlphaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentAlpha in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentAlpha() throws Exception {
        // Initialize the database
        insertedShipmentAlpha = shipmentAlphaRepository.saveAndFlush(shipmentAlpha);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentAlpha
        restShipmentAlphaMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentAlpha.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentAlphaRepository.count();
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

    protected ShipmentAlpha getPersistedShipmentAlpha(ShipmentAlpha shipmentAlpha) {
        return shipmentAlphaRepository.findById(shipmentAlpha.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentAlphaToMatchAllProperties(ShipmentAlpha expectedShipmentAlpha) {
        assertShipmentAlphaAllPropertiesEquals(expectedShipmentAlpha, getPersistedShipmentAlpha(expectedShipmentAlpha));
    }

    protected void assertPersistedShipmentAlphaToMatchUpdatableProperties(ShipmentAlpha expectedShipmentAlpha) {
        assertShipmentAlphaAllUpdatablePropertiesEquals(expectedShipmentAlpha, getPersistedShipmentAlpha(expectedShipmentAlpha));
    }
}

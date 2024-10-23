package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ShipmentBetaAsserts.*;
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
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.repository.ShipmentBetaRepository;
import xyz.jhmapstruct.service.dto.ShipmentBetaDTO;
import xyz.jhmapstruct.service.mapper.ShipmentBetaMapper;

/**
 * Integration tests for the {@link ShipmentBetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShipmentBetaResourceIT {

    private static final String DEFAULT_TRACKING_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_SHIPPED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SHIPPED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shipment-betas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShipmentBetaRepository shipmentBetaRepository;

    @Autowired
    private ShipmentBetaMapper shipmentBetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShipmentBetaMockMvc;

    private ShipmentBeta shipmentBeta;

    private ShipmentBeta insertedShipmentBeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShipmentBeta createEntity() {
        return new ShipmentBeta()
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
    public static ShipmentBeta createUpdatedEntity() {
        return new ShipmentBeta()
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);
    }

    @BeforeEach
    public void initTest() {
        shipmentBeta = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShipmentBeta != null) {
            shipmentBetaRepository.delete(insertedShipmentBeta);
            insertedShipmentBeta = null;
        }
    }

    @Test
    @Transactional
    void createShipmentBeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);
        var returnedShipmentBetaDTO = om.readValue(
            restShipmentBetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentBetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShipmentBetaDTO.class
        );

        // Validate the ShipmentBeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShipmentBeta = shipmentBetaMapper.toEntity(returnedShipmentBetaDTO);
        assertShipmentBetaUpdatableFieldsEquals(returnedShipmentBeta, getPersistedShipmentBeta(returnedShipmentBeta));

        insertedShipmentBeta = returnedShipmentBeta;
    }

    @Test
    @Transactional
    void createShipmentBetaWithExistingId() throws Exception {
        // Create the ShipmentBeta with an existing ID
        shipmentBeta.setId(1L);
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShipmentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentBetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTrackingNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        shipmentBeta.setTrackingNumber(null);

        // Create the ShipmentBeta, which fails.
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        restShipmentBetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentBetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShipmentBetas() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList
        restShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    void getShipmentBeta() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get the shipmentBeta
        restShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL_ID, shipmentBeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shipmentBeta.getId().intValue()))
            .andExpect(jsonPath("$.trackingNumber").value(DEFAULT_TRACKING_NUMBER))
            .andExpect(jsonPath("$.shippedDate").value(DEFAULT_SHIPPED_DATE.toString()))
            .andExpect(jsonPath("$.deliveryDate").value(DEFAULT_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    void getShipmentBetasByIdFiltering() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        Long id = shipmentBeta.getId();

        defaultShipmentBetaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultShipmentBetaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultShipmentBetaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShipmentBetasByTrackingNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where trackingNumber equals to
        defaultShipmentBetaFiltering(
            "trackingNumber.equals=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.equals=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentBetasByTrackingNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where trackingNumber in
        defaultShipmentBetaFiltering(
            "trackingNumber.in=" + DEFAULT_TRACKING_NUMBER + "," + UPDATED_TRACKING_NUMBER,
            "trackingNumber.in=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentBetasByTrackingNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where trackingNumber is not null
        defaultShipmentBetaFiltering("trackingNumber.specified=true", "trackingNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentBetasByTrackingNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where trackingNumber contains
        defaultShipmentBetaFiltering(
            "trackingNumber.contains=" + DEFAULT_TRACKING_NUMBER,
            "trackingNumber.contains=" + UPDATED_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentBetasByTrackingNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where trackingNumber does not contain
        defaultShipmentBetaFiltering(
            "trackingNumber.doesNotContain=" + UPDATED_TRACKING_NUMBER,
            "trackingNumber.doesNotContain=" + DEFAULT_TRACKING_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllShipmentBetasByShippedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where shippedDate equals to
        defaultShipmentBetaFiltering("shippedDate.equals=" + DEFAULT_SHIPPED_DATE, "shippedDate.equals=" + UPDATED_SHIPPED_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentBetasByShippedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where shippedDate in
        defaultShipmentBetaFiltering(
            "shippedDate.in=" + DEFAULT_SHIPPED_DATE + "," + UPDATED_SHIPPED_DATE,
            "shippedDate.in=" + UPDATED_SHIPPED_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentBetasByShippedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where shippedDate is not null
        defaultShipmentBetaFiltering("shippedDate.specified=true", "shippedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentBetasByDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where deliveryDate equals to
        defaultShipmentBetaFiltering("deliveryDate.equals=" + DEFAULT_DELIVERY_DATE, "deliveryDate.equals=" + UPDATED_DELIVERY_DATE);
    }

    @Test
    @Transactional
    void getAllShipmentBetasByDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where deliveryDate in
        defaultShipmentBetaFiltering(
            "deliveryDate.in=" + DEFAULT_DELIVERY_DATE + "," + UPDATED_DELIVERY_DATE,
            "deliveryDate.in=" + UPDATED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllShipmentBetasByDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        // Get all the shipmentBetaList where deliveryDate is not null
        defaultShipmentBetaFiltering("deliveryDate.specified=true", "deliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllShipmentBetasByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            shipmentBetaRepository.saveAndFlush(shipmentBeta);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        shipmentBeta.setTenant(tenant);
        shipmentBetaRepository.saveAndFlush(shipmentBeta);
        Long tenantId = tenant.getId();
        // Get all the shipmentBetaList where tenant equals to tenantId
        defaultShipmentBetaShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the shipmentBetaList where tenant equals to (tenantId + 1)
        defaultShipmentBetaShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultShipmentBetaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultShipmentBetaShouldBeFound(shouldBeFound);
        defaultShipmentBetaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShipmentBetaShouldBeFound(String filter) throws Exception {
        restShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shipmentBeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].trackingNumber").value(hasItem(DEFAULT_TRACKING_NUMBER)))
            .andExpect(jsonPath("$.[*].shippedDate").value(hasItem(DEFAULT_SHIPPED_DATE.toString())))
            .andExpect(jsonPath("$.[*].deliveryDate").value(hasItem(DEFAULT_DELIVERY_DATE.toString())));

        // Check, that the count call also returns 1
        restShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShipmentBetaShouldNotBeFound(String filter) throws Exception {
        restShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShipmentBetaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShipmentBeta() throws Exception {
        // Get the shipmentBeta
        restShipmentBetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShipmentBeta() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentBeta
        ShipmentBeta updatedShipmentBeta = shipmentBetaRepository.findById(shipmentBeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShipmentBeta are not directly saved in db
        em.detach(updatedShipmentBeta);
        updatedShipmentBeta.trackingNumber(UPDATED_TRACKING_NUMBER).shippedDate(UPDATED_SHIPPED_DATE).deliveryDate(UPDATED_DELIVERY_DATE);
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(updatedShipmentBeta);

        restShipmentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentBetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShipmentBetaToMatchAllProperties(updatedShipmentBeta);
    }

    @Test
    @Transactional
    void putNonExistingShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentBeta.setId(longCount.incrementAndGet());

        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shipmentBetaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentBeta.setId(longCount.incrementAndGet());

        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentBetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentBeta.setId(longCount.incrementAndGet());

        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentBetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shipmentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShipmentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentBeta using partial update
        ShipmentBeta partialUpdatedShipmentBeta = new ShipmentBeta();
        partialUpdatedShipmentBeta.setId(shipmentBeta.getId());

        partialUpdatedShipmentBeta.trackingNumber(UPDATED_TRACKING_NUMBER);

        restShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentBeta))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentBetaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedShipmentBeta, shipmentBeta),
            getPersistedShipmentBeta(shipmentBeta)
        );
    }

    @Test
    @Transactional
    void fullUpdateShipmentBetaWithPatch() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shipmentBeta using partial update
        ShipmentBeta partialUpdatedShipmentBeta = new ShipmentBeta();
        partialUpdatedShipmentBeta.setId(shipmentBeta.getId());

        partialUpdatedShipmentBeta
            .trackingNumber(UPDATED_TRACKING_NUMBER)
            .shippedDate(UPDATED_SHIPPED_DATE)
            .deliveryDate(UPDATED_DELIVERY_DATE);

        restShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShipmentBeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShipmentBeta))
            )
            .andExpect(status().isOk());

        // Validate the ShipmentBeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShipmentBetaUpdatableFieldsEquals(partialUpdatedShipmentBeta, getPersistedShipmentBeta(partialUpdatedShipmentBeta));
    }

    @Test
    @Transactional
    void patchNonExistingShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentBeta.setId(longCount.incrementAndGet());

        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shipmentBetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentBeta.setId(longCount.incrementAndGet());

        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentBetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shipmentBetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShipmentBeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        shipmentBeta.setId(longCount.incrementAndGet());

        // Create the ShipmentBeta
        ShipmentBetaDTO shipmentBetaDTO = shipmentBetaMapper.toDto(shipmentBeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShipmentBetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shipmentBetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShipmentBeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShipmentBeta() throws Exception {
        // Initialize the database
        insertedShipmentBeta = shipmentBetaRepository.saveAndFlush(shipmentBeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the shipmentBeta
        restShipmentBetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, shipmentBeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return shipmentBetaRepository.count();
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

    protected ShipmentBeta getPersistedShipmentBeta(ShipmentBeta shipmentBeta) {
        return shipmentBetaRepository.findById(shipmentBeta.getId()).orElseThrow();
    }

    protected void assertPersistedShipmentBetaToMatchAllProperties(ShipmentBeta expectedShipmentBeta) {
        assertShipmentBetaAllPropertiesEquals(expectedShipmentBeta, getPersistedShipmentBeta(expectedShipmentBeta));
    }

    protected void assertPersistedShipmentBetaToMatchUpdatableProperties(ShipmentBeta expectedShipmentBeta) {
        assertShipmentBetaAllUpdatablePropertiesEquals(expectedShipmentBeta, getPersistedShipmentBeta(expectedShipmentBeta));
    }
}

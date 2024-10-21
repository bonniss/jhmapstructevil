package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.Supplier;
import xyz.jhmapstruct.repository.SupplierRepository;
import xyz.jhmapstruct.service.SupplierService;
import xyz.jhmapstruct.service.dto.SupplierDTO;
import xyz.jhmapstruct.service.mapper.SupplierMapper;

/**
 * Integration tests for the {@link SupplierResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierRepository supplierRepositoryMock;

    @Autowired
    private SupplierMapper supplierMapper;

    @Mock
    private SupplierService supplierServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    private Supplier insertedSupplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity() {
        return new Supplier()
            .name(DEFAULT_NAME)
            .contactPerson(DEFAULT_CONTACT_PERSON)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createUpdatedEntity() {
        return new Supplier()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplier = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplier != null) {
            supplierRepository.delete(insertedSupplier);
            insertedSupplier = null;
        }
    }

    @Test
    @Transactional
    void createSupplier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);
        var returnedSupplierDTO = om.readValue(
            restSupplierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierDTO.class
        );

        // Validate the Supplier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplier = supplierMapper.toEntity(returnedSupplierDTO);
        assertSupplierUpdatableFieldsEquals(returnedSupplier, getPersistedSupplier(returnedSupplier));

        insertedSupplier = returnedSupplier;
    }

    @Test
    @Transactional
    void createSupplierWithExistingId() throws Exception {
        // Create the Supplier with an existing ID
        supplier.setId(1L);
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplier.setName(null);

        // Create the Supplier, which fails.
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSuppliers() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuppliersWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSuppliersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplier() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplier() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);
        SupplierDTO supplierDTO = supplierMapper.toDto(updatedSupplier);

        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierToMatchAllProperties(updatedSupplier);
    }

    @Test
    @Transactional
    void putNonExistingSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSupplier, supplier), getPersistedSupplier(supplier));
    }

    @Test
    @Transactional
    void fullUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierUpdatableFieldsEquals(partialUpdatedSupplier, getPersistedSupplier(partialUpdatedSupplier));
    }

    @Test
    @Transactional
    void patchNonExistingSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // Create the Supplier
        SupplierDTO supplierDTO = supplierMapper.toDto(supplier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplier() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplier
        restSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierRepository.count();
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

    protected Supplier getPersistedSupplier(Supplier supplier) {
        return supplierRepository.findById(supplier.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierToMatchAllProperties(Supplier expectedSupplier) {
        assertSupplierAllPropertiesEquals(expectedSupplier, getPersistedSupplier(expectedSupplier));
    }

    protected void assertPersistedSupplierToMatchUpdatableProperties(Supplier expectedSupplier) {
        assertSupplierAllUpdatablePropertiesEquals(expectedSupplier, getPersistedSupplier(expectedSupplier));
    }
}

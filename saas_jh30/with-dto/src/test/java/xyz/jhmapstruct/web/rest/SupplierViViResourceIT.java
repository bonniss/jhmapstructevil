package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierViViAsserts.*;
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
import xyz.jhmapstruct.domain.SupplierViVi;
import xyz.jhmapstruct.repository.SupplierViViRepository;
import xyz.jhmapstruct.service.SupplierViViService;
import xyz.jhmapstruct.service.dto.SupplierViViDTO;
import xyz.jhmapstruct.service.mapper.SupplierViViMapper;

/**
 * Integration tests for the {@link SupplierViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierViViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierViViRepository supplierViViRepository;

    @Mock
    private SupplierViViRepository supplierViViRepositoryMock;

    @Autowired
    private SupplierViViMapper supplierViViMapper;

    @Mock
    private SupplierViViService supplierViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierViViMockMvc;

    private SupplierViVi supplierViVi;

    private SupplierViVi insertedSupplierViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierViVi createEntity() {
        return new SupplierViVi()
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
    public static SupplierViVi createUpdatedEntity() {
        return new SupplierViVi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierViVi != null) {
            supplierViViRepository.delete(insertedSupplierViVi);
            insertedSupplierViVi = null;
        }
    }

    @Test
    @Transactional
    void createSupplierViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);
        var returnedSupplierViViDTO = om.readValue(
            restSupplierViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierViViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierViViDTO.class
        );

        // Validate the SupplierViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSupplierViVi = supplierViViMapper.toEntity(returnedSupplierViViDTO);
        assertSupplierViViUpdatableFieldsEquals(returnedSupplierViVi, getPersistedSupplierViVi(returnedSupplierViVi));

        insertedSupplierViVi = returnedSupplierViVi;
    }

    @Test
    @Transactional
    void createSupplierViViWithExistingId() throws Exception {
        // Create the SupplierViVi with an existing ID
        supplierViVi.setId(1L);
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierViViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierViVi.setName(null);

        // Create the SupplierViVi, which fails.
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        restSupplierViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierViViDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierViVis() throws Exception {
        // Initialize the database
        insertedSupplierViVi = supplierViViRepository.saveAndFlush(supplierViVi);

        // Get all the supplierViViList
        restSupplierViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierViVi() throws Exception {
        // Initialize the database
        insertedSupplierViVi = supplierViViRepository.saveAndFlush(supplierViVi);

        // Get the supplierViVi
        restSupplierViViMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierViVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingSupplierViVi() throws Exception {
        // Get the supplierViVi
        restSupplierViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierViVi() throws Exception {
        // Initialize the database
        insertedSupplierViVi = supplierViViRepository.saveAndFlush(supplierViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierViVi
        SupplierViVi updatedSupplierViVi = supplierViViRepository.findById(supplierViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierViVi are not directly saved in db
        em.detach(updatedSupplierViVi);
        updatedSupplierViVi.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(updatedSupplierViVi);

        restSupplierViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierViViDTO))
            )
            .andExpect(status().isOk());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierViViToMatchAllProperties(updatedSupplierViVi);
    }

    @Test
    @Transactional
    void putNonExistingSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierViVi.setId(longCount.incrementAndGet());

        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierViViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierViVi.setId(longCount.incrementAndGet());

        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierViVi.setId(longCount.incrementAndGet());

        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierViViWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierViVi = supplierViViRepository.saveAndFlush(supplierViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierViVi using partial update
        SupplierViVi partialUpdatedSupplierViVi = new SupplierViVi();
        partialUpdatedSupplierViVi.setId(supplierViVi.getId());

        partialUpdatedSupplierViVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierViVi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierViVi, supplierViVi),
            getPersistedSupplierViVi(supplierViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierViViWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierViVi = supplierViViRepository.saveAndFlush(supplierViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierViVi using partial update
        SupplierViVi partialUpdatedSupplierViVi = new SupplierViVi();
        partialUpdatedSupplierViVi.setId(supplierViVi.getId());

        partialUpdatedSupplierViVi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierViVi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierViViUpdatableFieldsEquals(partialUpdatedSupplierViVi, getPersistedSupplierViVi(partialUpdatedSupplierViVi));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierViVi.setId(longCount.incrementAndGet());

        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierViViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierViVi.setId(longCount.incrementAndGet());

        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierViViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierViVi.setId(longCount.incrementAndGet());

        // Create the SupplierViVi
        SupplierViViDTO supplierViViDTO = supplierViViMapper.toDto(supplierViVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierViViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierViVi() throws Exception {
        // Initialize the database
        insertedSupplierViVi = supplierViViRepository.saveAndFlush(supplierViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierViVi
        restSupplierViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierViViRepository.count();
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

    protected SupplierViVi getPersistedSupplierViVi(SupplierViVi supplierViVi) {
        return supplierViViRepository.findById(supplierViVi.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierViViToMatchAllProperties(SupplierViVi expectedSupplierViVi) {
        assertSupplierViViAllPropertiesEquals(expectedSupplierViVi, getPersistedSupplierViVi(expectedSupplierViVi));
    }

    protected void assertPersistedSupplierViViToMatchUpdatableProperties(SupplierViVi expectedSupplierViVi) {
        assertSupplierViViAllUpdatablePropertiesEquals(expectedSupplierViVi, getPersistedSupplierViVi(expectedSupplierViVi));
    }
}

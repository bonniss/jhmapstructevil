package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.SupplierMiMiAsserts.*;
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
import xyz.jhmapstruct.domain.SupplierMiMi;
import xyz.jhmapstruct.repository.SupplierMiMiRepository;
import xyz.jhmapstruct.service.SupplierMiMiService;

/**
 * Integration tests for the {@link SupplierMiMiResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SupplierMiMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_PERSON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_PERSON = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/supplier-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierMiMiRepository supplierMiMiRepository;

    @Mock
    private SupplierMiMiRepository supplierMiMiRepositoryMock;

    @Mock
    private SupplierMiMiService supplierMiMiServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMiMiMockMvc;

    private SupplierMiMi supplierMiMi;

    private SupplierMiMi insertedSupplierMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SupplierMiMi createEntity() {
        return new SupplierMiMi()
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
    public static SupplierMiMi createUpdatedEntity() {
        return new SupplierMiMi()
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        supplierMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSupplierMiMi != null) {
            supplierMiMiRepository.delete(insertedSupplierMiMi);
            insertedSupplierMiMi = null;
        }
    }

    @Test
    @Transactional
    void createSupplierMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SupplierMiMi
        var returnedSupplierMiMi = om.readValue(
            restSupplierMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SupplierMiMi.class
        );

        // Validate the SupplierMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupplierMiMiUpdatableFieldsEquals(returnedSupplierMiMi, getPersistedSupplierMiMi(returnedSupplierMiMi));

        insertedSupplierMiMi = returnedSupplierMiMi;
    }

    @Test
    @Transactional
    void createSupplierMiMiWithExistingId() throws Exception {
        // Create the SupplierMiMi with an existing ID
        supplierMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        supplierMiMi.setName(null);

        // Create the SupplierMiMi, which fails.

        restSupplierMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSupplierMiMis() throws Exception {
        // Initialize the database
        insertedSupplierMiMi = supplierMiMiRepository.saveAndFlush(supplierMiMi);

        // Get all the supplierMiMiList
        restSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplierMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactPerson").value(hasItem(DEFAULT_CONTACT_PERSON)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierMiMisWithEagerRelationshipsIsEnabled() throws Exception {
        when(supplierMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(supplierMiMiServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSupplierMiMisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(supplierMiMiServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSupplierMiMiMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(supplierMiMiRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSupplierMiMi() throws Exception {
        // Initialize the database
        insertedSupplierMiMi = supplierMiMiRepository.saveAndFlush(supplierMiMi);

        // Get the supplierMiMi
        restSupplierMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, supplierMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplierMiMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactPerson").value(DEFAULT_CONTACT_PERSON))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingSupplierMiMi() throws Exception {
        // Get the supplierMiMi
        restSupplierMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplierMiMi() throws Exception {
        // Initialize the database
        insertedSupplierMiMi = supplierMiMiRepository.saveAndFlush(supplierMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierMiMi
        SupplierMiMi updatedSupplierMiMi = supplierMiMiRepository.findById(supplierMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplierMiMi are not directly saved in db
        em.detach(updatedSupplierMiMi);
        updatedSupplierMiMi.name(UPDATED_NAME).contactPerson(UPDATED_CONTACT_PERSON).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplierMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupplierMiMi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierMiMiToMatchAllProperties(updatedSupplierMiMi);
    }

    @Test
    @Transactional
    void putNonExistingSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplierMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplierMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplierMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierMiMi = supplierMiMiRepository.saveAndFlush(supplierMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierMiMi using partial update
        SupplierMiMi partialUpdatedSupplierMiMi = new SupplierMiMi();
        partialUpdatedSupplierMiMi.setId(supplierMiMi.getId());

        partialUpdatedSupplierMiMi.contactPerson(UPDATED_CONTACT_PERSON).phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierMiMi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSupplierMiMi, supplierMiMi),
            getPersistedSupplierMiMi(supplierMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateSupplierMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedSupplierMiMi = supplierMiMiRepository.saveAndFlush(supplierMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplierMiMi using partial update
        SupplierMiMi partialUpdatedSupplierMiMi = new SupplierMiMi();
        partialUpdatedSupplierMiMi.setId(supplierMiMi.getId());

        partialUpdatedSupplierMiMi
            .name(UPDATED_NAME)
            .contactPerson(UPDATED_CONTACT_PERSON)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplierMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplierMiMi))
            )
            .andExpect(status().isOk());

        // Validate the SupplierMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierMiMiUpdatableFieldsEquals(partialUpdatedSupplierMiMi, getPersistedSupplierMiMi(partialUpdatedSupplierMiMi));
    }

    @Test
    @Transactional
    void patchNonExistingSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplierMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplierMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplierMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplierMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplierMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SupplierMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplierMiMi() throws Exception {
        // Initialize the database
        insertedSupplierMiMi = supplierMiMiRepository.saveAndFlush(supplierMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplierMiMi
        restSupplierMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplierMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierMiMiRepository.count();
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

    protected SupplierMiMi getPersistedSupplierMiMi(SupplierMiMi supplierMiMi) {
        return supplierMiMiRepository.findById(supplierMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierMiMiToMatchAllProperties(SupplierMiMi expectedSupplierMiMi) {
        assertSupplierMiMiAllPropertiesEquals(expectedSupplierMiMi, getPersistedSupplierMiMi(expectedSupplierMiMi));
    }

    protected void assertPersistedSupplierMiMiToMatchUpdatableProperties(SupplierMiMi expectedSupplierMiMi) {
        assertSupplierMiMiAllUpdatablePropertiesEquals(expectedSupplierMiMi, getPersistedSupplierMiMi(expectedSupplierMiMi));
    }
}

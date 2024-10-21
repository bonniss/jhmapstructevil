package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.ProductViViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;
import static xyz.jhmapstruct.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.ProductViViService;

/**
 * Integration tests for the {@link ProductViViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductViViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_STOCK = 1;
    private static final Integer UPDATED_STOCK = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductViViRepository productViViRepository;

    @Mock
    private ProductViViRepository productViViRepositoryMock;

    @Mock
    private ProductViViService productViViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductViViMockMvc;

    private ProductViVi productViVi;

    private ProductViVi insertedProductViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductViVi createEntity() {
        return new ProductViVi().name(DEFAULT_NAME).price(DEFAULT_PRICE).stock(DEFAULT_STOCK).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductViVi createUpdatedEntity() {
        return new ProductViVi().name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        productViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductViVi != null) {
            productViViRepository.delete(insertedProductViVi);
            insertedProductViVi = null;
        }
    }

    @Test
    @Transactional
    void createProductViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProductViVi
        var returnedProductViVi = om.readValue(
            restProductViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductViVi.class
        );

        // Validate the ProductViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductViViUpdatableFieldsEquals(returnedProductViVi, getPersistedProductViVi(returnedProductViVi));

        insertedProductViVi = returnedProductViVi;
    }

    @Test
    @Transactional
    void createProductViViWithExistingId() throws Exception {
        // Create the ProductViVi with an existing ID
        productViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViVi)))
            .andExpect(status().isBadRequest());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productViVi.setName(null);

        // Create the ProductViVi, which fails.

        restProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productViVi.setPrice(null);

        // Create the ProductViVi, which fails.

        restProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        productViVi.setStock(null);

        // Create the ProductViVi, which fails.

        restProductViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductViVis() throws Exception {
        // Initialize the database
        insertedProductViVi = productViViRepository.saveAndFlush(productViVi);

        // Get all the productViViList
        restProductViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductViVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(productViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productViViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductViVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productViViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductViViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productViViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductViVi() throws Exception {
        // Initialize the database
        insertedProductViVi = productViViRepository.saveAndFlush(productViVi);

        // Get the productViVi
        restProductViViMockMvc
            .perform(get(ENTITY_API_URL_ID, productViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productViVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductViVi() throws Exception {
        // Get the productViVi
        restProductViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductViVi() throws Exception {
        // Initialize the database
        insertedProductViVi = productViViRepository.saveAndFlush(productViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productViVi
        ProductViVi updatedProductViVi = productViViRepository.findById(productViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductViVi are not directly saved in db
        em.detach(updatedProductViVi);
        updatedProductViVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductViVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductViViToMatchAllProperties(updatedProductViVi);
    }

    @Test
    @Transactional
    void putNonExistingProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductViViWithPatch() throws Exception {
        // Initialize the database
        insertedProductViVi = productViViRepository.saveAndFlush(productViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productViVi using partial update
        ProductViVi partialUpdatedProductViVi = new ProductViVi();
        partialUpdatedProductViVi.setId(productViVi.getId());

        partialUpdatedProductViVi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductViVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductViVi, productViVi),
            getPersistedProductViVi(productViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductViViWithPatch() throws Exception {
        // Initialize the database
        insertedProductViVi = productViViRepository.saveAndFlush(productViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productViVi using partial update
        ProductViVi partialUpdatedProductViVi = new ProductViVi();
        partialUpdatedProductViVi.setId(productViVi.getId());

        partialUpdatedProductViVi.name(UPDATED_NAME).price(UPDATED_PRICE).stock(UPDATED_STOCK).description(UPDATED_DESCRIPTION);

        restProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductViVi))
            )
            .andExpect(status().isOk());

        // Validate the ProductViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductViViUpdatableFieldsEquals(partialUpdatedProductViVi, getPersistedProductViVi(partialUpdatedProductViVi));
    }

    @Test
    @Transactional
    void patchNonExistingProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        productViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductViVi() throws Exception {
        // Initialize the database
        insertedProductViVi = productViViRepository.saveAndFlush(productViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the productViVi
        restProductViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, productViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productViViRepository.count();
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

    protected ProductViVi getPersistedProductViVi(ProductViVi productViVi) {
        return productViViRepository.findById(productViVi.getId()).orElseThrow();
    }

    protected void assertPersistedProductViViToMatchAllProperties(ProductViVi expectedProductViVi) {
        assertProductViViAllPropertiesEquals(expectedProductViVi, getPersistedProductViVi(expectedProductViVi));
    }

    protected void assertPersistedProductViViToMatchUpdatableProperties(ProductViVi expectedProductViVi) {
        assertProductViViAllUpdatablePropertiesEquals(expectedProductViVi, getPersistedProductViVi(expectedProductViVi));
    }
}

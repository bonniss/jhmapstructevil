package ai.realworld.web.rest;

import static ai.realworld.domain.AlPyuJokerAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlProty;
import ai.realworld.domain.AlPyuJoker;
import ai.realworld.domain.EdSheeran;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.BenedictType;
import ai.realworld.domain.enumeration.PaulBargainStatus;
import ai.realworld.repository.AlPyuJokerRepository;
import ai.realworld.service.AlPyuJokerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.UUID;
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

/**
 * Integration tests for the {@link AlPyuJokerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlPyuJokerResourceIT {

    private static final String DEFAULT_BOOKING_NO = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_NO = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_HEITIGA = "BBBBBBBBBB";

    private static final BenedictType DEFAULT_PERIOD_TYPE = BenedictType.NIGHT;
    private static final BenedictType UPDATED_PERIOD_TYPE = BenedictType.DAYLIGHT;

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_IN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_IN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_OUT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_OUT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NUMBER_OF_ADULTS = 1;
    private static final Integer UPDATED_NUMBER_OF_ADULTS = 2;
    private static final Integer SMALLER_NUMBER_OF_ADULTS = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_PRESCHOOLERS = 0;
    private static final Integer UPDATED_NUMBER_OF_PRESCHOOLERS = 1;
    private static final Integer SMALLER_NUMBER_OF_PRESCHOOLERS = 0 - 1;

    private static final Integer DEFAULT_NUMBER_OF_CHILDREN = 0;
    private static final Integer UPDATED_NUMBER_OF_CHILDREN = 1;
    private static final Integer SMALLER_NUMBER_OF_CHILDREN = 0 - 1;

    private static final BigDecimal DEFAULT_BOOKING_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BOOKING_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BOOKING_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_EXTRA_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXTRA_FEE = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXTRA_FEE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final PaulBargainStatus DEFAULT_BOOKING_STATUS = PaulBargainStatus.PENDING;
    private static final PaulBargainStatus UPDATED_BOOKING_STATUS = PaulBargainStatus.RESERVED;

    private static final String DEFAULT_HISTORY_REF_JASON = "AAAAAAAAAA";
    private static final String UPDATED_HISTORY_REF_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-pyu-jokers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPyuJokerRepository alPyuJokerRepository;

    @Mock
    private AlPyuJokerRepository alPyuJokerRepositoryMock;

    @Mock
    private AlPyuJokerService alPyuJokerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPyuJokerMockMvc;

    private AlPyuJoker alPyuJoker;

    private AlPyuJoker insertedAlPyuJoker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuJoker createEntity() {
        return new AlPyuJoker()
            .bookingNo(DEFAULT_BOOKING_NO)
            .noteHeitiga(DEFAULT_NOTE_HEITIGA)
            .periodType(DEFAULT_PERIOD_TYPE)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .checkInDate(DEFAULT_CHECK_IN_DATE)
            .checkOutDate(DEFAULT_CHECK_OUT_DATE)
            .numberOfAdults(DEFAULT_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(DEFAULT_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(DEFAULT_NUMBER_OF_CHILDREN)
            .bookingPrice(DEFAULT_BOOKING_PRICE)
            .extraFee(DEFAULT_EXTRA_FEE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .bookingStatus(DEFAULT_BOOKING_STATUS)
            .historyRefJason(DEFAULT_HISTORY_REF_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuJoker createUpdatedEntity() {
        return new AlPyuJoker()
            .bookingNo(UPDATED_BOOKING_NO)
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE)
            .extraFee(UPDATED_EXTRA_FEE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);
    }

    @BeforeEach
    public void initTest() {
        alPyuJoker = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPyuJoker != null) {
            alPyuJokerRepository.delete(insertedAlPyuJoker);
            insertedAlPyuJoker = null;
        }
    }

    @Test
    @Transactional
    void createAlPyuJoker() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPyuJoker
        var returnedAlPyuJoker = om.readValue(
            restAlPyuJokerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJoker)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPyuJoker.class
        );

        // Validate the AlPyuJoker in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlPyuJokerUpdatableFieldsEquals(returnedAlPyuJoker, getPersistedAlPyuJoker(returnedAlPyuJoker));

        insertedAlPyuJoker = returnedAlPyuJoker;
    }

    @Test
    @Transactional
    void createAlPyuJokerWithExistingId() throws Exception {
        // Create the AlPyuJoker with an existing ID
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPyuJokerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJoker)))
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPyuJokers() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList
        restAlPyuJokerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuJoker.getId().toString())))
            .andExpect(jsonPath("$.[*].bookingNo").value(hasItem(DEFAULT_BOOKING_NO)))
            .andExpect(jsonPath("$.[*].noteHeitiga").value(hasItem(DEFAULT_NOTE_HEITIGA)))
            .andExpect(jsonPath("$.[*].periodType").value(hasItem(DEFAULT_PERIOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkInDate").value(hasItem(DEFAULT_CHECK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkOutDate").value(hasItem(DEFAULT_CHECK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfAdults").value(hasItem(DEFAULT_NUMBER_OF_ADULTS)))
            .andExpect(jsonPath("$.[*].numberOfPreschoolers").value(hasItem(DEFAULT_NUMBER_OF_PRESCHOOLERS)))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].bookingPrice").value(hasItem(sameNumber(DEFAULT_BOOKING_PRICE))))
            .andExpect(jsonPath("$.[*].extraFee").value(hasItem(sameNumber(DEFAULT_EXTRA_FEE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].bookingStatus").value(hasItem(DEFAULT_BOOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].historyRefJason").value(hasItem(DEFAULT_HISTORY_REF_JASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlPyuJokersWithEagerRelationshipsIsEnabled() throws Exception {
        when(alPyuJokerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlPyuJokerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alPyuJokerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlPyuJokersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alPyuJokerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlPyuJokerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alPyuJokerRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlPyuJoker() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get the alPyuJoker
        restAlPyuJokerMockMvc
            .perform(get(ENTITY_API_URL_ID, alPyuJoker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPyuJoker.getId().toString()))
            .andExpect(jsonPath("$.bookingNo").value(DEFAULT_BOOKING_NO))
            .andExpect(jsonPath("$.noteHeitiga").value(DEFAULT_NOTE_HEITIGA))
            .andExpect(jsonPath("$.periodType").value(DEFAULT_PERIOD_TYPE.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.checkInDate").value(DEFAULT_CHECK_IN_DATE.toString()))
            .andExpect(jsonPath("$.checkOutDate").value(DEFAULT_CHECK_OUT_DATE.toString()))
            .andExpect(jsonPath("$.numberOfAdults").value(DEFAULT_NUMBER_OF_ADULTS))
            .andExpect(jsonPath("$.numberOfPreschoolers").value(DEFAULT_NUMBER_OF_PRESCHOOLERS))
            .andExpect(jsonPath("$.numberOfChildren").value(DEFAULT_NUMBER_OF_CHILDREN))
            .andExpect(jsonPath("$.bookingPrice").value(sameNumber(DEFAULT_BOOKING_PRICE)))
            .andExpect(jsonPath("$.extraFee").value(sameNumber(DEFAULT_EXTRA_FEE)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.bookingStatus").value(DEFAULT_BOOKING_STATUS.toString()))
            .andExpect(jsonPath("$.historyRefJason").value(DEFAULT_HISTORY_REF_JASON));
    }

    @Test
    @Transactional
    void getAlPyuJokersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        UUID id = alPyuJoker.getId();

        defaultAlPyuJokerFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingNo equals to
        defaultAlPyuJokerFiltering("bookingNo.equals=" + DEFAULT_BOOKING_NO, "bookingNo.equals=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingNo in
        defaultAlPyuJokerFiltering("bookingNo.in=" + DEFAULT_BOOKING_NO + "," + UPDATED_BOOKING_NO, "bookingNo.in=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingNo is not null
        defaultAlPyuJokerFiltering("bookingNo.specified=true", "bookingNo.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingNoContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingNo contains
        defaultAlPyuJokerFiltering("bookingNo.contains=" + DEFAULT_BOOKING_NO, "bookingNo.contains=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingNo does not contain
        defaultAlPyuJokerFiltering("bookingNo.doesNotContain=" + UPDATED_BOOKING_NO, "bookingNo.doesNotContain=" + DEFAULT_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNoteHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where noteHeitiga equals to
        defaultAlPyuJokerFiltering("noteHeitiga.equals=" + DEFAULT_NOTE_HEITIGA, "noteHeitiga.equals=" + UPDATED_NOTE_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNoteHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where noteHeitiga in
        defaultAlPyuJokerFiltering(
            "noteHeitiga.in=" + DEFAULT_NOTE_HEITIGA + "," + UPDATED_NOTE_HEITIGA,
            "noteHeitiga.in=" + UPDATED_NOTE_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNoteHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where noteHeitiga is not null
        defaultAlPyuJokerFiltering("noteHeitiga.specified=true", "noteHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNoteHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where noteHeitiga contains
        defaultAlPyuJokerFiltering("noteHeitiga.contains=" + DEFAULT_NOTE_HEITIGA, "noteHeitiga.contains=" + UPDATED_NOTE_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNoteHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where noteHeitiga does not contain
        defaultAlPyuJokerFiltering(
            "noteHeitiga.doesNotContain=" + UPDATED_NOTE_HEITIGA,
            "noteHeitiga.doesNotContain=" + DEFAULT_NOTE_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByPeriodTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where periodType equals to
        defaultAlPyuJokerFiltering("periodType.equals=" + DEFAULT_PERIOD_TYPE, "periodType.equals=" + UPDATED_PERIOD_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByPeriodTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where periodType in
        defaultAlPyuJokerFiltering(
            "periodType.in=" + DEFAULT_PERIOD_TYPE + "," + UPDATED_PERIOD_TYPE,
            "periodType.in=" + UPDATED_PERIOD_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByPeriodTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where periodType is not null
        defaultAlPyuJokerFiltering("periodType.specified=true", "periodType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where fromDate equals to
        defaultAlPyuJokerFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where fromDate in
        defaultAlPyuJokerFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where fromDate is not null
        defaultAlPyuJokerFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where toDate equals to
        defaultAlPyuJokerFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where toDate in
        defaultAlPyuJokerFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where toDate is not null
        defaultAlPyuJokerFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCheckInDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where checkInDate equals to
        defaultAlPyuJokerFiltering("checkInDate.equals=" + DEFAULT_CHECK_IN_DATE, "checkInDate.equals=" + UPDATED_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCheckInDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where checkInDate in
        defaultAlPyuJokerFiltering(
            "checkInDate.in=" + DEFAULT_CHECK_IN_DATE + "," + UPDATED_CHECK_IN_DATE,
            "checkInDate.in=" + UPDATED_CHECK_IN_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCheckInDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where checkInDate is not null
        defaultAlPyuJokerFiltering("checkInDate.specified=true", "checkInDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCheckOutDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where checkOutDate equals to
        defaultAlPyuJokerFiltering("checkOutDate.equals=" + DEFAULT_CHECK_OUT_DATE, "checkOutDate.equals=" + UPDATED_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCheckOutDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where checkOutDate in
        defaultAlPyuJokerFiltering(
            "checkOutDate.in=" + DEFAULT_CHECK_OUT_DATE + "," + UPDATED_CHECK_OUT_DATE,
            "checkOutDate.in=" + UPDATED_CHECK_OUT_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCheckOutDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where checkOutDate is not null
        defaultAlPyuJokerFiltering("checkOutDate.specified=true", "checkOutDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults equals to
        defaultAlPyuJokerFiltering(
            "numberOfAdults.equals=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.equals=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults in
        defaultAlPyuJokerFiltering(
            "numberOfAdults.in=" + DEFAULT_NUMBER_OF_ADULTS + "," + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.in=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults is not null
        defaultAlPyuJokerFiltering("numberOfAdults.specified=true", "numberOfAdults.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults is greater than or equal to
        defaultAlPyuJokerFiltering(
            "numberOfAdults.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults is less than or equal to
        defaultAlPyuJokerFiltering(
            "numberOfAdults.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThanOrEqual=" + SMALLER_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults is less than
        defaultAlPyuJokerFiltering(
            "numberOfAdults.lessThan=" + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfAdultsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfAdults is greater than
        defaultAlPyuJokerFiltering(
            "numberOfAdults.greaterThan=" + SMALLER_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers equals to
        defaultAlPyuJokerFiltering(
            "numberOfPreschoolers.equals=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.equals=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers in
        defaultAlPyuJokerFiltering(
            "numberOfPreschoolers.in=" + DEFAULT_NUMBER_OF_PRESCHOOLERS + "," + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.in=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers is not null
        defaultAlPyuJokerFiltering("numberOfPreschoolers.specified=true", "numberOfPreschoolers.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers is greater than or equal to
        defaultAlPyuJokerFiltering(
            "numberOfPreschoolers.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers is less than or equal to
        defaultAlPyuJokerFiltering(
            "numberOfPreschoolers.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThanOrEqual=" + SMALLER_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers is less than
        defaultAlPyuJokerFiltering(
            "numberOfPreschoolers.lessThan=" + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfPreschoolersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfPreschoolers is greater than
        defaultAlPyuJokerFiltering(
            "numberOfPreschoolers.greaterThan=" + SMALLER_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren equals to
        defaultAlPyuJokerFiltering(
            "numberOfChildren.equals=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.equals=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren in
        defaultAlPyuJokerFiltering(
            "numberOfChildren.in=" + DEFAULT_NUMBER_OF_CHILDREN + "," + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.in=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren is not null
        defaultAlPyuJokerFiltering("numberOfChildren.specified=true", "numberOfChildren.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren is greater than or equal to
        defaultAlPyuJokerFiltering(
            "numberOfChildren.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThanOrEqual=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren is less than or equal to
        defaultAlPyuJokerFiltering(
            "numberOfChildren.lessThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThanOrEqual=" + SMALLER_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren is less than
        defaultAlPyuJokerFiltering(
            "numberOfChildren.lessThan=" + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByNumberOfChildrenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where numberOfChildren is greater than
        defaultAlPyuJokerFiltering(
            "numberOfChildren.greaterThan=" + SMALLER_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice equals to
        defaultAlPyuJokerFiltering("bookingPrice.equals=" + DEFAULT_BOOKING_PRICE, "bookingPrice.equals=" + UPDATED_BOOKING_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice in
        defaultAlPyuJokerFiltering(
            "bookingPrice.in=" + DEFAULT_BOOKING_PRICE + "," + UPDATED_BOOKING_PRICE,
            "bookingPrice.in=" + UPDATED_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice is not null
        defaultAlPyuJokerFiltering("bookingPrice.specified=true", "bookingPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice is greater than or equal to
        defaultAlPyuJokerFiltering(
            "bookingPrice.greaterThanOrEqual=" + DEFAULT_BOOKING_PRICE,
            "bookingPrice.greaterThanOrEqual=" + UPDATED_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice is less than or equal to
        defaultAlPyuJokerFiltering(
            "bookingPrice.lessThanOrEqual=" + DEFAULT_BOOKING_PRICE,
            "bookingPrice.lessThanOrEqual=" + SMALLER_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice is less than
        defaultAlPyuJokerFiltering("bookingPrice.lessThan=" + UPDATED_BOOKING_PRICE, "bookingPrice.lessThan=" + DEFAULT_BOOKING_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingPrice is greater than
        defaultAlPyuJokerFiltering(
            "bookingPrice.greaterThan=" + SMALLER_BOOKING_PRICE,
            "bookingPrice.greaterThan=" + DEFAULT_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee equals to
        defaultAlPyuJokerFiltering("extraFee.equals=" + DEFAULT_EXTRA_FEE, "extraFee.equals=" + UPDATED_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee in
        defaultAlPyuJokerFiltering("extraFee.in=" + DEFAULT_EXTRA_FEE + "," + UPDATED_EXTRA_FEE, "extraFee.in=" + UPDATED_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee is not null
        defaultAlPyuJokerFiltering("extraFee.specified=true", "extraFee.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee is greater than or equal to
        defaultAlPyuJokerFiltering("extraFee.greaterThanOrEqual=" + DEFAULT_EXTRA_FEE, "extraFee.greaterThanOrEqual=" + UPDATED_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee is less than or equal to
        defaultAlPyuJokerFiltering("extraFee.lessThanOrEqual=" + DEFAULT_EXTRA_FEE, "extraFee.lessThanOrEqual=" + SMALLER_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee is less than
        defaultAlPyuJokerFiltering("extraFee.lessThan=" + UPDATED_EXTRA_FEE, "extraFee.lessThan=" + DEFAULT_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByExtraFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where extraFee is greater than
        defaultAlPyuJokerFiltering("extraFee.greaterThan=" + SMALLER_EXTRA_FEE, "extraFee.greaterThan=" + DEFAULT_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice equals to
        defaultAlPyuJokerFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice in
        defaultAlPyuJokerFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice is not null
        defaultAlPyuJokerFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice is greater than or equal to
        defaultAlPyuJokerFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice is less than or equal to
        defaultAlPyuJokerFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice is less than
        defaultAlPyuJokerFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where totalPrice is greater than
        defaultAlPyuJokerFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingStatus equals to
        defaultAlPyuJokerFiltering("bookingStatus.equals=" + DEFAULT_BOOKING_STATUS, "bookingStatus.equals=" + UPDATED_BOOKING_STATUS);
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingStatus in
        defaultAlPyuJokerFiltering(
            "bookingStatus.in=" + DEFAULT_BOOKING_STATUS + "," + UPDATED_BOOKING_STATUS,
            "bookingStatus.in=" + UPDATED_BOOKING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByBookingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where bookingStatus is not null
        defaultAlPyuJokerFiltering("bookingStatus.specified=true", "bookingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByHistoryRefJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where historyRefJason equals to
        defaultAlPyuJokerFiltering(
            "historyRefJason.equals=" + DEFAULT_HISTORY_REF_JASON,
            "historyRefJason.equals=" + UPDATED_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByHistoryRefJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where historyRefJason in
        defaultAlPyuJokerFiltering(
            "historyRefJason.in=" + DEFAULT_HISTORY_REF_JASON + "," + UPDATED_HISTORY_REF_JASON,
            "historyRefJason.in=" + UPDATED_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByHistoryRefJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where historyRefJason is not null
        defaultAlPyuJokerFiltering("historyRefJason.specified=true", "historyRefJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByHistoryRefJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where historyRefJason contains
        defaultAlPyuJokerFiltering(
            "historyRefJason.contains=" + DEFAULT_HISTORY_REF_JASON,
            "historyRefJason.contains=" + UPDATED_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByHistoryRefJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        // Get all the alPyuJokerList where historyRefJason does not contain
        defaultAlPyuJokerFiltering(
            "historyRefJason.doesNotContain=" + UPDATED_HISTORY_REF_JASON,
            "historyRefJason.doesNotContain=" + DEFAULT_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPyuJokerRepository.saveAndFlush(alPyuJoker);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alPyuJoker.setCustomer(customer);
        alPyuJokerRepository.saveAndFlush(alPyuJoker);
        UUID customerId = customer.getId();
        // Get all the alPyuJokerList where customer equals to customerId
        defaultAlPyuJokerShouldBeFound("customerId.equals=" + customerId);

        // Get all the alPyuJokerList where customer equals to UUID.randomUUID()
        defaultAlPyuJokerShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByPersonInChargeIsEqualToSomething() throws Exception {
        EdSheeran personInCharge;
        if (TestUtil.findAll(em, EdSheeran.class).isEmpty()) {
            alPyuJokerRepository.saveAndFlush(alPyuJoker);
            personInCharge = EdSheeranResourceIT.createEntity(em);
        } else {
            personInCharge = TestUtil.findAll(em, EdSheeran.class).get(0);
        }
        em.persist(personInCharge);
        em.flush();
        alPyuJoker.setPersonInCharge(personInCharge);
        alPyuJokerRepository.saveAndFlush(alPyuJoker);
        Long personInChargeId = personInCharge.getId();
        // Get all the alPyuJokerList where personInCharge equals to personInChargeId
        defaultAlPyuJokerShouldBeFound("personInChargeId.equals=" + personInChargeId);

        // Get all the alPyuJokerList where personInCharge equals to (personInChargeId + 1)
        defaultAlPyuJokerShouldNotBeFound("personInChargeId.equals=" + (personInChargeId + 1));
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPyuJokerRepository.saveAndFlush(alPyuJoker);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPyuJoker.setApplication(application);
        alPyuJokerRepository.saveAndFlush(alPyuJoker);
        UUID applicationId = application.getId();
        // Get all the alPyuJokerList where application equals to applicationId
        defaultAlPyuJokerShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPyuJokerList where application equals to UUID.randomUUID()
        defaultAlPyuJokerShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPyuJokersByPropertyIsEqualToSomething() throws Exception {
        AlProty property;
        if (TestUtil.findAll(em, AlProty.class).isEmpty()) {
            alPyuJokerRepository.saveAndFlush(alPyuJoker);
            property = AlProtyResourceIT.createEntity(em);
        } else {
            property = TestUtil.findAll(em, AlProty.class).get(0);
        }
        em.persist(property);
        em.flush();
        alPyuJoker.addProperty(property);
        alPyuJokerRepository.saveAndFlush(alPyuJoker);
        UUID propertyId = property.getId();
        // Get all the alPyuJokerList where property equals to propertyId
        defaultAlPyuJokerShouldBeFound("propertyId.equals=" + propertyId);

        // Get all the alPyuJokerList where property equals to UUID.randomUUID()
        defaultAlPyuJokerShouldNotBeFound("propertyId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPyuJokerFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPyuJokerShouldBeFound(shouldBeFound);
        defaultAlPyuJokerShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPyuJokerShouldBeFound(String filter) throws Exception {
        restAlPyuJokerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuJoker.getId().toString())))
            .andExpect(jsonPath("$.[*].bookingNo").value(hasItem(DEFAULT_BOOKING_NO)))
            .andExpect(jsonPath("$.[*].noteHeitiga").value(hasItem(DEFAULT_NOTE_HEITIGA)))
            .andExpect(jsonPath("$.[*].periodType").value(hasItem(DEFAULT_PERIOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkInDate").value(hasItem(DEFAULT_CHECK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkOutDate").value(hasItem(DEFAULT_CHECK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfAdults").value(hasItem(DEFAULT_NUMBER_OF_ADULTS)))
            .andExpect(jsonPath("$.[*].numberOfPreschoolers").value(hasItem(DEFAULT_NUMBER_OF_PRESCHOOLERS)))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].bookingPrice").value(hasItem(sameNumber(DEFAULT_BOOKING_PRICE))))
            .andExpect(jsonPath("$.[*].extraFee").value(hasItem(sameNumber(DEFAULT_EXTRA_FEE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].bookingStatus").value(hasItem(DEFAULT_BOOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].historyRefJason").value(hasItem(DEFAULT_HISTORY_REF_JASON)));

        // Check, that the count call also returns 1
        restAlPyuJokerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPyuJokerShouldNotBeFound(String filter) throws Exception {
        restAlPyuJokerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPyuJokerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPyuJoker() throws Exception {
        // Get the alPyuJoker
        restAlPyuJokerMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPyuJoker() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuJoker
        AlPyuJoker updatedAlPyuJoker = alPyuJokerRepository.findById(alPyuJoker.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPyuJoker are not directly saved in db
        em.detach(updatedAlPyuJoker);
        updatedAlPyuJoker
            .bookingNo(UPDATED_BOOKING_NO)
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE)
            .extraFee(UPDATED_EXTRA_FEE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);

        restAlPyuJokerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlPyuJoker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlPyuJoker))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPyuJokerToMatchAllProperties(updatedAlPyuJoker);
    }

    @Test
    @Transactional
    void putNonExistingAlPyuJoker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJoker.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuJokerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuJoker.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJoker))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPyuJoker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJoker.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJoker))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPyuJoker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJoker.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJoker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPyuJokerWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuJoker using partial update
        AlPyuJoker partialUpdatedAlPyuJoker = new AlPyuJoker();
        partialUpdatedAlPyuJoker.setId(alPyuJoker.getId());

        partialUpdatedAlPyuJoker
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .toDate(UPDATED_TO_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);

        restAlPyuJokerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuJoker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuJoker))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuJoker in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuJokerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPyuJoker, alPyuJoker),
            getPersistedAlPyuJoker(alPyuJoker)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPyuJokerWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuJoker using partial update
        AlPyuJoker partialUpdatedAlPyuJoker = new AlPyuJoker();
        partialUpdatedAlPyuJoker.setId(alPyuJoker.getId());

        partialUpdatedAlPyuJoker
            .bookingNo(UPDATED_BOOKING_NO)
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE)
            .extraFee(UPDATED_EXTRA_FEE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);

        restAlPyuJokerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuJoker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuJoker))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuJoker in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuJokerUpdatableFieldsEquals(partialUpdatedAlPyuJoker, getPersistedAlPyuJoker(partialUpdatedAlPyuJoker));
    }

    @Test
    @Transactional
    void patchNonExistingAlPyuJoker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJoker.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuJokerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPyuJoker.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuJoker))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPyuJoker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJoker.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuJoker))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPyuJoker() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJoker.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPyuJoker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuJoker in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPyuJoker() throws Exception {
        // Initialize the database
        insertedAlPyuJoker = alPyuJokerRepository.saveAndFlush(alPyuJoker);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPyuJoker
        restAlPyuJokerMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPyuJoker.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPyuJokerRepository.count();
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

    protected AlPyuJoker getPersistedAlPyuJoker(AlPyuJoker alPyuJoker) {
        return alPyuJokerRepository.findById(alPyuJoker.getId()).orElseThrow();
    }

    protected void assertPersistedAlPyuJokerToMatchAllProperties(AlPyuJoker expectedAlPyuJoker) {
        assertAlPyuJokerAllPropertiesEquals(expectedAlPyuJoker, getPersistedAlPyuJoker(expectedAlPyuJoker));
    }

    protected void assertPersistedAlPyuJokerToMatchUpdatableProperties(AlPyuJoker expectedAlPyuJoker) {
        assertAlPyuJokerAllUpdatablePropertiesEquals(expectedAlPyuJoker, getPersistedAlPyuJoker(expectedAlPyuJoker));
    }
}

package com.example.libreria.service;

import com.example.libreria.dto.ReservationRequestDTO;
import com.example.libreria.dto.ReservationResponseDTO;
import com.example.libreria.dto.ReturnBookRequestDTO;
import com.example.libreria.model.Book;
import com.example.libreria.model.Reservation;
import com.example.libreria.model.User;
import com.example.libreria.repository.BookRepository;
import com.example.libreria.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final int MONETARY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ReservationService reservationService;

    private User testUser;
    private Book testBook;
    private Reservation testReservation;
    private ReservationRequestDTO reservationRequestDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Juan Pérez");
        testUser.setEmail("juan@example.com");

        testBook = new Book();
        testBook.setExternalId(258027L);
        testBook.setTitle("The Lord of the Rings");
        testBook.setPrice(new BigDecimal("15.99").setScale(MONETARY_SCALE, ROUNDING_MODE));
        testBook.setStockQuantity(10);
        testBook.setAvailableQuantity(5);

        BigDecimal totalFee = new BigDecimal("111.93").setScale(MONETARY_SCALE, ROUNDING_MODE);

        testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setUser(testUser);
        testReservation.setBook(testBook);
        testReservation.setRentalDays(7);
        testReservation.setStartDate(LocalDate.now());
        testReservation.setExpectedReturnDate(LocalDate.now().plusDays(7));
        testReservation.setDailyRate(testBook.getPrice());
        testReservation.setTotalFee(totalFee);
        testReservation.setLateFee(BigDecimal.ZERO.setScale(MONETARY_SCALE, ROUNDING_MODE));
        testReservation.setStatus(Reservation.ReservationStatus.ACTIVE);
        testReservation.setCreatedAt(LocalDateTime.now());

        reservationRequestDTO = new ReservationRequestDTO(
                1L,
                258027L,
                7,
                LocalDate.now()
        );
    }

    @Test
    void testCreateReservation_Success() {
        when(userService.getUserEntity(anyLong())).thenReturn(testUser);
        when(bookRepository.findByExternalId(anyLong())).thenReturn(Optional.of(testBook));

        Reservation newReservation = new Reservation();
        newReservation.setId(2L);
        newReservation.setUser(testUser);
        newReservation.setBook(testBook);
        newReservation.setRentalDays(7);
        newReservation.setStartDate(LocalDate.now());
        newReservation.setExpectedReturnDate(LocalDate.now().plusDays(7));
        newReservation.setDailyRate(testBook.getPrice());
        newReservation.setTotalFee(new BigDecimal("111.93").setScale(MONETARY_SCALE, ROUNDING_MODE));
        newReservation.setStatus(Reservation.ReservationStatus.ACTIVE);

        when(reservationRepository.save(any(Reservation.class))).thenReturn(newReservation);

        ReservationResponseDTO result = reservationService.createReservation(reservationRequestDTO);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(Reservation.ReservationStatus.ACTIVE, result.getStatus());
        assertEquals(new BigDecimal("111.93").setScale(MONETARY_SCALE, ROUNDING_MODE), result.getTotalFee());
        verify(bookService, times(1)).decreaseAvailableQuantity(testBook.getExternalId());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void testCreateReservation_BookNotAvailable() {
        when(userService.getUserEntity(anyLong())).thenReturn(testUser);
        when(bookRepository.findByExternalId(anyLong())).thenReturn(Optional.of(testBook));

        doThrow(new RuntimeException("No hay libros disponibles para reservar"))
                .when(bookService).decreaseAvailableQuantity(anyLong());

        assertThrows(RuntimeException.class, () -> {
            reservationService.createReservation(reservationRequestDTO);
        }, "Debería lanzar una excepción si el libro no está disponible.");

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void testReturnBook_OnTime() {
        LocalDate onTimeReturnDate = testReservation.getExpectedReturnDate();
        ReturnBookRequestDTO returnRequest = new ReturnBookRequestDTO(onTimeReturnDate);

        Reservation reservationToReturn = new Reservation(
                testReservation.getId(), testReservation.getUser(), testReservation.getBook(),
                testReservation.getRentalDays(), testReservation.getStartDate(),
                testReservation.getExpectedReturnDate(), testReservation.getActualReturnDate(),
                testReservation.getDailyRate(), testReservation.getTotalFee(),
                testReservation.getLateFee(), testReservation.getStatus(), testReservation.getCreatedAt()
        );

        Reservation returnedReservation = reservationToReturn;
        returnedReservation.setActualReturnDate(onTimeReturnDate);
        returnedReservation.setStatus(Reservation.ReservationStatus.RETURNED);
        returnedReservation.setLateFee(BigDecimal.ZERO.setScale(MONETARY_SCALE, ROUNDING_MODE));

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservationToReturn));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(returnedReservation);

        ReservationResponseDTO result = reservationService.returnBook(1L, returnRequest);

        assertNotNull(result);
        assertEquals(Reservation.ReservationStatus.RETURNED, result.getStatus());
        assertEquals(BigDecimal.ZERO.setScale(MONETARY_SCALE, ROUNDING_MODE), result.getLateFee());
        assertEquals(new BigDecimal("111.93").setScale(MONETARY_SCALE, ROUNDING_MODE), result.getTotalFee());
        verify(bookService, times(1)).increaseAvailableQuantity(testBook.getExternalId());
    }

    @Test
    void testReturnBook_Overdue() {
        LocalDate overdueReturnDate = testReservation.getExpectedReturnDate().plusDays(3); // 3 días tarde
        ReturnBookRequestDTO returnRequest = new ReturnBookRequestDTO(overdueReturnDate);

        BigDecimal expectedLateFee = new BigDecimal("7.20").setScale(MONETARY_SCALE, ROUNDING_MODE);
        BigDecimal expectedTotalFee = new BigDecimal("119.13").setScale(MONETARY_SCALE, ROUNDING_MODE);

        Reservation reservationToReturn = new Reservation(
                testReservation.getId(), testReservation.getUser(), testReservation.getBook(),
                testReservation.getRentalDays(), testReservation.getStartDate(),
                testReservation.getExpectedReturnDate(), testReservation.getActualReturnDate(),
                testReservation.getDailyRate(), testReservation.getTotalFee(),
                testReservation.getLateFee(), testReservation.getStatus(), testReservation.getCreatedAt()
        );

        Reservation overdueReservation = reservationToReturn;
        overdueReservation.setActualReturnDate(overdueReturnDate);
        overdueReservation.setStatus(Reservation.ReservationStatus.OVERDUE);
        overdueReservation.setLateFee(expectedLateFee);
        overdueReservation.setTotalFee(expectedTotalFee);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservationToReturn));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(overdueReservation);

        ReservationResponseDTO result = reservationService.returnBook(1L, returnRequest);

        assertNotNull(result);
        assertEquals(Reservation.ReservationStatus.OVERDUE, result.getStatus());
        assertEquals(expectedLateFee, result.getLateFee());
        assertEquals(expectedTotalFee, result.getTotalFee());
        verify(bookService, times(1)).increaseAvailableQuantity(testBook.getExternalId());
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        ReservationResponseDTO result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(testReservation.getId(), result.getId());
    }

    @Test
    void testGetAllReservations() {
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setUser(testUser);
        reservation2.setBook(testBook);
        reservation2.setDailyRate(new BigDecimal("10.00"));
        reservation2.setTotalFee(new BigDecimal("50.00"));
        reservation2.setLateFee(BigDecimal.ZERO);
        reservation2.setStatus(Reservation.ReservationStatus.ACTIVE);
        reservation2.setCreatedAt(LocalDateTime.now());

        when(reservationRepository.findAll()).thenReturn(Arrays.asList(testReservation, reservation2));

        List<ReservationResponseDTO> result = reservationService.getAllReservations();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetReservationsByUserId() {
        when(reservationRepository.findByUserId(1L)).thenReturn(Arrays.asList(testReservation));

        List<ReservationResponseDTO> result = reservationService.getReservationsByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetActiveReservations() {
        when(reservationRepository.findByStatus(Reservation.ReservationStatus.ACTIVE))
                .thenReturn(Arrays.asList(testReservation));

        List<ReservationResponseDTO> result = reservationService.getActiveReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
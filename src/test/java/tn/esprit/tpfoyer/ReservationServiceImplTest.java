package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservation = new Reservation("1", new Date(), true, null); // Mocked reservation data
    }

    @Test
    void testRetrieveAllReservations() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        // Act
        List<Reservation> reservations = reservationService.retrieveAllReservations();

        // Assert
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));

        // Act
        Reservation foundReservation = reservationService.retrieveReservation("1");

        // Assert
        assertNotNull(foundReservation);
        assertEquals("1", foundReservation.getIdReservation());
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testAddReservation() {
        // Arrange
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation addedReservation = reservationService.addReservation(reservation);

        // Assert
        assertNotNull(addedReservation);
        assertEquals("1", addedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testModifyReservation() {
        // Arrange
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation modifiedReservation = reservationService.modifyReservation(reservation);

        // Assert
        assertNotNull(modifiedReservation);
        assertEquals("1", modifiedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testTrouverResSelonDateEtStatus() {
        // Arrange
        Date date = new Date();
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, true)).thenReturn(List.of(reservation));

        // Act
        List<Reservation> reservations = reservationService.trouverResSelonDateEtStatus(date, true);

        // Assert
        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(date, true);
    }

    @Test
    void testRemoveReservation() {
        // Arrange
        doNothing().when(reservationRepository).deleteById(anyString());

        // Act
        reservationService.removeReservation("1");

        // Assert
        verify(reservationRepository, times(1)).deleteById("1");
    }
}

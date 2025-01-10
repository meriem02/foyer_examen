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

    // ✅ Test retrieveAllReservations
    @Test
    void testRetrieveAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.retrieveAllReservations();

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    // ✅ Test retrieveReservation - Success case
    @Test
    void testRetrieveReservation() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.retrieveReservation("1");

        assertNotNull(foundReservation);
        assertEquals("1", foundReservation.getIdReservation());
        verify(reservationRepository, times(1)).findById("1");
    }

    // ✅ Test retrieveReservation - Not Found case
    @Test
    void testRetrieveReservationNotFound() {
        when(reservationRepository.findById("2")).thenReturn(Optional.empty());

        Reservation foundReservation = reservationService.retrieveReservation("2");

        assertNull(foundReservation);
        verify(reservationRepository, times(1)).findById("2");
    }

    // ✅ Test addReservation
    @Test
    void testAddReservation() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation addedReservation = reservationService.addReservation(reservation);

        assertNotNull(addedReservation);
        assertEquals("1", addedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    // ✅ Test modifyReservation
    @Test
    void testModifyReservation() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation modifiedReservation = reservationService.modifyReservation(reservation);

        assertNotNull(modifiedReservation);
        assertEquals("1", modifiedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    // ✅ Test trouverResSelonDateEtStatus - Success case
    @Test
    void testTrouverResSelonDateEtStatus() {
        Date date = new Date();
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, true)).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.trouverResSelonDateEtStatus(date, true);

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(date, true);
    }

    // ✅ Test trouverResSelonDateEtStatus - Empty result case
    @Test
    void testTrouverResSelonDateEtStatusNoResults() {
        Date date = new Date();
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, true)).thenReturn(List.of());

        List<Reservation> reservations = reservationService.trouverResSelonDateEtStatus(date, true);

        assertNotNull(reservations);
        assertEquals(0, reservations.size());
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(date, true);
    }

    // ✅ Test removeReservation
    @Test
    void testRemoveReservation() {
        doNothing().when(reservationRepository).deleteById("1");

        reservationService.removeReservation("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }

    // ✅ Test removeReservation - Not Found case
    @Test
    void testRemoveReservationNotFound() {
        doThrow(new RuntimeException("Reservation not found")).when(reservationRepository).deleteById("2");

        assertThrows(RuntimeException.class, () -> reservationService.removeReservation("2"));
        verify(reservationRepository, times(1)).deleteById("2");
    }

    // ✅ Test addReservation - Null case
    @Test
    void testAddReservationNull() {
        when(reservationRepository.save(null)).thenThrow(new IllegalArgumentException("Reservation cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> reservationService.addReservation(null));
        verify(reservationRepository, times(1)).save(null);
    }

    // ✅ Test modifyReservation - Null case
    @Test
    void testModifyReservationNull() {
        when(reservationRepository.save(null)).thenThrow(new IllegalArgumentException("Reservation cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> reservationService.modifyReservation(null));
        verify(reservationRepository, times(1)).save(null);
    }
}

package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Creating a mock Reservation object for use in tests
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void testRetrieveAllReservations() {
        // Mocking repository response
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        // Calling the service method
        List<Reservation> reservations = reservationService.retrieveAllReservations();

        // Asserting that the result is correct
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.size()).isEqualTo(1);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        // Mocking repository response for retrieving a reservation by ID
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        // Calling the service method
        Reservation result = reservationService.retrieveReservation("1");

        // Asserting that the result is correct
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testRetrieveReservationNotFound() {
        // Mocking repository response for a non-existent reservation
        when(reservationRepository.findById("999")).thenReturn(Optional.empty());

        // Calling the service method
        Reservation result = reservationService.retrieveReservation("999");

        // Asserting that the result is null when not found
        assertThat(result).isNull();
        verify(reservationRepository, times(1)).findById("999");
    }

    @Test
    void testAddReservation() {
        // Mocking repository response for adding a reservation
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Calling the service method
        Reservation result = reservationService.addReservation(reservation);

        // Asserting that the result is correct
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testModifyReservation() {
        // Mocking repository response for modifying a reservation
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Calling the service method
        Reservation result = reservationService.modifyReservation(reservation);

        // Asserting that the result is correct
        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testRemoveReservation() {
        // Mocking repository behavior for removing a reservation
        doNothing().when(reservationRepository).deleteById("1");

        // Calling the service method
        reservationService.removeReservation("1");

        // Verifying that the delete method was called
        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    void testRetrieveReservationParDateEtStatus() {
        // Mocking repository response for reservations based on date and status
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        // Calling the service method
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);

        // Asserting that the result is not empty
        assertThat(result).isNotEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }

    @Test
    void testRetrieveReservationParDateEtStatusNoResults() {
        // Mocking repository response for no results
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(false)))
                .thenReturn(Arrays.asList());  // Simulating no reservations

        // Calling the service method
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), false);

        // Asserting that the result is empty
        assertThat(result).isEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(false));
    }
}

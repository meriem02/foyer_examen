package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.entity.Etudiant; // Add the Etudiant import
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Prepare the reservation object
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        // Initialize the Etudiants set with mock Etudiant objects
        Etudiant etudiant1 = mock(Etudiant.class);
        Etudiant etudiant2 = mock(Etudiant.class);
        reservation.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2)));
    }

    @Test
    void testRetrieveAllReservations() {
        // Arrange: Mock the repository call to return a list with the reservation
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        // Act: Call the service method
        List<Reservation> reservations = reservationService.retrieveAllReservations();

        // Assert: Check the results and verify the interactions
        assertThat(reservations).isNotEmpty();
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        // Arrange: Mock the repository call to return an Optional with the reservation
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        // Act: Call the service method
        Reservation result = reservationService.retrieveReservation("1");

        // Assert: Check the results and verify the interactions
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testAddReservation() {
        // Arrange: Mock the repository call to return the saved reservation
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Act: Call the service method
        Reservation result = reservationService.addReservation(reservation);

        // Assert: Check the results and verify the interactions
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testModifyReservation() {
        // Arrange: Mock the repository call to return the modified reservation
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Act: Call the service method
        Reservation result = reservationService.modifyReservation(reservation);

        // Assert: Check the results and verify the interactions
        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testRemoveReservation() {
        // Arrange: Mock the repository call to do nothing when deleting
        doNothing().when(reservationRepository).deleteById("1");

        // Act: Call the service method
        reservationService.removeReservation("1");

        // Assert: Verify the delete operation is called once
        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    void testTrouverResSelonDateEtStatus() {
        // Arrange: Mock the repository call with a specific date and status
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        // Act: Call the service method
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);

        // Assert: Check the results and verify the interactions
        assertThat(result).isNotEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }
}

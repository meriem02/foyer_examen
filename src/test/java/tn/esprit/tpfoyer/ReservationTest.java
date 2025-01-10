package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        Etudiant etudiant1 = mock(Etudiant.class);
        Etudiant etudiant2 = mock(Etudiant.class);
        reservation.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2)));
    }

    // Test de duplication
    @Test
    void testAddDuplicateReservation() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation newReservation = new Reservation();
        newReservation.setIdReservation("1");

        assertThrows(IllegalArgumentException.class, () -> reservationService.addReservation(newReservation));

        verify(reservationRepository, never()).save(newReservation);
    }

    // Test de suppression de réservation inexistante
    @Test
    void testRemoveNonExistentReservation() {
        doThrow(new NoSuchElementException("Reservation not found")).when(reservationRepository).deleteById("99");

        assertThrows(NoSuchElementException.class, () -> reservationService.removeReservation("99"));

        verify(reservationRepository, times(1)).deleteById("99");
    }

    // Test de modification avec données nulles
    @Test
    void testModifyReservationWithNullData() {
        assertThrows(IllegalArgumentException.class, () -> reservationService.modifyReservation(null));
        verify(reservationRepository, never()).save(any());
    }

    // Test de recherche avec ID null
    @Test
    void testRetrieveReservationWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> reservationService.retrieveReservation(null));
        verify(reservationRepository, never()).findById(null);
    }

    // Test de suppression avec ID null
    @Test
    void testRemoveReservationWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> reservationService.removeReservation(null));
        verify(reservationRepository, never()).deleteById(null);
    }

    // Test de recherche par date et statut avec une liste vide
    @Test
    void testTrouverResSelonDateEtStatusWithEmptyResult() {
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Collections.emptyList());

        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);

        assertThat(result).isEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }

    // Test de mise à jour d'une réservation inexistante
    @Test
    void testModifyNonExistentReservation() {
        Reservation nonExistentReservation = new Reservation();
        nonExistentReservation.setIdReservation("99");

        when(reservationRepository.save(nonExistentReservation)).thenReturn(nonExistentReservation);

        Reservation result = reservationService.modifyReservation(nonExistentReservation);
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("99");
    }

    // Test de validation de la liste d'étudiants
    @Test
    void testAddReservationWithEmptyEtudiants() {
        Reservation reservationWithNoEtudiants = new Reservation();
        reservationWithNoEtudiants.setIdReservation("2");
        reservationWithNoEtudiants.setEtudiants(new HashSet<>());

        assertThrows(IllegalArgumentException.class, () -> reservationService.addReservation(reservationWithNoEtudiants));
        verify(reservationRepository, never()).save(any());
    }

    // Test pour vérifier que la méthode findAll() est appelée sans répétition
    @Test
    void testRetrieveAllReservationsCalledOnce() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationService.retrieveAllReservations();
        assertThat(reservations).isNotEmpty();

        verify(reservationRepository, times(1)).findAll();
    }

    // Test avec suppression et vérification de l'interaction
    @Test
    void testRemoveReservationAndVerifyInteraction() {
        doNothing().when(reservationRepository).deleteById("1");
        reservationService.removeReservation("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }
}

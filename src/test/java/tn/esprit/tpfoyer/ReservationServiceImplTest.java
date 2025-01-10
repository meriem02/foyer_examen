package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Création de la réservation avec un id, une date, et une validation
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        // Optionnel : initialisation de la relation ManyToMany avec des étudiants
        Set<Etudiant> etudiants = new HashSet<>();
        reservation.setEtudiants(etudiants);
    }

    @Test
    void testRetrieveAllReservations() {
        // Given
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        // When
        List<Reservation> result = reservationService.retrieveAllReservations();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        // Given
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        // When
        Reservation result = reservationService.retrieveReservation("1");

        // Then
        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testAddReservation() {
        // Given
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // When
        Reservation result = reservationService.addReservation(reservation);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testModifyReservation() {
        // Given
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // When
        Reservation result = reservationService.modifyReservation(reservation);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testTrouverResSelonDateEtStatus() {
        // Given
        Date date = new Date();
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, true))
                .thenReturn(reservations);

        // When
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(date, true);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(date, true);
    }

    @Test
    void testRemoveReservation() {
        // When
        reservationService.removeReservation("1");

        // Then
        verify(reservationRepository, times(1)).deleteById("1");
    }
}

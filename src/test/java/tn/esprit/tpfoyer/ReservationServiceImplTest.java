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
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void testRetrieveAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationService.retrieveAllReservations();

        assertThat(reservations).isNotEmpty();
        assertThat(reservations.size()).isEqualTo(1);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveReservation() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.retrieveReservation("1");

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testRetrieveReservationNotFound() {
        when(reservationRepository.findById("999")).thenReturn(Optional.empty());  // Simuler l'absence de la réservation

        Reservation result = reservationService.retrieveReservation("999");

        assertThat(result).isNull();  // Vérifier que la réservation est null
        verify(reservationRepository, times(1)).findById("999");
    }

    @Test
    void testAddReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.addReservation(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testModifyReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.modifyReservation(reservation);

        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testRemoveReservation() {
        doNothing().when(reservationRepository).deleteById("1");

        reservationService.removeReservation("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    void testRetrieveReservationParDateEtStatus() {
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);

        assertThat(result).isNotEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }

    @Test
    void testRetrieveReservationParDateEtStatusNoResults() {
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(false)))
                .thenReturn(Arrays.asList());  // Simuler aucune réservation

        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), false);

        assertThat(result).isEmpty();  // Vérifier que la liste est vide
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(false));
    }
}

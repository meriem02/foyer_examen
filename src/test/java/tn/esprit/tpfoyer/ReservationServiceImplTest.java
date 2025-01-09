package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceAndControllerTest {

    // Service Mock
    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    // Controller Mock
    @InjectMocks
    private ReservationRestController reservationRestController;

    // Data
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    // Tests pour le service
    @Test
    void testServiceRetrieveAllReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));
        List<Reservation> reservations = reservationService.retrieveAllReservations();
        assertThat(reservations).isNotEmpty();
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testServiceRetrieveReservation() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        Reservation result = reservationService.retrieveReservation("1");
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testServiceAddReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        Reservation result = reservationService.addReservation(reservation);
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testServiceModifyReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        Reservation result = reservationService.modifyReservation(reservation);
        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testServiceRemoveReservation() {
        doNothing().when(reservationRepository).deleteById("1");
        reservationService.removeReservation("1");
        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    void testServiceTrouverResSelonDateEtStatus() {
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);
        assertThat(result).isNotEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }

    // Tests pour le contrôleur
    @Test
    void testControllerGetReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationRestController.getReservations();

        assertThat(reservations).isNotEmpty();
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testControllerRetrieveReservation() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationRestController.retrieveReservation("1");

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void testControllerAddReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationRestController.addReservation(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testControllerRemoveReservation() {
        doNothing().when(reservationRepository).deleteById("1");

        reservationRestController.removeReservation("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    void testControllerModifyReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationRestController.modifyReservation(reservation);

        assertThat(result).isNotNull();
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testControllerRetrieveReservationParDateEtStatus() {
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(new Date(), true);

        assertThat(result).isNotEmpty();
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }
}

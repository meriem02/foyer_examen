package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationRepositoryTest {

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        // Initialiser les mocks
        MockitoAnnotations.openMocks(this);

        // Créer une réservation de test
        reservation = new Reservation();
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    public void testFindAllByAnneeUniversitaireBeforeAndEstValide() {
        // Simuler le comportement du repository
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        // Appeler la méthode simulée
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(new Date(), true);

        // Vérifier le résultat
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));

        // Vérifier que la méthode du repository a été appelée
        verify(reservationRepository).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }
}

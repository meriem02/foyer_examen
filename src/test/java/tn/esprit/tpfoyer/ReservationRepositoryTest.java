package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
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
    void testSaveReservation() {
        Reservation savedReservation = reservationRepository.save(reservation);

        assertThat(savedReservation).isNotNull();
        assertThat(savedReservation.getIdReservation()).isEqualTo("1");
    }

    @Test
    void testFindById() {
        reservationRepository.save(reservation);

        Optional<Reservation> foundReservation = reservationRepository.findById("1");
        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getIdReservation()).isEqualTo("1");
    }

    @Test
    void testFindByIdNotFound() {
        Optional<Reservation> foundReservation = reservationRepository.findById("999");
        assertThat(foundReservation).isNotPresent();
    }

    @Test
    void testDeleteReservation() {
        reservationRepository.save(reservation);

        reservationRepository.deleteById("1");
        Optional<Reservation> foundReservation = reservationRepository.findById("1");
        assertThat(foundReservation).isNotPresent();
    }

    @Test
    void testFindAll() {
        reservationRepository.save(reservation);
        assertThat(reservationRepository.findAll()).hasSize(1);
    }
}

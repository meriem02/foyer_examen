package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Reservation;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void testGetIdReservation() {
        assertThat(reservation.getIdReservation()).isEqualTo("1");
    }

    @Test
    void testSetIdReservation() {
        reservation.setIdReservation("2");
        assertThat(reservation.getIdReservation()).isEqualTo("2");
    }

    @Test
    void testGetAnneeUniversitaire() {
        assertThat(reservation.getAnneeUniversitaire()).isNotNull();
    }

    @Test
    void testSetAnneeUniversitaire() {
        Date newDate = new Date();
        reservation.setAnneeUniversitaire(newDate);
        assertThat(reservation.getAnneeUniversitaire()).isEqualTo(newDate);
    }

    @Test
    void testEqualsAndHashCode() {
        Reservation anotherReservation = new Reservation();
        anotherReservation.setIdReservation("1");
        anotherReservation.setAnneeUniversitaire(new Date());
        anotherReservation.setEstValide(true);

        // Check equality
        assertThat(reservation).isEqualTo(anotherReservation);

        // Check hashcode
        assertThat(reservation.hashCode()).isEqualTo(anotherReservation.hashCode());
    }

    @Test
    void testNotEqualReservations() {
        Reservation anotherReservation = new Reservation();
        anotherReservation.setIdReservation("2");

        // Check inequality based on id
        assertThat(reservation).isNotEqualTo(anotherReservation);
    }
}

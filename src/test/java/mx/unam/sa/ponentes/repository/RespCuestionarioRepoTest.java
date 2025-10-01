package mx.unam.sa.ponentes.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import mx.unam.sa.ponentes.models.RespCuestionario;



@Slf4j
@SpringBootTest
public class RespCuestionarioRepoTest {
    @Autowired
    private RespCuestionarioRepo respCuestionarioRepo;

    @Test
    void testCountByUserIdAndCuestionarioIdCuestionario() {
        System.out.println("----------countByUserIdAndCuestionarioIdCuestionario");
        int count = respCuestionarioRepo.countByCuestionarioIdCuestionarioAndStatus(1L, 1);
        log.info("count: " + count);
        System.out.println("----------");
    }


    @Test
    void testFindoDoctosRub() {
        System.out.println("----------findDoctosRub");
        List<Integer> salida = respCuestionarioRepo.findDoctosRub(1L);
        salida.forEach(s -> {
            log.info("DoctoRub: " + s);
        });
        System.out.println("----------");
    }   

    @Test
    @Transactional
    void testFindByCuestionarioIdCuestionarioAndStatus() {
        System.out.println("----------testFindByCuestionarioIdCuestionarioAndStatus---");
        int status = 4;  //Terminado
        List<RespCuestionario> salida = respCuestionarioRepo.findByCuestionarioIdCuestionarioAndStatus(1L, status);
        salida.forEach(s -> {
            log.info("RespCuestionario: " + s);
        });
        System.out.println("----------");
    }

    @Test
    @Transactional
    void testFindByCuestionarioIdCuestionarioAndStatusAndFechaTerminadoBetween() {
        System.out.println("----------testFindByCuestionarioIdCuestionarioAndStatus---");
        int status = 4;  //Terminado

        LocalDateTime start = LocalDate.of(2025, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(2025, 12, 31).atTime(23, 59, 59);

        List<RespCuestionario> salida = respCuestionarioRepo.findByCuestionarioIdCuestionarioAndStatusAndFechaTerminadoBetween(1L, status, start, end);

        //List<RespCuestionario> salida = respCuestionarioRepo.findByCuestionarioIdCuestionarioAndStatus(1L, status);

        salida.forEach(s -> {
            log.info("RespCuestionario: " + s.getIdRespCuestionario());
        });
        System.out.println("----------");
    }

    @Test
    void testFindDistinctYears() {
        System.out.println("----------testFindDistinctYears---");
        List<Integer> salida = respCuestionarioRepo.findDistinctYears(1L);
        salida.forEach(s -> {
            log.info("Año: " + s);
        });
        System.out.println("----------");
    }
}

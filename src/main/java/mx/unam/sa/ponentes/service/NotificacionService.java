package mx.unam.sa.ponentes.service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.unam.sa.ponentes.dto.NotificacionDTO;
import mx.unam.sa.ponentes.models.Notificacion;
import mx.unam.sa.ponentes.repository.NotificacionesRepo;
import mx.unam.sa.ponentes.utils.Utils;


@Service
public class NotificacionService {
    @Autowired
    private NotificacionesRepo notificacionesRepo;

    public Notificacion save(Notificacion notificacion) {
        return notificacionesRepo.save(notificacion);
    }

    public List<NotificacionDTO> findByIdCuestionarioAndidUserActual(Long idCuestionario, Integer idUser) {

        LocalDate firstDayOfYear = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        Date firstDate = java.sql.Date.valueOf(firstDayOfYear);

        List<Notificacion> notificaciones = notificacionesRepo
                .findByIdCuestionarioAndIdUserAndRevisadaAndStatusAndFecRegBetween(idCuestionario, idUser, false, 1,
                        firstDate, new Date());
        List<NotificacionDTO> notificacionesDTO = new ArrayList<>();

        for (Notificacion notificacion : notificaciones) {
            NotificacionDTO notificacionDTO = new NotificacionDTO();
            notificacionDTO.setIdNotificacion(notificacion.getIdNotifica());
            notificacionDTO.setNotificacion(notificacion.getNotificacion());
            notificacionDTO.setUrlDestino(notificacion.getUrlDestino());
            notificacionDTO.setFecReg(Utils.formatFechaHM(notificacion.getFecReg()));
            notificacionDTO.setParam(Utils.encode(notificacion.getIdNotifica().toString()));
            notificacionesDTO.add(notificacionDTO);
        }

        return notificacionesDTO;
    }

    public void quitaRevisa(Long id) throws Exception {
        Notificacion notificacion = notificacionesRepo.findById(id)
                .orElseThrow(() -> new Exception("Notificacion no encontrada"));
        notificacion.setRevisada(true);
        notificacionesRepo.save(notificacion);
    }

    public List<NotificacionDTO> findByIdUserAndidCuestionario(Integer idUser, Long idRespCuestionario) {
        List<Notificacion> notificaciones = notificacionesRepo.findByIdUserAndIdCuestionario(idUser,
                idRespCuestionario);
        List<NotificacionDTO> notificacionesDTO = new ArrayList<>();

        for (Notificacion notificacion : notificaciones) {
            NotificacionDTO notificacionDTO = new NotificacionDTO();
            notificacionDTO.setIdNotificacion(notificacion.getIdNotifica());
            notificacionDTO.setNotificacion(notificacion.getNotificacion());
            notificacionDTO.setUrlDestino(notificacion.getUrlDestino());
            notificacionDTO.setFecReg(Utils.formatFechaHM(notificacion.getFecReg()));
            notificacionesDTO.add(notificacionDTO);
        }
        return notificacionesDTO;
    }

    public Integer countNotificaciones(Integer idUser, Long idCuestionario) {
        return notificacionesRepo.countByIdUserAndIdCuestionario(idUser, idCuestionario);
    }

}
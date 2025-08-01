package mx.unam.sa.ponentes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.dto.CuesEvaluaDTO;
import mx.unam.sa.ponentes.dto.CuestDetalleDTO;
import mx.unam.sa.ponentes.dto.CuestionarioDTO;
import mx.unam.sa.ponentes.models.Cuestionario;
import mx.unam.sa.ponentes.models.RespCuestionario;
import mx.unam.sa.ponentes.models.Respuesta;
import mx.unam.sa.ponentes.repository.CatContenidoRepository;
import mx.unam.sa.ponentes.repository.CuestionarioRepository;
import mx.unam.sa.ponentes.repository.RespCuestionarioRepo;
import mx.unam.sa.ponentes.repository.RespuestaRep;
import mx.unam.sa.ponentes.repository.UserRepository;
import mx.unam.sa.ponentes.utils.Utils;

@Service
public class CuestionarioService {
    @Autowired
    CuestionarioRepository cuestionarioRepository;

    @Autowired
    CatContenidoRepository contenidoRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CuestionarioRepository cuestionarioRep;

    @Autowired
    RespCuestionarioRepo respCuestionarioRep;

    @Autowired
    RespuestaRep respuestaRep;

    @Autowired
    DocumentoService documentosService;

    @Autowired
    FolioService folioService;

    public Cuestionario findById(Long id) {
        return cuestionarioRepository.findById(id).get();
    }

    public List<CuestionarioDTO> getAllCuestionarios() {
        List<Cuestionario> Cuestionarios = cuestionarioRepository.findByStatus(1);
        List<CuestionarioDTO> cuestionariosDTO = new ArrayList<>();

        for (Cuestionario cuestionario : Cuestionarios) {
            CuestionarioDTO cuestionarioDTO = new CuestionarioDTO();
            cuestionarioDTO.setId(cuestionario.getIdCuestionario());
            cuestionarioDTO.setTitulo(cuestionario.getTitulo());
            cuestionarioDTO.setSubtitulo(cuestionario.getSubtitulo());
            cuestionarioDTO.setContenido(cuestionario.getContenido());
            cuestionarioDTO.setFechaCreacion(Utils.formatFecha(cuestionario.getFecReg()));
            cuestionariosDTO.add(cuestionarioDTO);
        }

        return cuestionariosDTO;

    }

    @Transactional
    public RespCuestionario saveNuevasRespuestas(Long idCuestionario, List<Respuesta> respuestas, String user,
            String claveCuestionario) throws RuntimeException {

        RespCuestionario respCuestionario = new RespCuestionario();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Cuestionario cuestionario = cuestionarioRep.findById(idCuestionario).get();
        respCuestionario.setCuestionario(cuestionario);
        //respCuestionario.setUser(user);
        String folio = folioService.getFolio(currentYear, idCuestionario, claveCuestionario);
        respCuestionario.setFolio(folio);
        respCuestionarioRep.save(respCuestionario);

        // Asociar las respuestas al cuestionario, esto es necesario pues antes no se
        // conocía el id del respcuestionario
        for (Respuesta respuesta : respuestas) {
            respuesta.setRespCuestionario(respCuestionario);
        }
        respuestaRep.saveAll(respuestas);

        return respCuestionario;
    }

    public RespCuestionario findRespCuestionarioById(Long idRespCuestionario) {
        return respCuestionarioRep.findById(idRespCuestionario).get();
    }

    public List<CuesEvaluaDTO> getCuestionariosListEvaluacion() {
        List<CuestionarioDTO> cuestionarios = this.getAllCuestionarios();
        List<CuesEvaluaDTO> cuestionariosSal = new ArrayList<>();

        cuestionarios.stream().forEach(c -> {
            CuesEvaluaDTO ced = new CuesEvaluaDTO();
            ced.setId(c.getId());
            ced.setTitulo(c.getTitulo());
            ced.setSubtitulo(c.getSubtitulo());
            ced.setCapturados(0);
            ced.setEvaluados(0);
            ced.setDictamenincompleto(0);
            ced.setTerminados(0);
            cuestionariosSal.add(ced);
        });

        return cuestionariosSal;
    }

    public List<CuesEvaluaDTO> getCuestionariosListEvaluacion(List<Long> idCuestionarios) {
        List<Cuestionario> cuestionarios = cuestionarioRep.findByidCuestionarioIn(idCuestionarios);

        List<CuesEvaluaDTO> cuestionariosSal = new ArrayList<>();

        cuestionarios.stream().forEach(c -> {
            CuesEvaluaDTO ced = new CuesEvaluaDTO();
            ced.setId(c.getIdCuestionario());
            ced.setTitulo(c.getTitulo());
            ced.setSubtitulo(c.getSubtitulo());
            ced.setCapturados(0);
            ced.setEvaluados(0);
            ced.setDictamenincompleto(0);
            ced.setTerminados(0);
            cuestionariosSal.add(ced);
        });

        return cuestionariosSal;
    }

    /**
     * Obtiene los cuestionarios capturados por el usuario
     * 
     * @param idCuestionario identificador del cuestionario
     * @param status         status del cuestionario, 1 capturado, 2 evaluado, 3
     *                       revisado, 4 terminado
     * @param acceso         acceso al cuestionario en caso de que sea propio no es
     *                       posible acceder para el caso 2 y 3
     * @param idUser         identificador del usuario para saber si el cuestionario
     *                       es propio
     * @param muestraTodos   muestra todos los cuestionarios
     */
    public List<CuestDetalleDTO> getDetalleCuestionario(Long idCuestionario, int status,
            String acceso, String username, boolean muestraTodos) {

        List<RespCuestionario> respCuestionarios = null;

        respCuestionarios = respCuestionarioRep
                .findByCuestionarioIdCuestionarioAndStatus(idCuestionario, status);

        List<CuestDetalleDTO> detalles = new ArrayList<>();

        respCuestionarios.stream().forEach(rc -> {
            CuestDetalleDTO cdd = new CuestDetalleDTO();
            cdd.setIdRespCuestionario(rc.getIdRespCuestionario());
            cdd.setUsuario(rc.getUser().getUsername());
            cdd.setFechaCaptura(Utils.formatFecha(rc.getFecReg()));
            cdd.setFechaRevision(Utils.formatFecha(rc.getFechaRevisado()));
            cdd.setParam(Utils.encode("idRespCuestionario=" + rc.getIdRespCuestionario() + "&acceso=" + acceso));
            cdd.setFolio(rc.getFolio());
            
            detalles.add(cdd);
        });

        return detalles;
    }

    /**
     * Obtiene los cuestionarios capturados por el usuario por año
     * 
     * @param idCuestionario identificador del cuestionario
     * @param status         status del cuestionario, 1 capturado, 2 evaluado, 3
     *                       revisado, 4 terminado
     * @param acceso         acceso al cuestionario en caso de que sea propio no es
     *                       posible acceder para el caso 2 y 3
     * @param idUser         identificador del usuario para saber si el cuestionario
     *                       es propio
     * @param muestraTodos   muestra todos los cuestionarios
     */
    public List<CuestDetalleDTO> getDetalleCuestionarioAnio(Long idCuestionario, int status,
            String acceso, String username, boolean muestraTodos, int anio) {

        
        List<RespCuestionario> respCuestionarios = null;

        LocalDateTime start = LocalDate.of(anio, 1, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(anio, 12, 31).atTime(23, 59, 59);

        
            respCuestionarios = respCuestionarioRep
                    .findByCuestionarioIdCuestionarioAndStatusAndFechaTerminadoBetween(idCuestionario, status, start,
                            end);
        

        List<CuestDetalleDTO> detalles = new ArrayList<>();

        respCuestionarios.stream().forEach(rc -> {
            CuestDetalleDTO cdd = new CuestDetalleDTO();
            cdd.setIdRespCuestionario(rc.getIdRespCuestionario());
            cdd.setUsuario(rc.getUser().getUsername());
            cdd.setFechaCaptura(Utils.formatFecha(rc.getFecReg()));
            cdd.setFechaRevision(Utils.formatFecha(rc.getFechaRevisado()));
            cdd.setParam(Utils.encode("idRespCuestionario=" + rc.getIdRespCuestionario() + "&acceso=" + acceso));
            cdd.setFolio(rc.getFolio());

           
            detalles.add(cdd);
        });

        return detalles;
    }

    public void cambiaStatusCaptura(Long idCuestionario, int statusActual, int statusNuevo) {
        List<RespCuestionario> respCuestionarios = respCuestionarioRep
                .findByCuestionarioIdCuestionarioAndStatus(idCuestionario, statusActual);
        respCuestionarios.stream().forEach(rc -> {
            rc.setStatus(statusNuevo);
            Date fecha = new Date();
            rc.setCambioCaptura(fecha);
            respCuestionarioRep.save(rc);
        });
    }

    public Cuestionario getCuestionario(Long idCuestionario) {
        return cuestionarioRep.findById(idCuestionario).get();
    }

    public List<Integer> getDistinctYears(Long idCuestionario) {
        return respCuestionarioRep.findDistinctYears(idCuestionario);
    }
}

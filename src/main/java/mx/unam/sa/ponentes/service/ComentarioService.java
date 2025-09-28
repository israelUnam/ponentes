package mx.unam.sa.ponentes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.unam.sa.ponentes.dto.ComentariosDTO;
import mx.unam.sa.ponentes.models.ComentariosSimple;
import mx.unam.sa.ponentes.models.User;
import mx.unam.sa.ponentes.repository.ComentariosSimpleRepo;
import mx.unam.sa.ponentes.utils.Utils;


@Service
public class ComentarioService {
    @Autowired
    private ComentariosSimpleRepo comentariosSimpleRepo;


    public List<ComentariosDTO> getComentariosOtrosUsuarios(Long idResp, Long idTema, Integer userid, String username) {

        List<ComentariosSimple> comentariosSimple = comentariosSimpleRepo
                .findByidUserNotAndIdRespCuestionarioAndIdTemaOrderByFecRegAsc(userid, idResp, idTema);

        List<ComentariosDTO> comentarios = comentariosSimple.stream().map(c -> {
            ComentariosDTO comentario = new ComentariosDTO();
            comentario.setIdComentario(c.getIdComentario());
            comentario.setComentario(c.getComentario());
            comentario.setFechaAct(Utils.formatFechaHM(c.getFecReg()));
            comentario.setIdRespCuestionario(c.getIdRespCuestionario());
            comentario.setIdTema(c.getIdTema());
            comentario.setIdUser(c.getIdUser());
            comentario.setUsuario(username);
            return comentario;
        }).collect(Collectors.toList());
        return comentarios;
    }
}

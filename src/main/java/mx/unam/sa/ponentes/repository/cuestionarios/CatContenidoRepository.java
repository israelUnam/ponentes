package mx.unam.sa.ponentes.repository.cuestionarios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import mx.unam.sa.ponentes.dto.cuestionarios.SolicitudDTO;
import mx.unam.sa.ponentes.entity.cuestionarios.CatContenido;



@Repository
public interface CatContenidoRepository extends JpaRepository<CatContenido, Long> {
    @Query(value = "select tema.id_tema as IdTema, tema.descripcion as descTema, cat_pregunta.descripcion as desPregunta, "
            +
            " cat_pregunta.observaciones, cat_pregunta.obligatoria, cat_contenido.id_contenido as idContenido, " +
            " cat_contenido.descripcion as desContenido, cat_contenido.otro," +
            " cat_tipo_pregunta.descripcion as desCatalogo, cat_pregunta.id_pregunta as idPregunta, " +
            " if( cat_pregunta.obligatoria = 1, 'required','') as required, " +
            " if(cat_tipo_pregunta.descripcion='Multiple' or cat_tipo_pregunta.descripcion='Una' ,'0','') as respuesta "
            +
            " from cuestionario, tema, cat_tipo_pregunta, cat_pregunta,  cat_contenido " +
            " where  cuestionario.id_cuestionario = tema.id_cuestionario " +
            " and tema.id_tema = cat_pregunta.id_tema " +
            " and cat_pregunta.id_pregunta = cat_contenido.id_pregunta " +
            " and cat_tipo_pregunta.id_catalogo = cat_pregunta.id_catalogo " +
            " and cat_pregunta.status = 1 " +
            " and cuestionario.id_cuestionario = ? " +
            "ORDER BY tema.orden, cat_pregunta.orden, cat_contenido.orden", nativeQuery = true)
    List<SolicitudDTO> findCuestionario(Long idCuestionario);
}

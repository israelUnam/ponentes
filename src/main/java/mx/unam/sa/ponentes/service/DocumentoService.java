package mx.unam.sa.ponentes.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.dto.DocumentoDTO;
import mx.unam.sa.ponentes.models.Documento;
import mx.unam.sa.ponentes.repository.DocumentoRepo;
import mx.unam.sa.ponentes.utils.Utils;

@Service
public class DocumentoService {
    @Autowired
    private DocumentoRepo documentoRep;

    public Documento save(Documento documento) throws RuntimeException {
        try {
            return documentoRep.save(documento);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<DocumentoDTO> getDocumentoByIds(Integer[] id) {
        List<DocumentoDTO> salida = null;
        return salida;
    }

    @Transactional
    public Documento saveRubrica(MultipartFile file, Long idRespCuestionario, String UserName) {

        try {


            Documento documento = new Documento();
            documento.setNombre(file.getOriginalFilename());
            documento.setTipo(file.getContentType());
            documento.setData(file.getBytes());
            documento.setSize((int) file.getSize());
            documento = documentoRep.save(documento);
            documentoRep.updateRespcuestDoctosRub(idRespCuestionario, documento.getIdDocto());

            return documento;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void deleteById(Integer id) {
        documentoRep.deleteById(id);
    }

    @Transactional
    public void deleteRubricaByIdDocto(Integer id) {
        documentoRep.deleteRelacionDoctosRub(id);
        this.deleteById(id);
    }

    public Documento findDocumentobyId(Integer id) {
        return documentoRep.findById(id).orElse(null);
    }

    public List<Map<String, String>> getDoctosMapWeb(Set<Documento> documentos) {
        List<Map<String, String>> salida = new ArrayList<>();

        documentos.forEach(docto -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idDocto", docto.getIdDocto());
            String paramdocto = Utils.encodeWJT("docto", map, "AxRwYWESR");
            String url = "/documento/getPdf?param=" + paramdocto;

            Map<String, String> interior = new HashMap<String, String>();
            interior.put("key", docto.getNombre());
            interior.put("value", url);
            salida.add(interior);
        });

        return salida;
    }
}

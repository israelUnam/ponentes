package mx.unam.sa.ponentes.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.unam.sa.ponentes.config.Datosconf;
import mx.unam.sa.ponentes.models.Documento;
import mx.unam.sa.ponentes.repository.DocumentoRepo;
import mx.unam.sa.ponentes.utils.Utils;


@Controller
@RequestMapping("/documento")
public class DocumentoController {
    @Autowired
    DocumentoRepo documentoRep;

    @Autowired
    Datosconf datosconf;

    @GetMapping(value="/getPdf")
    @ResponseBody
    public ResponseEntity<byte[]> mostrarPDF(String param) {

        try {

            Map<String, Object> map = Utils.decodeJWT("docto", param, datosconf.getSecretJWT());

            int idDocto = (int) map.get("idDocto");

            Documento documento = documentoRep.findById(idDocto).get();

            byte[] pdfBytes = documento.getData();

            HttpHeaders headers = new HttpHeaders();

            if (documento.getTipo().toUpperCase().contains("PDF")){
                headers.setContentType(MediaType.APPLICATION_PDF);
            }else{
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("inline", documento.getNombre());
            }

            

            //headers.setContentDispositionFormData("attachment", documento.getNombre());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package mx.unam.sa.ponentes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import mx.unam.sa.ponentes.utils.Utils;

@Configuration
@ConfigurationProperties(prefix = "app")
public class Datosconf {
    private boolean produccion;
    private String version;
    private String siglas;
    private String facultad;
    private String comite;
    private String paginacomite;
    private String paginamanual;
    private String correo;
    private String paginasistema;
    private String avisoprivacidad;
    private String secretJWT;

    public boolean isProduccion() {
        return produccion;
    }

    public void setProduccion(boolean produccion) {
        this.produccion = produccion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getFacultad() {
        return Utils.decodeHtmlAccents(this.facultad);
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getComite() {
        return Utils.decodeHtmlAccents(comite);
    }

    public void setComite(String comite) {
        this.comite = comite;
    }

    public String getPaginacomite() {
        return Utils.decodeHtmlAccents(paginacomite);
    }
    public void setPaginacomite(String paginacomite) {
        this.paginacomite = paginacomite;
    }   
    public String getPaginamanual() {
        return Utils.decodeHtmlAccents(paginamanual);
    }   
    public void setPaginamanual(String paginamanual) {
        this.paginamanual = paginamanual;
    }
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPaginasistema() {
        return Utils.decodeHtmlAccents(paginasistema);
    }

    public void setPaginasistema(String paginasistema) {
        this.paginasistema = paginasistema;
    }

    public String getAvisoprivacidad() {
        return avisoprivacidad;
    }

    public void setAvisoprivacidad(String avisoprivacidad) {
        this.avisoprivacidad = avisoprivacidad;
    }

    public String getSecretJWT() {
        return secretJWT;
    }

    public void setSecretJWT(String secretJWT) {
        this.secretJWT = secretJWT;
    }
}


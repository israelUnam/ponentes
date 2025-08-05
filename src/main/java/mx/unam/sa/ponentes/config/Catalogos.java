package mx.unam.sa.ponentes.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import mx.unam.sa.ponentes.models.Authority;
import mx.unam.sa.ponentes.models.CatContenido;
import mx.unam.sa.ponentes.models.CatPregunta;
import mx.unam.sa.ponentes.models.CatTipoPregunta;
import mx.unam.sa.ponentes.models.Cuestionario;
import mx.unam.sa.ponentes.models.Tema;
import mx.unam.sa.ponentes.repository.AuthorityRepository;
import mx.unam.sa.ponentes.repository.CatTipoPreguntaRepository;
import mx.unam.sa.ponentes.repository.CuestionarioRepository;

@Configuration
public class Catalogos {
        AuthorityRepository authorityRepository;
        CatTipoPreguntaRepository catTipoPreguntaRepository;
        CuestionarioRepository cuestionarioRepository;

        public Catalogos(AuthorityRepository authorityRepository, CatTipoPreguntaRepository catTipoPreguntaRepository,
                        CuestionarioRepository cuestionarioRepository) {
                this.authorityRepository = authorityRepository;
                this.catTipoPreguntaRepository = catTipoPreguntaRepository;
                this.cuestionarioRepository = cuestionarioRepository;
        }

        @PostConstruct
        public void initDatabase() {
                // Si ya hay usuarios no se hace nada
                if (cuestionarioRepository.count() > 0) {
                        return;
                }
                System.out.println("Creando catálogos...");
                // En caso contrario llena los datos básicos
                // alta_authorities();
                // catTipoPregunta();
                cuestionario_conferencia();
                cuestionario_microtaller();
                cuestionario_panel();
                cuestionario_trabajoLibre();
                cuestionario_carteDigital();
                cuestionario_artículosInvestigación();
                cuestionario_cineMintuo();


                System.out.println("Catálogos creados.");
        }

        @SuppressWarnings("unused")
        private void alta_authorities() {
                authorityRepository.save(new Authority("ROLE_ADMIN"));
        }

        @Transactional
        private void catTipoPregunta() {
                CatTipoPregunta catTexto = new CatTipoPregunta(1, "Texto", null);
                catTipoPreguntaRepository.saveAndFlush(catTexto);
                CatTipoPregunta catMultiple = new CatTipoPregunta(2, "Multiple", null);
                catTipoPreguntaRepository.saveAndFlush(catMultiple);
                CatTipoPregunta catUna = new CatTipoPregunta(3, "Una", null);
                catTipoPreguntaRepository.saveAndFlush(catUna);
                CatTipoPregunta catSinResp = new CatTipoPregunta(4, "Sin respuesta", null);
                catTipoPreguntaRepository.saveAndFlush(catSinResp);
                CatTipoPregunta catArchivo = new CatTipoPregunta(5, "Archivo", null);
                catTipoPreguntaRepository.saveAndFlush(catArchivo);
                CatTipoPregunta catSi = new CatTipoPregunta(6, "SI", "Si forzoso");
                catTipoPreguntaRepository.saveAndFlush(catSi);

        }

        @Transactional
        private void cuestionario_conferencia() {
                System.out.println("Creando cuestionario conferencia...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                final String CVEMOD = "CF";

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios

                Cuestionario cuestionario = new Cuestionario(null,
                                "Conferencia",
                                "Inscripción de conferencistas",
                                "Formulario de inscripción para conferencia",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Trabajo principal",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos al nombrar al archivo:"
                                                + "Para el archivo, Word extensión “.docx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].docx",
                                catArchivo, tema3, true, 1);
                t3_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta1, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Presentación en PowerPoint",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, PowerPont extensión “.pptx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pptx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta2, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

        @Transactional
        private void cuestionario_panel() {
                System.out.println("Creando cuestionario panel...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios
                final String CVEMOD = "PA";

                Cuestionario cuestionario = new Cuestionario(null,
                                "Panel de especialistas",
                                "Inscripción de especialistas",
                                "Formulario de inscripción para panel de especialistas",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Trabajo principal",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos al nombrar al archivo:"
                                                + "Para el archivo, Word extensión “.docx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].docx",
                                catArchivo, tema3, true, 1);
                t3_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta1, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Presentación en PowerPoint",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, PowerPont extensión “.pptx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pptx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta2, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

        @Transactional
        private void cuestionario_microtaller() {
                System.out.println("Creando cuestionario micro taller...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios
                final String CVEMOD = "MT";

                Cuestionario cuestionario = new Cuestionario(null,
                                "Microtaller",
                                "Inscripción de instructores",
                                "Formulario de inscripción para el microtaller",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Trabajo principal",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos al nombrar al archivo:"
                                                + "Para el archivo, Word extensión “.docx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].docx",
                                catArchivo, tema3, true, 1);
                t3_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta1, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Presentación en PowerPoint",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, PowerPont extensión “.pptx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pptx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta2, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

        @Transactional
        private void cuestionario_trabajoLibre() {
                System.out.println("Creando cuestionario trabajo libre...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios
                final String CVEMOD = "TL";

                Cuestionario cuestionario = new Cuestionario(null,
                                "Trabajo libre",
                                "Inscripción de presentador",
                                "Formulario de inscripción para el trabajo libre",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Trabajo principal",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos al nombrar al archivo:"
                                                + "Para el archivo, Word extensión “.docx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].docx",
                                catArchivo, tema3, true, 1);
                t3_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta1, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Presentación en PowerPoint",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, PowerPont extensión “.pptx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pptx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta2, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

        @Transactional
        private void cuestionario_carteDigital() {
                System.out.println("Creando cuestionario cartel digital...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios
                final String CVEMOD = "CD";

                Cuestionario cuestionario = new Cuestionario(null,
                                "Cartel digital",
                                "Inscripción de presentador",
                                "Formulario de inscripción para el cartel digital",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Cartel digital",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos al nombrar al archivo:"
                                                + "Para el archivo extensión “.pdf nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pdf",
                                catArchivo, tema3, true, 1);
                t3_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta1, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Descripción del cartel digital",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, Word extensión “.docx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].docx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta2, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

        private void cuestionario_artículosInvestigación() {
                System.out.println("Creando cuestionario articulo de investigación...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios
                final String CVEMOD = "AI";

                Cuestionario cuestionario = new Cuestionario(null,
                                "Artículo de Investigación",
                                "Inscripción de presentador",
                                "Formulario de inscripción para el Artículo de Investigación",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Artículo de investigación",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos al nombrar al archivo:"
                                                + "Para el archivo extensión “.pdf nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pdf",
                                catArchivo, tema3, true, 1);
                t3_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta1, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Presentación en PowerPoint",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, PowerPont extensión “.pptx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].pptx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta2, "Archivo",
                                                false, 0));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

        @Transactional
        private void cuestionario_cineMintuo() {
                System.out.println("Creando cuestionario cine minuto...");

                CatTipoPregunta catTexto = catTipoPreguntaRepository.findById(1).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catMultiple = catTipoPreguntaRepository.findById(2).get();
                CatTipoPregunta catUna = catTipoPreguntaRepository.findById(3).get();
                @SuppressWarnings("unused")
                CatTipoPregunta catSinResp = catTipoPreguntaRepository.findById(4).get();
                CatTipoPregunta catArchivo = catTipoPreguntaRepository.findById(5).get();
                CatTipoPregunta catSi = catTipoPreguntaRepository.findById(6).get();

                // se crea un cuestionario con sus temas y preguntas, utilice esta sección para
                // crear cuetionarios propios
                final String CVEMOD = "CM";

                Cuestionario cuestionario = new Cuestionario(null,
                                "Cine minuto",
                                "Inscripción de responsable",
                                "Formulario de inscripción para el cine minuto",
                                CVEMOD);

                // Preguntas y respuestas Tema 1
                Tema tema1 = new Tema(null, "Datos del responsable", cuestionario, 1);

                CatPregunta t1_catPregunta1 = new CatPregunta(null, "Nombre completo:",
                                "Apellidos y nombre(s)", catTexto, tema1, true, 1);
                t1_catPregunta1.getCatContenido().add(new CatContenido(null, t1_catPregunta1,
                                "Texto",
                                false, 1));
                tema1.getCatPreguntas().add(t1_catPregunta1);

                CatPregunta t1_catPregunta2 = new CatPregunta(null, "Correo de contacto:",
                                "Correo donde se recibirá la comunicación", catTexto, tema1, true, 1);
                t1_catPregunta2.getCatContenido().add(new CatContenido(null, t1_catPregunta2,
                                "Texto",
                                false, 2));
                tema1.getCatPreguntas().add(t1_catPregunta2);

                CatPregunta t1_catPregunta3 = new CatPregunta(null, "Organización o institución:",
                                "Proporcione el nombre completo de su organización", catTexto, tema1, true,
                                1);
                t1_catPregunta3.getCatContenido().add(new CatContenido(null, t1_catPregunta3,
                                "Texto",
                                false, 3));
                tema1.getCatPreguntas().add(t1_catPregunta3);

                CatPregunta t1_catPregunta4 = new CatPregunta(null, "Grado académico",
                                "Proporcione el último grado de estudios", catUna, tema1, true, 4);
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Licenciatura", false, 1));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Maestria", false, 2));
                t1_catPregunta4.getCatContenido().add(new CatContenido(null, t1_catPregunta4,
                                "Doctorado",
                                false, 3));
                t1_catPregunta4.getCatContenido()
                                .add(new CatContenido(null, t1_catPregunta4,
                                                "Otra", true, 4));

                tema1.getCatPreguntas().add(t1_catPregunta4);

                CatPregunta t1_catPregunta5 = new CatPregunta(null, "Número telefónico :",
                                "incluyendo clave internacional", catTexto, tema1, true, 1);
                t1_catPregunta5.getCatContenido().add(new CatContenido(null, t1_catPregunta5,
                                "Texto",
                                false, 4));
                tema1.getCatPreguntas().add(t1_catPregunta5);

                CatPregunta t1_catPregunta6 = new CatPregunta(null, "Ejes y líneas temáticas",
                                "Seleccione la línea temática en que participa", catUna, tema1, true, 5);
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en proyectos de información, de nuevas tecnologías y de software en el sector público.   ",
                                false, 1));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales: Agenda política, ética y participación.",
                                false, 2));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos de la economía social y solidaria.",
                                false, 3));
                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Administración de proyectos en el Sector publico.",
                                false, 4));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Proyectos sociales y participacion ciudadana para la reconstrucción del tejido social.",
                                false, 5));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "La ética en la construcción de la vivienda digana.",
                                false, 6));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Responsabilidad ética y compromiso social.",
                                false, 7));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética de la inteligencia artificial: una nueva tecnología centrada en el ser humano.",
                                false, 8));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética y Responsabilidad Social en la Dirección de Proyectos Cinematográficos: El Rol del Financiamiento Público",
                                false, 9));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 10));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la toma de decisiones de TIC.",
                                false, 11));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Ética en la dirección de proyectos sociales y voluntariado.",
                                false, 12));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Sostenibilidad y Valor Social.",
                                false, 13));

                t1_catPregunta6.getCatContenido().add(new CatContenido(null, t1_catPregunta6,
                                "Otra.",
                                false, 14));

                tema1.getCatPreguntas().add(t1_catPregunta6);

                cuestionario.getTemas().add(tema1);

                Tema tema2 = new Tema(null, "Aviso de privacidad y cesión de derechos", cuestionario, 2);

                CatPregunta t2_catPregunta1 = new CatPregunta(null, "¿Ha leído y acepta el  aviso de privacidad?",
                                "<a href=\"https://www.congreso-unam.org/aviso-de-privacidad\" target='_blank'>Aviso de privacidad</a>",
                                catSi, tema2, true, 1);
                t2_catPregunta1.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta1, "SI", false, 1));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta1);

                CatPregunta t2_catPregunta2 = new CatPregunta(null,
                                "¿Ha leído y acepta la Carta de Cesión de Derechos ?",
                                "<a href=\"https://docs.google.com/document/d/1rxiEtVsHvW78kC-C1ClYKk4SOPD1m6lNz7OBHZtvf4o/edit?usp=sharing\" target='_blank'>Carta de Cesión de Derechos </a>",
                                catSi, tema2, true, 2);
                t2_catPregunta2.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta2, "SI", false, 2));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta2);

                CatPregunta t2_catPregunta3 = new CatPregunta(null, "Carta de Cesión de Derechos",
                                " En esta sección deberá adjuntar su carta. "
                                                + "El documentos deberá de cumplir con los siguientes lineamientos al nombrar al archivo: "
                                                + "Para el archivo extensión “.pdf” nombrado del archivo con la siguiente nomenclatura: "
                                                + "En caso de haber más de un participante, agregar una carta por cada uno de ellos en un solo documento."
                                                + "codep4_CartaCesion_[ddmmaa]_[apellido paterno_responsable].pdf”",
                                catArchivo, tema2, true, 3);

                t2_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t2_catPregunta3, "Archivo",
                                                false, 0));
                tema2.getCatPreguntas()
                                .add(t2_catPregunta3);

                cuestionario.getTemas().add(tema2);

                Tema tema3 = new Tema(null, "Carga de documentos", cuestionario, 3);

                CatPregunta t3_catPregunta1 = new CatPregunta(null, "Título del cortometraje:",
                                "Escriba el título de su contrometraje", catTexto, tema1, true, 1);
                t3_catPregunta1.getCatContenido().add(new CatContenido(null, t3_catPregunta1,
                                "Texto",
                                false, 1));
                tema3.getCatPreguntas().add(t3_catPregunta1);

                CatPregunta t3_catPregunta2 = new CatPregunta(null, "Nombre completo de los participantes:",
                                "Escriba el nombre de los participantes, separados por una coma, iniciando por el nombre",
                                catTexto, tema3, true, 1);
                t3_catPregunta2.getCatContenido().add(new CatContenido(null, t3_catPregunta2,
                                "Texto",
                                false, 2));
                tema3.getCatPreguntas().add(t3_catPregunta2);

                CatPregunta t3_catPregunta3 = new CatPregunta(null, "Formato del cortometraje",
                                "Seleccione el fomrato", catUna, tema3, true, 4);
                t3_catPregunta3.getCatContenido().add(new CatContenido(null, t3_catPregunta3,
                                "Animado", false, 1));
                t3_catPregunta3.getCatContenido().add(new CatContenido(null, t3_catPregunta3,
                                "Ficción", false, 2));
                t3_catPregunta3.getCatContenido().add(new CatContenido(null, t3_catPregunta3,
                                "Documental",
                                false, 3));
                t3_catPregunta3.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta3,
                                                "Otra", true, 3));
                tema3.getCatPreguntas().add(t3_catPregunta3);                                

                CatPregunta t3_catPregunta4 = new CatPregunta(null, "Enlace, link o hipervínculo:",
                                "Recuerde que el video debe de estar precargado en las plataformas de DropBox, Google Drive o OneDrive y deberá estar compartido de manera privada con acceso únicamente con el enlace y seguir los lineamientos del formato",
                                catTexto, tema3, true, 1);
                t3_catPregunta4.getCatContenido().add(new CatContenido(null, t3_catPregunta4,
                                "Texto",
                                false, 4));
                tema3.getCatPreguntas().add(t3_catPregunta4);

                CatPregunta t3_catPregunta5 = new CatPregunta(null, "Descripción del cineminuto",
                                " En esta sección deberá adjuntar su trabajo para participar. Tamaño máximo del archivo 10MB. "
                                                + "El documento deberá de cumplir con los siguientes lineamientos: plantilla de aacuerdo a la convocatoria y al nombrar al archivo:"
                                                + "Para el archivo, Word extensión “.docx” nombrado del archivo con la siguiente nomenclatura: "
                                                + "codep4_" + CVEMOD + "_[ddmmaa]_[apellido paterno].docx”",
                                catArchivo, tema3, true, 1);
                t3_catPregunta5.getCatContenido()
                                .add(new CatContenido(null, t3_catPregunta5, "Archivo",
                                                false, 5));
                tema3.getCatPreguntas().add(t3_catPregunta5);

                cuestionario.getTemas().add(tema3);

                try {
                        cuestionario = cuestionarioRepository.save(cuestionario);
                } catch (Exception e) {
                        System.out.println(e.getMessage());
                }

        }

}

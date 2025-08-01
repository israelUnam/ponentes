package mx.unam.sa.ponentes.utils;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.passay.CharacterData;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Clase de utilidades que proporciona varios métodos auxiliares para el formato
 * de fechas,
 * codificación, decodificación y operaciones JWT.
 */
public class Utils {

    private static final byte[] SALT = { (byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F,
            (byte) 0x5A, (byte) 0x75 };

    private final static int ITERATION_COUNT = 31;

    /**
     * Formats the given date into a string representation using the format "dd 'de'
     * MMMM 'de' yyyy" in Spanish locale.
     *
     * @param fecha The date to format.
     * @return The formatted date string.
     */
    public static String formatFecha(Date fecha) {
        if (fecha == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es"));
        return sdf.format(fecha);
    }

    /**
     * Formats the given date into a string representation using the format "dd 'de'
     * MMMM 'de' yyyy hh:mm:ss" in Spanish locale.
     *
     * @param fecha The date to format.
     * @return The formatted date string.
     */
    public static String formatFechaHM(Date fecha) {
        if (fecha == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy hh:mm:ss", new Locale("es"));
        return sdf.format(fecha);
    }

    /**
     * Converts the given string representation of a date into a Date object using
     * the format "dd-MM-yyyy".
     *
     * @param dateString The string representation of the date.
     * @return The converted Date object.
     * @throws RuntimeException If an error occurs during the conversion.
     */
    public static Date convertStrToDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return sdf.parse(dateString);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Encodes the given input string using a salt and iteration count.
     *
     * @param input The input string to encode.
     * @return The encoded string.
     * @throws RuntimeException If an error occurs during the encoding process.
     */
    public static String encode(String input) throws RuntimeException {
        // from https://gist.github.com/cxubrix/4316635

        if (input == null) {
            throw new IllegalArgumentException();
        }
        try {

            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);
            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            byte[] enc = ecipher.doFinal(input.getBytes());
            String res = new String(Base64.encodeBase64(enc));
            // escapes for url
            res = res.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");
            return res;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decodes the given token string using a salt and iteration count.
     *
     * @param token The token string to decode.
     * @return The decoded string.
     * @throws RuntimeException If an error occurs during the decoding process.
     */
    public static String decode(String token) throws RuntimeException {
        if (token == null) {
            return null;
        }
        try {

            String input = token.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');

            byte[] dec = Base64.decodeBase64(input.getBytes());

            KeySpec keySpec = new PBEKeySpec(null, SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            byte[] decoded = dcipher.doFinal(dec);

            String result = new String(decoded);
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Decodes the given string and returns a map of key-value pairs.
     *
     * @param decode The string to decode.
     * @return A map containing the decoded key-value pairs.
     */
    public static Map<String, Object> getMapDecode(String decode) {
        Map<String, Object> paramMap = new HashMap<>();
        if (decode == null || decode.trim().isEmpty()) {
            return paramMap;
        }
        
        String valor = decode(decode);
        if (valor == null) {
            return paramMap;
        }
        
        String[] params = valor.split("&");
        for (String dato : params) {
            if (dato != null && dato.contains("=")) {
                String[] keyValue = dato.split("=");
                if (keyValue.length >= 2) {
                    paramMap.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return paramMap;
    }

    /**
     * Encodes the given data into a JWT token using the provided identifier and
     * secret.
     *
     * @param identificador The identifier for the token.
     * @param data          The data to include in the token.
     * @param secret        The secret key for signing the token.
     * @return The encoded JWT token.
     */
    public static String encodeWJT(String identificador, Map<String, Object> data, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date expirationTime = new Date(System.currentTimeMillis() + 3600 * 1000); // 1 hour
        String token = JWT.create()
                .withIssuer(identificador)
                .withClaim("data", data)
                .withExpiresAt(expirationTime)
                .sign(algorithm);
        return token;
    }

    /**
     * Decodes the given JWT token using the provided identifier and secret.
     *
     * @param identificador The identifier for the token.
     * @param token         The JWT token to decode.
     * @param secret        The secret key for verifying the token.
     * @return A map containing the decoded data from the token.
     */
    public static Map<String, Object> decodeJWT(String identificador, String token, String secret) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT jwt = JWT.require(algorithm)
                .withIssuer(identificador)
                .build()
                .verify(token);

        Claim salida = jwt.getClaim("data");
        return salida.asMap();
    }

    public static String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "ERROR_CODE";
            }

            public String getCharacters() {
                return "!@#$%&_-+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }

    /**
     * Convierte entidades HTML de acentos (como &aacute;) a sus caracteres normales.
     *
     * @param texto El texto con entidades HTML.
     * @return El texto con acentos normales.
     */
    public static String decodeHtmlAccents(String texto) {
        if (texto == null) {
            return null;
        }
        return texto
            .replace("&aacute;", "á")
            .replace("&eacute;", "é")
            .replace("&iacute;", "í")
            .replace("&oacute;", "ó")
            .replace("&uacute;", "ú")
            .replace("&Aacute;", "Á")
            .replace("&Eacute;", "É")
            .replace("&Iacute;", "Í")
            .replace("&Oacute;", "Ó")
            .replace("&Uacute;", "Ú")
            .replace("&ntilde;", "ñ")
            .replace("&Ntilde;", "Ñ")
            .replace("&uuml;", "ü")
            .replace("&Uuml;", "Ü");
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package item_1;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 *
 * @author clmac
 */
public class Decrypt {

    public static void main(String[] args) {

        // leer archivos y contrasena desde la linea de comandos
        File inFile = new File("archivos/item1/salida.txt");//args[0]);
        File keyStoreFile = new File("archivos/item1/keytool/myKeyStore.jks");
        String password = "storepass";

        try{
            System.out.println("Abriendo Archivo a desencriptar: " + inFile);
            FileInputStream rawDataFromFile = new FileInputStream(inFile);

            // Carga el keystore
            KeyStore myKeyStore = KeyStore.getInstance("JKS");
            FileInputStream inStream = new FileInputStream(keyStoreFile);
            myKeyStore.load(inStream, password.toCharArray());

            // Lee la llave privada del keystore para desencriptar.
            PrivateKey privatekey = (PrivateKey) myKeyStore.getKey("mykey", "storepass".toCharArray());

            // Inicializar el Objeto Cipher RSA
            Cipher aesCipher = Cipher.getInstance("RSA");
            aesCipher.init(Cipher.DECRYPT_MODE, privatekey);

            // Lee la clave secreta AES (256 bytes)
            byte[] b = new byte[256];
            rawDataFromFile.read(b);
            byte[] claveAESraw = aesCipher.doFinal(b);
            SecretKeySpec claveAES = new SecretKeySpec(claveAESraw, "AES");

            // Inicializar el Objeto Cipher AES
            Cipher rsaCipher = Cipher.getInstance("AES");
            rsaCipher.init(Cipher.DECRYPT_MODE, claveAES);

            // Desencripta los contenidos del archivo
            byte[] out = rsaCipher.doFinal(rawDataFromFile.readAllBytes());

            // Imprime los contenidos por consola
            System.out.println(new String(out));

            // Cierra el stream de datos
            rawDataFromFile.close();
            inStream.close();

        } catch (Exception e){
            // Este codigo se ejecuta si hay un error en el archivo, un error de codificación, etc.
            e.printStackTrace();
        }


    }
}

package item3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Ejemplo de encriptado y desencriptado con algoritmo AES.
 * Se apoya en RSAAsymetricCrypto.java para salvar en fichero
 * o recuperar la clave de encriptacion.
 *
 * Clase modificada por Sebastian Gonzalez y Claudio Macher
 * 
 * @author Chuidiang
 *
 */
public class AESSymetricCrypto {

   public static void main(String[] args) throws Exception {
      try{
         String pathInput, pathOutput;
         String password = "storepass";
         if(args.length < 2){
            pathInput = "archivos/item3/input.txt";
            pathOutput = "archivos/item3/output.txt";
         }
         else{
            pathInput = args[0];
            pathOutput = args[1];
         }

         File keyStoreFile = new File("archivos/item3/gonzalez.jks");
         FileInputStream jksFile = new FileInputStream(keyStoreFile);
         FileInputStream inFile = new FileInputStream(pathInput);
         FileOutputStream outFile = new FileOutputStream(pathOutput);

         KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
         keyGenerator.init(128);
         Key key = keyGenerator.generateKey();

         key = new SecretKeySpec("una clave de 16 bytes".getBytes(),  0, 16, "AES");

         Cipher aesCipher = Cipher.getInstance("AES");
         aesCipher.init(Cipher.ENCRYPT_MODE, key);
         byte[] encriptado = aesCipher.doFinal(inFile.readAllBytes());

         // Encriptar llave simetrica AES con la llave publica RSA
         byte[] encodedKey = aesCipher.doFinal(key.getEncoded());

         byte[] datosYClave = new byte[encriptado.length + encodedKey.length];
         System.arraycopy(encodedKey, 0, datosYClave, 0, encodedKey.length);
         System.arraycopy(encriptado, 0, datosYClave, encodedKey.length, encriptado.length);

         // Carga el keystore
         KeyStore myKeyStore = KeyStore.getInstance("JKS");
         FileInputStream inStream = new FileInputStream(keyStoreFile);
         myKeyStore.load(inStream, password.toCharArray());

         // Lee las llave publica del keystore.
         Certificate cert = myKeyStore.getCertificate("mykey");
         PublicKey publicKey = cert.getPublicKey();

         // Inicializa el Objeto Cipher RSA
         Cipher rsaCipher = Cipher.getInstance("RSA");
         rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

         byte[] encriptadoAESyPriv = rsaCipher.doFinal(datosYClave);

         outFile.write(encriptadoAESyPriv);

         inStream.close();
         inFile.close();
         outFile.close();


      } catch (Exception e){
         System.out.println("Error:");
         e.printStackTrace();
      }
   }

}

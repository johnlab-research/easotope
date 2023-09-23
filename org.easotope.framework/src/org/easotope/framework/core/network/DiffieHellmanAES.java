/*
 * Copyright © 2016-2023 by Devon Bowen.
 *
 * This file is part of Easotope.
 *
 * Easotope is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * Additional permission under GNU GPL version 3 section 7:
 * If you modify this Program, or any covered work, by linking or combining
 * it with the Eclipse Rich Client Platform (or a modified version of that
 * library), containing parts covered by the terms of the Eclipse Public
 * License, the licensors of this Program grant you additional permission
 * to convey the resulting work. Corresponding Source for a non-source form
 * of such a combination shall include the source code for the parts of the
 * Eclipse Rich Client Platform used as well as that of the covered work.
 *
 * Easotope is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Easotope. If not, see <http://www.gnu.org/licenses/>.
 */

package org.easotope.framework.core.network;

import java.math.BigInteger;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DiffieHellmanAES {
	// p, g, and l are generated by a one-time run of main()
	// after this they are shared by the local and remote instances
	// they do not need to be kept secret
	private static String p = "156423479455214913311268512044694198000874552283999129175410237154854704547215117557653116727445939052598195767186998098260939898353335005391887250409318173462725917483374594721131546769238471321800135545112046745972835282459212169422183045525295130602404454315598839172202046286305979621464321200325456774317";
	private static String g = "70319784004225237707048249702900086643997204055587922191617250593469838549404359757940319523457106916700083307800989624115972650178312629229359684546298306764327710144060920436370863898745148959440997282076088791247900678472426503489571326245527451226437081474112812646876118749756798086593794082481268525672";
	private static String l = "1023";
	
	private static String DH = "DiffieHellman";
	private static String AES = "AES/CBC/PKCS5Padding";

	private boolean isListener;
	
	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	private byte[] iv1 = new byte[16];
	private byte[] iv2 = new byte[16];

	private Cipher ecipher;
	private Cipher dcipher;

	public static void main(String[] args) {
		try {
			AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance(DH);
			paramGen.init(1024);
			AlgorithmParameters params = paramGen.generateParameters();
			DHParameterSpec dhSpec = (DHParameterSpec) params.getParameterSpec(DHParameterSpec.class);

			System.out.println("private static String p = \"" + dhSpec.getP() + "\";");
			System.out.println("private static String g = \"" + dhSpec.getG() + "\";");
			System.out.println("private static String l = \"" + dhSpec.getL() + "\";");

		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public DiffieHellmanAES(boolean isListener) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		this.isListener = isListener;
		
		DHParameterSpec dhSpec = new DHParameterSpec(new BigInteger(p), new BigInteger(g), Integer.parseInt(l));

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(DH);
		keyGen.initialize(dhSpec);

		KeyPair keypair = keyGen.generateKeyPair();
		privateKey = keypair.getPrivate();
		publicKey = keypair.getPublic();
		
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(iv1);
		secureRandom.nextBytes(iv2);
	}

	public byte[] getLocalPublicKey() {
		return publicKey.getEncoded();
	}

	public void setRemotePublicKey(byte[] publicKeyBytes, byte[] remoteIv1, byte[] remoteIv2) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException {
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFact = KeyFactory.getInstance(DH);
		PublicKey publicKey = keyFact.generatePublic(x509KeySpec);
		
		KeyAgreement ka = KeyAgreement.getInstance(DH);
		ka.init(privateKey);
		ka.doPhase(publicKey, true);
		
		byte[] masterKey = ka.generateSecret();
		SecretKeySpec secretKey = new SecretKeySpec(masterKey, 0, 16, "AES");

		for (int i=0; i<16; i++) {
			iv1[i] ^= remoteIv1[i];
			iv2[i] ^= remoteIv2[i];
		}

		ecipher = Cipher.getInstance(AES);
		ecipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(isListener ? iv1 : iv2));

		dcipher = Cipher.getInstance(AES);
		dcipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(isListener ? iv2 : iv1));

		privateKey = null;
	}

	public boolean isReady() {
		return ecipher != null && dcipher != null;
	} 

	public byte[] encrypt(byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
		if (ecipher == null) {
			throw new RuntimeException("Cannot encrypt because cipher was not initialized");
		}

		return ecipher.doFinal(bytes);
	}

	public byte[] decrypt(byte[] bytes) throws IllegalBlockSizeException, BadPaddingException {
		if (dcipher == null) {
			throw new RuntimeException("Cannot decrypt because cipher was not initialized");
		}

		return dcipher.doFinal(bytes);
	}

	public class MissingRemotePublicKey extends Exception {
		private static final long serialVersionUID = 1L;

		public MissingRemotePublicKey() {
			super("Remote Diffie-Hellman key has not been set.");
		}
	}

	public byte[] getIv1() {
		return iv1;
	}
	
	public byte[] getIv2() {
		return iv2;
	}
}
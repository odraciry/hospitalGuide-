package br.com.sp.rick.hospitalguia.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class FirebaseUtil {
	// variavel para guardar as credenciais de acesso
	private Credentials credenciais;
	// variavel para acessar e manipular o storage
	private Storage storage;
	// constante para o nome do bucket
	private final String BUCKET_NAME = "hospitalguia.appspot.com";
	// constante para o prefixo da url
	private final String PREFIX = "https://firebasestorage.googleapis.com/v0/b/" + BUCKET_NAME + "/o/";
	// constante para o sufixo da url
	private final String SUFFIX = "?alt=media";
	// constante para a url
	private final String DOWNLOAD_URL = PREFIX + "%s" + SUFFIX;

	public FirebaseUtil() {
		// acessar o arquivo json com a chave privada
		Resource resource = new ClassPathResource("chavefirebase.json");

		try {
			// gera uma credencial no firebase atraves do arquivo
			credenciais = GoogleCredentials.fromStream(resource.getInputStream());
			// cria o storage para manipular os dados no Firebase
			storage = StorageOptions.newBuilder().setCredentials(credenciais).build().getService();
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//metodo para extrair a extenção do arquivo
	private String getExtencao(String nomeArquivo) {
		//extrai o trecho do arquivo onde esta a extenção
		return nomeArquivo.substring(nomeArquivo.lastIndexOf('.'));
	}
	
	//metodo que faz o upload
	public String upload(MultipartFile arquivo) {
		//gera um nome aleatorio para o arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtencao(arquivo.getOriginalFilename());
		return "";
	}
}

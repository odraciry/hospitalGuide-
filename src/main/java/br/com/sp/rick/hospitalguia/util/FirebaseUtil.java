package br.com.sp.rick.hospitalguia.util;

import java.io.IOException;
import java.util.UUID;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
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
	public String upload(MultipartFile arquivo) throws IOException {
		//gera um nome aleatorio para o arquivo
		String nomeArquivo = UUID.randomUUID().toString() + getExtencao(arquivo.getOriginalFilename());
		// criar um blobId atraves do nome gerado para o arquivo
		BlobId blobId = BlobId.of(BUCKET_NAME, nomeArquivo);
		// cria um blobinfo atraves do blobid
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
		// gravar o blobinfo no storage passando os bytes do arquivo
		storage.create(blobInfo, arquivo.getBytes());
		//retorna a URl do arquivo gerado no Storage
		return String.format(DOWNLOAD_URL, nomeArquivo);
	}
	
	//metodo que exclui o arquivo do storage
	public void deletar(String nomeArq) {
		//retirar o prefixo e o sufixo da string
		nomeArq = nomeArq.replace(PREFIX, "").replace(SUFFIX, "");
		//obter um Blob atraves do nome
		Blob blob  = storage.get(BlobId.of(BUCKET_NAME, nomeArq));
		//deleta atraves do blob
		storage.delete(blob.getBlobId());
	}
}

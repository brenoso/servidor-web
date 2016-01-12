import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;

/**
 * Esta classe será responsável por iniciar a conexão.
 * 
 */
public class HTTPServidor {

	private int porta;

	public HTTPServidor() {
		this.porta = 80;
	}

	public HTTPServidor(int porta) {
		this.porta = porta;
	}

	public void setPorta(int porta) {
		this.porta = porta;
	}

	public void iniciar() throws IOException {

		//Declaração de algumas das variáveis que serão utilizadas durante a execução do código
		ServerSocket socketServidor = null;
		OutputStream outputStream = null;
		Socket socketCliente = null;
		String infoInput = null;
		String nomeArquivo = null;
		
		System.out.println("Inicializando servidor....");

		try {

			System.out.println("Tentando alocar a porta...");
			socketServidor = new ServerSocket(porta);

		} catch (IOException e) {

			System.out.println("Erro Fatal: " + e.getMessage());
			System.exit(1);

		} finally {

			System.out.println("Servidor OK!");
			
		}
		
		while (true) {

			System.out.println("Aguardando requisição...");

			try {

				//Tenta criar uma conexão com o servidor, a conexão estabelecida é atribuida a variável socketCliente
				socketCliente = socketServidor.accept();
				
				//Abre uma conexão de de saída pra que possa ser enviada informações para o cliente
				outputStream = socketCliente.getOutputStream();
				
				//Busca o endereço no qual o cliente está conectado com o servidor (IP)
				InetAddress infoCliente = socketCliente.getInetAddress();
				
				//É feita a impressão do endereço do usuário, utilizando o método getHostName.
				System.out.println("Cliente: " + infoCliente.getHostName() + " conectou ao servidor!");
				
				//recebe uma conexão de entrada, do cliente para o servidor
				BufferedReader input = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));				

				/* Método alternátivo para buscar o nome do arquivo que o usuário colocou na requisição
				 * Na variável infoInput é atribuido a primeira linha do input, que é justamento o tipo de requisição
				 * o nome do arquivo e o protocolo. Utilizando a função replace do tipo String é possível remover as informaçoes
				 * adicionais ficando somente com o nome do arquivo.
				 */
				infoInput = input.readLine();
				nomeArquivo = infoInput.replace("GET /", "");
				nomeArquivo = nomeArquivo.replace(" HTTP/1.1", "");
				
				//Pega o path do projeto até a localização desta classe
				URL path = getClass().getResource("/");
				
				//Busca o arquivo html que foi solicitado e cria-se um file com ele, para que possamos trabalhar com o arquivo
				File html = new File(path.getPath() + "arquivosHtml/" + nomeArquivo);
				
				//Verifica se o arquivo html existe e se o nome do arquivo não está vazio
				if(html.exists() && !nomeArquivo.isEmpty()) {
					
					//Caso a verificação for verdadeira é executado o código abaixo e é enviado o html pro cliente para ser renderizado pelo navegador
					outputStream.write(Files.readAllBytes(html.toPath()));
					outputStream.flush();
					outputStream.close();
					
				} else {
					
					/*Caso a verificação for falsa é executado o seguinte código e é enviado ao cliente o html de erro 404 (Dizendo que a
					 * página solicitada não foi encontrada
					 */
					File htmlNaoEncontrado = new File(path.getPath() + "arquivosHtml/404.html");
					
					outputStream.write(Files.readAllBytes(htmlNaoEncontrado.toPath()));
					outputStream.flush();
					outputStream.close();
					
				}

			} catch (Exception e) {

				System.out.println("Erro de conexão: " + e.getMessage());

			} finally {
				
				//E finalmente é finalizada a conexão entre cliente e servidor.				
				socketCliente.close();
			}

		}
		
	}

}

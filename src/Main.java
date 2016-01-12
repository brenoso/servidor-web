import java.io.IOException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		HTTPServidor servidor = new HTTPServidor(8012);
		servidor.iniciar();

	}

}

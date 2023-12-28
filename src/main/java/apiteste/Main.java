package apiteste;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpServer;

import apiteste.controllers.FuncionarioController;
import apiteste.models.Funcionario;

public class Main {
	
	public static List<Funcionario> funcionarios = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
        int port = 8080; // Porta para o servidor

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/funcionarios", new FuncionarioController());
        server.setExecutor(null); // default executor
        server.start();

        System.out.println("Servidor rodando na porta " + port);
	}	

	
}

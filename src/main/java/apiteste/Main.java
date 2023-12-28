package apiteste;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import apiteste.models.Funcionario;

public class Main {
	
	public static List<Funcionario> funcionarios = new ArrayList<>();
	
	public static void main(String[] args) throws IOException {
        int port = 8080; // Porta para o servidor

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/funcionarios", new MyHandler());
        server.setExecutor(null); // default executor
        server.start();

        System.out.println("Servidor rodando na porta " + port);
	}	

	static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Verifica se a requisição é do tipo GET
            if ("GET".equals(exchange.getRequestMethod())) {
            	
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("funcionarios", funcionarios);

                String response = jsonResponse.toString();

                // Configuração do cabeçalho para indicar que a resposta é um JSON
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                // Obtém os bytes da resposta
                byte[] responseBytes = response.getBytes();

                // Configura o tamanho correto no cabeçalho da resposta
                exchange.sendResponseHeaders(200, responseBytes.length);

                // Escreve os bytes no corpo da resposta
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }catch(Exception e) {
                	System.out.println(e.getMessage());
                }
            } else if("POST".equals(exchange.getRequestMethod())) {
            	try {
            	// Lê o corpo da solicitação POST
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder payload = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    payload.append(line);
                }
                br.close();
                isr.close();
                
                // Cria um objeto JSON a partir do corpo da solicitação
                JSONObject requestJson = new JSONObject(payload.toString());
                
                Gson gson = new Gson();
                Funcionario f = gson.fromJson(requestJson.toString(), Funcionario.class);
                f.setId((long) funcionarios.size());
                funcionarios.add(f);

                // Configuração do cabeçalho para indicar que a resposta é um JSON
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, -1);
            	}catch(Exception e) {
            		System.out.println(e.getMessage());
            	}
            }else if("DELETE".equals(exchange.getRequestMethod())){
            	try {
	            	var url = exchange.getRequestURI().toString();
	            	var id = Integer.parseInt(url.replace("/api/funcionarios/", ""));
	            	if(id < 0) {
	            		exchange.sendResponseHeaders(400, -1);
	            	}
	            	
//	            	var search = funcionarios.stream().filter(f -> f.getId() == id).findFirst();
	            	funcionarios.removeIf(f -> f.getId() == id);
	            	exchange.sendResponseHeaders(204, -1);
            	}catch(Exception e) {
            		System.out.println(e.getMessage());
            	}
            }else if("PUT".equals(exchange.getRequestMethod())){
            	try {
	            	var url = exchange.getRequestURI().toString();
	            	var id = Integer.parseInt(url.replace("/api/funcionarios/", ""));
	            	if(id < 0) {
	            		exchange.sendResponseHeaders(400, -1);
	            	}
	            	
	            	var buscaFuncionario = funcionarios.stream().filter(f -> f.getId() == id).findFirst();
	            	
	            	// Lê o corpo da solicitação POST
	                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
	                BufferedReader br = new BufferedReader(isr);
	                StringBuilder payload = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    payload.append(line);
	                }
	                br.close();
	                isr.close();
	                
	                // Cria um objeto JSON a partir do corpo da solicitação
	                JSONObject requestJson = new JSONObject(payload.toString());
	                
	                Gson gson = new Gson();
	                Funcionario f = gson.fromJson(requestJson.toString(), Funcionario.class);
	                
	                buscaFuncionario.get().atualizar(f);
	                
	                exchange.getResponseHeaders().set("Content-Type", "application/json");
	                
	                JSONObject jsonResponse = new JSONObject();
	                
	                jsonResponse.put("id", buscaFuncionario.get().getId());
	                jsonResponse.put("name", buscaFuncionario.get().getName());
	                jsonResponse.put("description", buscaFuncionario.get().getDescription());
	                jsonResponse.put("cnpj", buscaFuncionario.get().getCnpj());

	                String response = jsonResponse.toString();
	                
	                // Obtém os bytes da resposta
	                byte[] responseBytes = response.getBytes();

	                // Configura o tamanho correto no cabeçalho da resposta
	                exchange.sendResponseHeaders(200, responseBytes.length);

	                // Escreve os bytes no corpo da resposta
	                try (OutputStream os = exchange.getResponseBody()) {
	                    os.write(responseBytes);
	                }catch(Exception e) {
	                	System.out.println(e.getMessage());
	                }
            	}catch(Exception e) {
            		System.out.println(e.getMessage());
            	}
            }
            else {
                // Caso não seja uma requisição GET, retorna um código 405 (Method Not Allowed)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}

package apiteste.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import apiteste.models.Funcionario;
import apiteste.service.FuncionarioService;

public class FuncionarioController implements HttpHandler {
	
	FuncionarioService service;
	
	public FuncionarioController() {
		service = new FuncionarioService();
	}

	@Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
        	
            JSONObject jsonResponse = new JSONObject();
            var funcionarios = service.findAll();
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
            service.adicionar(requestJson.toString());

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
            	service.delete(id);
            	exchange.sendResponseHeaders(204, -1);
        	}catch(Exception e) {
        		exchange.sendResponseHeaders(404, -1);
        	}
        }else if("PUT".equals(exchange.getRequestMethod())){
        	try {
            	var url = exchange.getRequestURI().toString();
            	var id = Integer.parseInt(url.replace("/api/funcionarios/", ""));
            	if(id < 0) {
            		exchange.sendResponseHeaders(400, -1);
            	}
            	
            	var buscaFuncionario = service.findById(id);
            	
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
                
                buscaFuncionario.atualizar(f);
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                
                JSONObject jsonResponse = new JSONObject();
                
                jsonResponse.put("id", buscaFuncionario.getId());
                jsonResponse.put("name", buscaFuncionario.getName());
                jsonResponse.put("description", buscaFuncionario.getDescription());
                jsonResponse.put("cnpj", buscaFuncionario.getCnpj());

                String response = jsonResponse.toString();
                
                // Obtém os bytes da resposta
                byte[] responseBytes = response.getBytes();

                // Configura o tamanho correto no cabeçalho da resposta
                exchange.sendResponseHeaders(200, responseBytes.length);

                // Escreve os bytes no corpo da resposta
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
        	}catch(Exception e) {
        		exchange.sendResponseHeaders(404, -1);
        	}
        }
        else {
            // Caso não seja uma requisição GET, retorna um código 405 (Method Not Allowed)
            exchange.sendResponseHeaders(405, -1);
        }
    }

}

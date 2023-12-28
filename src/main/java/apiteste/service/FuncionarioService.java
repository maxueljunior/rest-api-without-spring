package apiteste.service;

import java.util.List;

import com.google.gson.Gson;

import apiteste.Main;
import apiteste.models.Funcionario;

public class FuncionarioService {
	
	public List<Funcionario> findAll() {
		return Main.funcionarios;
	}
	
	public void adicionar(String json) {
        Gson gson = new Gson();
        Funcionario f = gson.fromJson(json, Funcionario.class);
        f.setId((long) Main.funcionarios.size());
        Main.funcionarios.add(f);
	}
	
	public void delete(Integer id) throws Exception {
		var funcionario = Main.funcionarios.stream()
				.filter(f -> f.getId() == (long) id)
				.findFirst();
		
		if(funcionario.isEmpty()) {
			throw new Exception("Funcionario não existe");
		}
		
		Main.funcionarios.removeIf(f -> f.getId() == (long) id);
	}
	
	public Funcionario findById(Integer id) throws Exception {
		var funcionario = Main.funcionarios.stream()
				.filter(f -> f.getId() == (long) id)
				.findFirst();
		
		if(funcionario.isEmpty()) {
			throw new Exception("Funcionario não existe");
		}
		
		return funcionario.get();
	}
}

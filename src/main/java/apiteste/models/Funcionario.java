package apiteste.models;

public class Funcionario {

	private Long id;
	private String name;
	private String description;
	private String cnpj;
	
	public Funcionario(Long id, String name, String description, String cnpj) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.cnpj = cnpj;
	}
	
	public void atualizar(Funcionario f) {
		if(f.getDescription() != null) {
			this.description = f.getDescription();
		}
		if(f.getName() != null) {
			this.name = f.getName();
		}
		if(f.getCnpj() != null) {
			this.cnpj = f.getCnpj();
		}
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCnpj() {
		return cnpj;
	}

	@Override
	public String toString() {
		return "Funcionario [id=" + id + ", name=" + name + ", description=" + description + ", cnpj=" + cnpj + "]";
	}
	
}

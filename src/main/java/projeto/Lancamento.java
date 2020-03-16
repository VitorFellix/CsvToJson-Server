package projeto;

public class Lancamento {
	@Override
	public String toString() {
		return "[data=" + data + ", nome=" + nome + ", preco=" + preco + "]";
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public float getPreco() {
		return preco;
	}
	public void setPreco(float preco) {
		this.preco = preco;
	}
	
	private String data = "null";
	private String nome = "null";
	private float preco = 0f;
	
	public Lancamento(String data, String nome, float preco) {
		super();
		this.data = data;
		this.nome = nome;
		this.preco = preco;
	}
}

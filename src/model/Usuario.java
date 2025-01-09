package model;

import controller.Validadores;

public class Usuario {
	
	private String nome;
	private int idade;
	private String matricula;
	
	public Usuario(String nome, int idade, String matricula) {
		if(Validadores.isUsuarioValid(nome,idade,matricula)) {
			this.nome=nome;
			this.idade=idade; 
			this.matricula=matricula;
		
		} else {
			System.out.println("Usuario Invalido");
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if(Validadores.isNomeValid(nome)) {
			this.nome = nome;
		}else {
			System.out.println("Insira um Nome Válido!");
		}
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		if(Validadores.isIdadeValid(idade)) {
			this.idade = idade;
		}else {
			System.out.println("Insira uma Idade Válida! (12 a 100 anos)");
		}
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		if(Validadores.isMatriculaValid(matricula)){
			this.matricula=matricula;
		}else {
			System.out.println("Insira uma matricula válida!");
		}
	}
   
	public void exibirInformacoes() {
		System.out.println("Nome: " + this.getNome() + "Idade: " + this.getIdade() + " " + this.getMatricula());
	}
	
	public boolean autenticarAcesso(String matricula) {
		if(matricula.equals(this.getMatricula())) {
			return true;
		}else {
			return false;
		}
	}
	
	
}

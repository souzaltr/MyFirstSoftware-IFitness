package model.entities;

import java.util.List;

import model.util.Validadores;

import java.io.Serializable;
import java.util.ArrayList;

public class Treinos implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private List<Exercicio> exercicios;
	
	public Treinos(String nome) {
		if(Validadores.isNomeTreinoValid(nome)) {
		this.nome=nome;
		this.exercicios=new ArrayList<>();
		}else {
			System.out.println("Treino Invalido");
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if(Validadores.isNomeTreinoValid(nome)) {
		this.nome = nome;
		}else {
			System.out.println("Insira um nome válido!");
		}
	}

	public List<Exercicio> getExercicios() {
		return exercicios;
	}
	
	public void setExercicios(List<Exercicio> exercicios) {
	    this.exercicios = exercicios;
	}
	
	public void adicionarExercicio(Exercicio exercicio) {
		exercicios.add(exercicio);
	}

	@Override
	public String toString() {
		return "Nome:" + nome + "\n" + " Exercicios: " + exercicios.toString();
	}
	
}

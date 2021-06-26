package it.polito.tdp.artsmia.model;

public class ArtistiConnessi implements Comparable<ArtistiConnessi> {

	Artist a;
	int peso;
	
	
	public ArtistiConnessi(Artist a, int peso) {
		super();
		this.a = a;
		this.peso = peso;
	}

	

	public Artist getA() {
		return a;
	}



	public void setA(Artist a) {
		this.a = a;
	}



	public int getPeso() {
		return peso;
	}



	public void setPeso(int peso) {
		this.peso = peso;
	}



	@Override
	public int compareTo(ArtistiConnessi o) {
		// TODO Auto-generated method stub
		return -(this.getPeso()-o.getPeso());
	}
}

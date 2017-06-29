package movie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Movie {
	private int cantNodos;
	private int finMovie;
	private int[][] matAdy; 
	private ArrayList<Segmento> segmentos = new ArrayList<Segmento>();
	private int[] vectorSolucion;
	public Movie(String path) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path));
		int j;
		int resta;
		sc.useLocale(Locale.ENGLISH);
		cantNodos = sc.nextInt();
		finMovie = sc.nextInt();
		matAdy = new int[cantNodos][cantNodos];
		vectorSolucion = new int[cantNodos];
		for (int i = 0; i < matAdy.length; i++) {
			for (int k = 0; k < matAdy.length; k++) {
				matAdy[i][k] = 999;
			}
		}
		for (int i = 0; i < cantNodos; i++) {
			sc.nextInt();
			segmentos.add(new Segmento(sc.nextInt(),sc.nextInt()));
		}
		sc.close();
		for (int i = 0; i < segmentos.size(); i++) {
			j = i + 1;
			while(j < cantNodos) {
				if (segmentos.get(i).getFin() != finMovie && (segmentos.get(i).getFin() == segmentos.get(j).getInicio()+1 || comprendido(j, i))) {
					if(segmentos.get(i).getFin() == segmentos.get(j).getInicio()+1) {
						matAdy[i][j] = 0;
						matAdy[j][i] = 0;
						
					} else {
						resta = Math.abs(segmentos.get(j).getInicio() - segmentos.get(i).getFin());
						matAdy[i][j] = resta + 1;
						matAdy[j][i] = resta + 1;
					}
				}
				j++;
			}
			
		}
	}

	private boolean comprendido(int j, int i) {
		return segmentos.get(j).getInicio() <= segmentos.get(i).getFin() &&
				segmentos.get(j).getInicio() > segmentos.get(i).getInicio();
	}
	
	public void mostrarIn() {
		for (int i = 0; i < matAdy.length; i++) {
			for (int j = 0; j < matAdy.length; j++) {
				System.out.print(matAdy[i][j] + "   ");
			}
			System.out.println();
		}
	}
	
	
	public void resolver() {
		int[] precedencia = new int[cantNodos];
		int[] costo = new int[cantNodos];
		int[] vecAux = new int[cantNodos];
		int menor = Integer.MAX_VALUE;
		int aristasActuales = 0;
		int j = 0;
		int z = 0;
		resolverDijkstra(precedencia, costo);
		for (int i = 0; i < vecAux.length; i++) {
			vecAux[i] = -1;
		}
		for (int i = 0; i < segmentos.size(); i++) {
			if (segmentos.get(i).getFin() == finMovie) {
				z = i;
				while (j != vecAux.length && precedencia[z] != -1) {
					vecAux[j] = z;
					aristasActuales++;
					z = precedencia[z];
					j++;
				}
				vecAux[j] = z;
				aristasActuales++;
				z = precedencia[z];
				if (menor > aristasActuales) {
					menor = aristasActuales;
					vectorSolucion = vecAux.clone();
				}
				aristasActuales = 0;
				j = 0;
			}
		}
	}
	
	private void resolverDijkstra(int[] precedencia, int[] costo) {
		ArrayList<Integer> conjS = new ArrayList<Integer>();
		ArrayList<Integer> conjNS = new ArrayList<Integer>();
		int w;
		for (int i = 0; i < costo.length; i++) {
			precedencia[i] = 0;
			costo[i] = matAdy[0][i];
			conjNS.add(i);
		}
		precedencia[0] = -1;
		costo[0] = -99;
		conjS.add(0);
		conjNS.remove(0);
		while (!conjNS.isEmpty()) {
			w = findLower(costo, conjNS);
			conjS.add((Integer.valueOf(w)));
			conjNS.remove((Object)(w));
			if(buscarAdyacencia(w,conjNS)) {
				for (int i = 0; i < matAdy.length; i++) {
					if(matAdy[w][i] != 999) {
						if(costo[i] > costo[w] + matAdy[w][i]) {
							costo[i] = costo[w] + matAdy[w][i];
							precedencia[i] = w;
						}
					}
				}
			}
		}

	}

	private boolean buscarAdyacencia(int w, ArrayList<Integer> conjNS) {
		for (int i = 0; i < matAdy.length; i++) {
			if(conjNS.contains(i) && matAdy[w][i] != 999) {
				return true;
			}
		}
		return false;
	}

	private int findLower(int[] costo, ArrayList<Integer> conjNS) {
		int menor = Integer.MAX_VALUE;
		int retorno = -1;
		for (int i = 0; i < costo.length; i++) {
			if (conjNS.contains(i) && menor > costo[i]) {
				menor = costo[i];
				retorno = i;
			}
		}
		return retorno;
	}
	
	public void mostrarSolucion() {
		System.out.println();
		for (int i = vectorSolucion.length -1; i >= 0; i--) {
			if ( vectorSolucion[i] != -1){
				System.out.print(Integer.valueOf(vectorSolucion[i]+1) + " ");
			}
			
		}
	}
	
	public void imprimir() throws IOException {
		PrintWriter pr = new PrintWriter(new FileWriter("nico.txt"));
		
		for (int i = 0; i < matAdy.length; i++) {
			pr.print("{");
			for (int j = 0; j < matAdy.length; j++) {
				pr.print(matAdy[i][j] + ",");
			}
			pr.println("}");
			
		}
		pr.close();
	}
	
}

package movie;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		Movie movie = new Movie("pelicula1.in");
//		movie.mostrarIn();
		movie.resolver();
		movie.mostrarSolucion();
		

	}

}

package aed.iteradores;


import es.upm.aedlib.indexedlist.ArrayIndexedList;
import es.upm.aedlib.indexedlist.IndexedList;

import java.util.Iterator;


/**
 * Administra una coleccion de asignaturas.
 */
public class Secretaria {
    private Iterable<AsignaturaAdmin> asignaturas;

  /**
   * Empieza administrar una coleccion de asignaturas.
   */
    public Secretaria(Iterable<AsignaturaAdmin> asignaturas) {
	this.asignaturas = asignaturas;
    }

    private AsignaturaAdmin findAsignatura(String asignatura) {
	Iterator<AsignaturaAdmin> it = asignaturas.iterator();
	AsignaturaAdmin res = null; 
	while (it.hasNext() && res == null) {
          AsignaturaAdmin admin = it.next();
	  if (admin.getNombreAsignatura().equals(asignatura)) {
	      res = admin;
	  }
	}
	return res;
    }

    private AsignaturaAdmin getAsignatura(String asignatura)
	throws InvalidAsignaturaException {
	AsignaturaAdmin admin = findAsignatura(asignatura);
	if (admin == null) throw new InvalidAsignaturaException();
	else return admin;
    }

   
  /**
   * Matricula una coleccion de alumnos (representados por el
   * parametro matriculas) en una asignatura.
   * @return los números de matricula de los alumnos matriculados.
   * @throws InvalidAsignaturaException si la asignatura no
   * está siendo administrada por la secretaría.
   */
    public Iterable<String> matricular(String asignatura, Iterable<String> matriculas)
	throws InvalidAsignaturaException {
       return getAsignatura(asignatura).matricular(matriculas);
    }

  /**
   * Desmatricula una coleccion de alumnos (representados por el
   * parametro matriculas) de una asignatura.
   * @return las matriculas desmatriculados (que debían estar
   * matriculados y no tener nota).
   * @throws InvalidAsignaturaException si la asignatura no está
   * siendo administrado por la secretaria.
   */
    public Iterable<String> desMatricular(String asignatura, Iterable<String> matriculas)
	throws InvalidAsignaturaException {
	return getAsignatura(asignatura).desMatricular(matriculas);
    }

  /**
   * Calcula la nota media de un alumno (representado por su
   * identificador de matrícula) en todas asignaturas en las que está
   * matriculado.  Si el alumno no tiene ninguna nota en ninguna
   * asignatura, el metodo debe devolver 0.
   * @return la nota media del alumno.
   */
    public double notaMediaExpediente (String matricula) {
      // Completar este metodo
        double res = 0;
        double sum = 0;
        int count = 0;

        Iterator<AsignaturaAdmin> aux = asignaturas.iterator();

        while (aux.hasNext()){
            AsignaturaAdmin asignatura = aux.next();

            try {
                if(asignatura.estaMatriculado(matricula) && asignatura.tieneNota(matricula)){
                    sum = sum + asignatura.getNota(matricula);
                    count++;
                }
            } catch (InvalidMatriculaException e) {
                e.printStackTrace();
            }
        }

        if (sum == 0){
            return res;
        }

        res = sum/count;


      return res;
    }

  /**
   * Devuelve el nombre de la asignatura que tiene la mejor nota
   * media, calculada usando las notas de todos los alumnos que tienen
   * notas para esa asignatura.  Si la secretaria no esta
   * administrando ninguna asignatura, el metodo debe devolver
   * null. Similarmente, si ningún alumno tiene nota en ninguna
   * asignatura, el metodo tambien debe devolver null.
   * @return el nombre de la asignatura con la mejor nota media.
   */
    public String mejorNotaMedia() {
      // Completar este metodo
        Iterator<AsignaturaAdmin> aux = asignaturas.iterator();

        double max = 0;
        String res = null;

        while (aux.hasNext()){
            AsignaturaAdmin asignatura = aux.next();
            if (asignatura.notaMedia() > max){
                max = asignatura.notaMedia();
                res = asignatura.getNombreAsignatura();
            }

        }
      return res;
    }

  /**
   * Devuelve todas las notas de un alumno (representado por su
   * identificador de matrícula) como una coleccion de objetos
   * Pair(NombreAsignatura, Nota).
   * @return una coleccion de pares de las notas de la matricula en
   * todas las asignaturas.
   */
    public Iterable<Pair<String,Integer>> expediente(String matricula) {
      // Completar este metodo
        Iterator<AsignaturaAdmin> aux = asignaturas.iterator();
        IndexedList<Pair<String, Integer>> res = new ArrayIndexedList<Pair<String, Integer>>();
        int pos = 0;

        while (aux.hasNext()){
            AsignaturaAdmin asignatura = aux.next();
            int nota = 0;
            try {
                nota = asignatura.getNota(matricula);
                Pair<String, Integer> a = new Pair<String, Integer>(asignatura.getNombreAsignatura(), nota);
                res.add(pos, a);
                pos++;

            } catch (InvalidMatriculaException e) {
                e.printStackTrace();
            }
        }
      return res;
    }

  /**
   * Devuelve una coleccion con todas los pares de asignaturas --
   * representados como Pair(NombreAsignatura1, NombreAsignatura2) --
   * que no tienen alumnos en comun.  El metodo NO debe devolver nunca
   * un par Pair(NombreAsignatura,NombreAsignatura), es decir, con
   * nombres iguales de asignaturas.  Si dos asignaturas A1 y A2 no
   * tienen ningún alumno en común, para ellas se puede devolver: (i)
   * Pair(A1,A2), o (ii) Pair(A1,A2), Pair(A2,A1), o (iii)
   * Pair(A2,A1).
   * @return una coleccion que contiene todos los pares de asignaturas
   * que no tienen ningún alumno en comun.
   */
    public Iterable<Pair<String,String>> asignaturasNoConflictivas () {
      // Completar este metodo
        IndexedList<Pair<String, String>> res = new ArrayIndexedList<Pair<String, String>>();
        Iterator<AsignaturaAdmin> aux = asignaturas.iterator();
        int a = 0;
        int b;

        while (aux.hasNext()){
            AsignaturaAdmin asignatura1 = aux.next();
            a++;
            Iterator<AsignaturaAdmin> aux2 = asignaturas.iterator();
            b = 0;
            while(aux2.hasNext()){
                AsignaturaAdmin asignatura2 = aux2.next();
                b++;
                if (b<=a && aux2.hasNext()){
                    asignatura2 = aux2.next();
                    b++;
                }
                if (!compartenAlumnos(asignatura1, asignatura2)){
                    Pair<String, String> var = new Pair<String, String>(asignatura1.getNombreAsignatura(), asignatura2.getNombreAsignatura());
                    if (!var.getRight().equals(var.getLeft())) {
                        res.add(res.size(), var);
                    }
                }
            }
        }

      return res;
    }

  /**
   * Devuelve true si dos asignaturas a1 y a2 tienen algún alumno en
   * comun.
   * @return true si las dos asignaturas no tienen ningún alumno en comun.
   */
  private boolean compartenAlumnos (AsignaturaAdmin a1, AsignaturaAdmin a2) {
    // Hay que modificar este metodo
      Iterable<String> alumnos = a1.matriculados();
      Iterable<String> alumnos2 = a2.matriculados();

      Iterator<String> aux1 = alumnos.iterator();

      boolean found = false;

      while (aux1.hasNext() && !found){
          String alumno1 = aux1.next();
          Iterator<String> aux2 = alumnos2.iterator();
          while (aux2.hasNext() && !found){
              String alumno2 = aux2.next();
              if (alumno1.equals(alumno2)){
                  found = true;
              }
          }
      }
    return found;
  }
}

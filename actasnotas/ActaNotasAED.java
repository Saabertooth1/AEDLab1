package aed.actasnotas;

import es.upm.aedlib.indexedlist.*;

import java.util.Comparator;

public class ActaNotasAED implements ActaNotas {

    IndexedList<Calificacion> acta = new ArrayIndexedList<Calificacion>();

    public void addCalificacion(String nombre, String matricula, int nota) throws CalificacionAlreadyExistsException {

        Calificacion aux = new Calificacion(nombre, matricula, nota);

        if (acta.size()==0){
            acta.add(0, aux);
        }

        else {

            for (int i = 0; i < acta.size(); i++){
                if (matricula.equals(acta.get(i).getMatricula())){
                    throw new CalificacionAlreadyExistsException();
                }
            }

            acta.add(0, aux);
        }
    }

    public void updateNota(String matricula, int nota) throws InvalidMatriculaException {

        if (acta.size()==0){
            throw new InvalidMatriculaException();
        }

        else {

            boolean existe = false;

            for (int i = 0; i < acta.size() && !existe; i++){
                if (acta.get(i).getMatricula().equals(matricula)){
                    acta.get(i).setNota(nota);
                    existe = true;
                }
            }

            if (!existe){
                throw new InvalidMatriculaException();
            }
        }
    }

    public void deleteCalificacion(String matricula) throws InvalidMatriculaException {

        if (acta.size()==0){
            throw new InvalidMatriculaException();
        }

        else {

            boolean existe = false;

            for (int i = 0; i < acta.size() && !existe; i++){
                if (acta.get(i).getMatricula().equals(matricula)){
                    acta.removeElementAt(i);
                    existe = true;
                }
            }

            if (!existe){
                throw new InvalidMatriculaException();
            }
        }
    }

    public Calificacion getCalificacion(String matricula) throws InvalidMatriculaException {

        Calificacion aux = new Calificacion("", "", 0);

        if (acta.size()==0){
            throw new InvalidMatriculaException();
        }

        else {

            boolean existe = false;

            for (int i = 0; i < acta.size() && !existe; i++){
                if (acta.get(i).getMatricula().equals(matricula)){
                    aux = acta.get(i);
                    existe = true;
                }
            }

            if (!existe){
                throw new InvalidMatriculaException();
            }
        }

        return aux;
    }

    public IndexedList<Calificacion> getCalificaciones(Comparator<Calificacion> cmp) {

        IndexedList<Calificacion> aux = new ArrayIndexedList<Calificacion>();

        if (acta.size() != 0){
            aux.add(0, acta.get(0));

            boolean done = false;

            for (int i = 1; i < acta.size(); i++) {
                done = false;
                for (int j = 0; j < aux.size() && !done; j++) {
                    if (cmp.compare(acta.get(i), aux.get(j)) < 0) {
                        aux.add(j, acta.get(i));
                        done = true;
                    }
                }

                if (!done) {
                    aux.add(aux.size(), acta.get(i));
                    done = true;
                }
            }
        }

        return aux;
    }

    public IndexedList<Calificacion> getAprobados(int notaMinima) {

        IndexedList<Calificacion> aux = new ArrayIndexedList<Calificacion>();
        int count = 0;
        for(int i = 0; i < acta.size(); i++){
            if (acta.get(i).getNota() >= notaMinima){
                aux.add(count, acta.get(i));
                count ++;
            }
        }
        return aux;
    }
}

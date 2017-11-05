package aed.zork;

import es.upm.aedlib.positionlist.*;

import java.util.Iterator;


public class Explorador {
  
  public static Pair<Object,PositionList<Lugar>> explora(Lugar inicialLugar) {
      Lugar actual = inicialLugar;
      PositionList<Lugar> path = new NodePositionList<Lugar>();
      Pair<Object,PositionList<Lugar>> res = null;

      path.addFirst(actual);
      if(!actual.sueloMarcadoConTiza()){
          if(actual.tieneTesoro()){
              Object tesoro = actual.getTesoro();
              res = new Pair<Object, PositionList<Lugar>>(tesoro, path);
              return res;
          }
          actual.marcaSueloConTiza();
          Iterable<Lugar> aux = actual.caminos();
          Iterator<Lugar> var = aux.iterator();

          while (var.hasNext()){
              Lugar next = var.next();
              if(!next.sueloMarcadoConTiza()){
                  res = explora(next);
              }
              if(res!=null){
                  path = res.getRight();
                  path.addFirst(actual);
                  Pair<Object,PositionList<Lugar>> res2 = new Pair<Object, PositionList<Lugar>>(res.getLeft(),path);
                  return res2;
              }
          }
      }
      return null;
  }
}

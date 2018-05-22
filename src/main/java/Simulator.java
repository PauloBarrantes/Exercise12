/**
 * Class that executes a simulation of a FERRY.

 */

import java.util.*;

class Simulator {

    private Comparator<Event> comparator = new Comparator<Event>() {
        public int compare(Event o1, Event o2) {
            if (o1.getTime() < o2.getTime())
            {
                return -1;
            }
            if ( o1.getTime()  > o2.getTime() )
            {
                return 1;
            }
            return 0;
        }
    };
    private PriorityQueue<Event> tableOfEvents = new PriorityQueue<Event>(100,comparator);
    private double clock;
    private int estadoFerry;
    private double tiempoEsperaFerry;
    private int queueLengthPuntarenas;
    private int queueLengthPlayaNaranjo;

    private Random rnd = new Random(System.currentTimeMillis());
    private GeneradorNormal generadorNormal = new GeneradorNormal(6,1);
    private GeneradorExponencial generadorExponencial = new GeneradorExponencial(10);
    private GeneradorFuncionX generadorFuncionX = new GeneradorFuncionX();


    /**Class constructor.

     */
    Simulator() {

        this.clock = 0.0;
        this.estadoFerry = 0;   // 0 - Puntarenas - 1 Viajando - 2 Playa Naranjo
        this.queueLengthPlayaNaranjo =0;
        this.queueLengthPuntarenas = 0;

    }

    void simulate(){

        Event initialArrive = new Event(0.0,0);

        tableOfEvents.add(initialArrive);
        Event actualEvent;
        while(clock < 60){
            //Get next event
            actualEvent =  tableOfEvents.poll();

            //Process that event
            assert actualEvent != null;
            if(actualEvent.getType()==0){
               // this.processArrival();
            }else{
              // this.processDeparture();
            }
            //Move the clock

            assert tableOfEvents.peek() != null;

            clock = tableOfEvents.peek().getTime();

        }

    }
    private void procesarLlegadaP(){
        if ((queueLengthPuntarenas >= 40|| tiempoEsperaFerry >=240) && estadoFerry ==0 ){
            generarViaje();
            queueLengthPuntarenas -= 40;
        }else{
            queueLengthPuntarenas++;
        }
        generarLlegada();
    }


    private void procesarLlegadaPN(){
        if ((queueLengthPlayaNaranjo >= 40 || tiempoEsperaFerry >=240) && estadoFerry ==0 ){
            generarViaje();
            queueLengthPlayaNaranjo -= 40;
        }else{
            queueLengthPlayaNaranjo++;
        }
        generarLlegada();
    }

    private void procesarViajes(){

    }



    private void generarLlegada(){
        Event event;
        int type =0;
        double time;

        int tamano = 0;
        //Primero determinamos donde va aparecer el carro // Puntarenas o Playa Naranjo
        if(rnd.nextDouble() >=0.5){
            type = 2;
        }
        //Luego vemos si es carro liviano o carro pesado

        if(type == 0){
            if(rnd.nextDouble() < 0.3){
                type++;
            }
        }else{
            if(rnd.nextDouble() < 0.8){
                type++;
            }
        }
        //Ahora vemos en que tiempo va aparecer
        if(type <= 1){
            time = generadorNormal.generate();
        }else{
            time = generadorExponencial.generate();
        }
        // EXP - Puntarenas
        // Normal - Playa Naranjo

        event = new Event(time+this.clock,type);
        tableOfEvents.add(event);
    }

    private void generarViaje(){
        Event event;
        Double time = generadorFuncionX.generate();

        event = new Event(time+this.clock, 4);
        tableOfEvents.add(event);
    }



}

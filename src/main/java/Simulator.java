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
    private int promedioDinero;
    private int tiempoPromedioColaP;
    private int tiempoPromedioColaPN;
    private int tiempoPromedioSistema;
    private Random rnd = new Random(System.currentTimeMillis());
    private GeneradorNormal generadorNormal = new GeneradorNormal(6,1);
    private GeneradorExponencial generadorExponencial = new GeneradorExponencial(10);
    private GeneradorFuncionX generadorFuncionX = new GeneradorFuncionX();
    private ArrayList<Carro> listaCarrosPuntarenas = new ArrayList<Carro>(200);
    private ArrayList<Carro> listaCarrosPlayaNaranjo= new ArrayList<Carro>(200);
    int indiceP;
    int indicePN;
    /**Class constructor.

     */
    Simulator() {

        this.clock = 0.0;
        this.estadoFerry = 0;   // 0 - Puntarenas - 1 Viajando - 2 Playa Naranjo
        this.queueLengthPlayaNaranjo =0;
        this.queueLengthPuntarenas = 0;
        this.promedioDinero = 0;
        this.tiempoPromedioColaP =0;
        this.tiempoPromedioColaPN = 0;
        this.tiempoPromedioSistema = 0;
        this.indiceP = 0;
        this.indicePN =0;
    }

    void simulate(){

        Event initialArrive = new Event(0.0,0);

        tableOfEvents.add(initialArrive);
        Event actualEvent;
        while(clock < 360){
            //Get next event
            actualEvent =  tableOfEvents.poll();

            //Process that event
            assert actualEvent != null;

            if(actualEvent.getType() <=1){
                if(actualEvent.getType() == 0){
                    promedioDinero +=1500;
                }else{
                    promedioDinero +=2000;
                }
                listaCarrosPuntarenas.add(indicePN, new Carro(clock));
                procesarLlegadaP();
            }else{
                if(actualEvent.getType() <= 3){
                    if(actualEvent.getType() == 2){
                        promedioDinero +=1500;
                    }else{
                        promedioDinero +=2000;
                    }
                    listaCarrosPlayaNaranjo.add(indicePN, new Carro(clock));
                    procesarLlegadaPN();
                }else{
                    if(actualEvent.getType()== 4){
                        procesarViajesPN();
                    }else{
                        procesarViajesP();
                    }
                }
            }
            if(actualEvent.getType() <4){
                if(actualEvent.getType() <= 1){
                    if(actualEvent.getType() == 0){
                        System.out.println("Clock: " + clock +" "+ "QLP: " + queueLengthPuntarenas+" " + "QLPN: " + queueLengthPlayaNaranjo + " Lugar: Puntarenas " + "Carro: Liviano" ) ;

                    }else{
                        System.out.println("Clock: " + clock +" "+ "QLP: " + queueLengthPuntarenas+" " + "QLPN: " + queueLengthPlayaNaranjo + " Lugar: Puntarenas " + "Carro: Pesado" ) ;

                    }
                }else{
                    if(actualEvent.getType() == 2){
                        System.out.println("Clock: " + clock +" "+ "QLP: " + queueLengthPuntarenas+" " + "QLPN: " + queueLengthPlayaNaranjo + " Lugar: Playa Naranjo " + "Carro: Liviano" ) ;

                    }else{
                        System.out.println("Clock: " + clock +" "+ "QLP: " + queueLengthPuntarenas+" " + "QLPN: " + queueLengthPlayaNaranjo + " Lugar: Playa Naranjo " + "Carro: Pesado" ) ;

                    }
                }

            }else{
                System.out.println("Clock: " + clock +" "+ "QLP: " + queueLengthPuntarenas+" " + "QLPN: " + queueLengthPlayaNaranjo);
            }

            //System.out.println("Tiempo que lleva esperando el ferry   " + tiempoEsperaFerry);

           // System.out.println("Tiempo Promedio En El Sistema  " + 1);



            assert tableOfEvents.peek() != null;

            clock = tableOfEvents.peek().getTime();

        }
        System.out.println("Dinero Promedio Generado  " + promedioDinero);

        System.out.println("Tiempo Promedio En la Cola Puntarenas  " + calcularTiempoEsperaP());
        System.out.println("Tiempo Promedio En la Cola Playa Naranjo  " + calcularTiempoEsperaPN());

    }
    private void procesarLlegadaP(){
        System.out.println("Proceso Llegada a Puntarenas");
        if ((queueLengthPuntarenas >= 39|| tiempoEsperaFerry >=240) && estadoFerry ==0 ){
            tiempoEsperaFerry = 0;
            generarViaje(5);
            estadoFerry = 1;

            if(queueLengthPuntarenas>=39) {
                queueLengthPuntarenas -= 39;
                for(int i = indiceP; i < indiceP+40;++i){
                    listaCarrosPuntarenas.get(i).setHoraMontadaFerry(clock);
                }
                indiceP+=40;
            }else{
                for(int i = indiceP; i < indiceP+queueLengthPuntarenas;++i){
                    listaCarrosPuntarenas.get(i).setHoraMontadaFerry(clock);
                }
                indiceP+=queueLengthPuntarenas;
                queueLengthPuntarenas -= queueLengthPuntarenas;
            }
        }else{
            queueLengthPuntarenas++;
        }
        generarLlegada();
    }


    private void procesarLlegadaPN(){
        System.out.println("Proceso Llegada a Playa Naranjo");

        if ((queueLengthPlayaNaranjo >= 39 || tiempoEsperaFerry >=240) && estadoFerry ==2 ){
            tiempoEsperaFerry = 0;
            generarViaje(4);
            estadoFerry = 1;
            if(queueLengthPlayaNaranjo >= 39){
                queueLengthPlayaNaranjo -= 39;
                for(int i = indicePN; i < indicePN+40;++i){
                    listaCarrosPlayaNaranjo.get(i).setHoraMontadaFerry(clock);
                }
                indicePN+=40;
            }else{
                for(int i = indicePN; i < indicePN+queueLengthPlayaNaranjo;++i){
                    listaCarrosPlayaNaranjo.get(i).setHoraMontadaFerry(clock);
                }
                indicePN+=queueLengthPlayaNaranjo;
                queueLengthPlayaNaranjo -= queueLengthPlayaNaranjo;
            }
        }else{
            queueLengthPlayaNaranjo++;
        }
        generarLlegada();
    }

    private void procesarViajesP(){ //Procesan los viajes que fueron de Puntarenas a Playa Naranjo
        System.out.println("Proceso Viaje de Puntarenas a Playa Naranjo");
        for(int i = indiceP-40; i < indiceP; ++i){
            listaCarrosPuntarenas.get(i).setHoraSalida(clock);
        }
        tiempoEsperaFerry = 0;
        estadoFerry = 2;

        if(queueLengthPlayaNaranjo >= 40){
            queueLengthPlayaNaranjo-=40;
            generarViaje(5);
        }
        generarLlegada();

    }
    private void procesarViajesPN(){ //Procesan los viajes que fueron de Playa Naranjo a Puntarenas
        System.out.println(" Proceso Viaje de Pn a P");
        for(int i = indicePN-40; i < indicePN; ++i){
            listaCarrosPlayaNaranjo.get(i).setHoraSalida(clock);
        }
        tiempoEsperaFerry = 0;
        estadoFerry = 0;
        if(queueLengthPuntarenas >= 40){
            queueLengthPuntarenas -=40;
            generarViaje(4);
        }
        generarLlegada();
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
        tiempoEsperaFerry += time;
        tableOfEvents.add(event);
    }

    private double calcularTiempoEsperaP(){
        double tiempoPromedio =0.0;
        for(int i =0; i < indiceP;++i){
            tiempoPromedio+= listaCarrosPuntarenas.get(i).getHoraMontadaFerry() - listaCarrosPuntarenas.get(i).getHoraLlegada();
        }
        return tiempoPromedio/indiceP;
    }

    private double calcularTiempoEsperaPN(){
        double tiempoPromedio =0.0;
        for(int i =0; i < indicePN;++i){
            tiempoPromedio+= listaCarrosPlayaNaranjo.get(i).getHoraMontadaFerry() - listaCarrosPlayaNaranjo.get(i).getHoraLlegada();
        }
        return tiempoPromedio/indicePN;
    }

    private void generarViaje(int type){
        Event event;
        Double time = generadorFuncionX.generate();
        tiempoEsperaFerry += time;

        event = new Event(time+this.clock, type);
        tableOfEvents.add(event);
    }




}

class Carro {
    private final double horaLlegada;
    private double horaMontadaFerry;
    private double horaSalida;

    Carro(double horaLlegada){
        this.horaLlegada = horaLlegada;
    }

    public double getHoraLlegada() {
        return horaLlegada;
    }

    public double getHoraMontadaFerry() {
        return horaMontadaFerry;
    }

    public void setHoraMontadaFerry(double horaMontadaFerry) {
        this.horaMontadaFerry = horaMontadaFerry;
    }

    public double getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(double horaSalida) {
        this.horaSalida = horaSalida;
    }
}

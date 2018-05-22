/**
 * Event of simulation
 */

//Tipos de eventos
// 0 - Llegada en Puntarenas un auto liviano
// 1 - Llegada en Puntarenas un auto pesado
// 2 - Llegada en Playa Naranjo un auto liviano
// 3 - Llegada en Playa Naranjo un auto pesado
// 4 - Viaje del ferry
public class Event {
    private final double time;
    private final int type;
    /**
     Event Constructor
     @param time
     @param type
     */
    public Event(double time, int type) {
        this.time = time;
        this.type = type;

    }

    public double getTime() {
        return time;
    }


    public int getType() {
        return type;
    }

}

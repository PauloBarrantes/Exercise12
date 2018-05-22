

import java.util.Random;

public class GeneradorNormal {
    private double media ;
    private double varianza;
    private double desviacion;
    private Random rnd = new Random(System.currentTimeMillis());

    public GeneradorNormal(double media, double varianza) {
        this.media = media;
        this.varianza= varianza;
        this.desviacion = Math.sqrt(varianza);
    }

    public double generate(){

        double zeta= 0;

        for(int i = 0; i <12 ; ++i){
            double numRandom = rnd.nextDouble();
            zeta += numRandom;
        }
        zeta = zeta - 6;
        double time = media + desviacion * zeta;
        return time;
    }
}

import java.util.Random;

public class GeneradorExponencial {
    private double media ;
    private double lambda;
    private Random rnd = new Random(System.currentTimeMillis());

    public GeneradorExponencial(double media) {
        this.lambda= 1/media;
        this.media = media;

    }

    public double generate(){

        double numRandom = rnd.nextDouble();
        double time = (-1/lambda)*Math.log(1-numRandom);
        return time;
    }
}

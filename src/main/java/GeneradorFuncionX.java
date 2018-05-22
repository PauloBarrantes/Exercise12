import java.util.Random;


public class GeneradorFuncionX {

    private Random rnd = new Random(System.currentTimeMillis());

    public GeneradorFuncionX() {


    }

    public double generate(){

        double numRandom = rnd.nextDouble();
        double time = Math.sqrt(numRandom*300 +100);
        return time;
    }
}
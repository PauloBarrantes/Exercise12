public class Application {
    Simulator simulator = new Simulator();
    public void run(){
        simulator.simulate();
    }


    public static void main(String[] args){
        Application app = new Application();
        app.run();
    }
}

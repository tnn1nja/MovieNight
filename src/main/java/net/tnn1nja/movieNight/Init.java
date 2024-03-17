package net.tnn1nja.movieNight;

public class Init {

    //Argument
    public static String arg = null;

    //Start Program
    public static void main(String[] args) {

        //Pass In Arguments
        if (args.length>0) {
            arg = args[0];
        }

        //Start Program
        Main.onStart(arg);

    }

}
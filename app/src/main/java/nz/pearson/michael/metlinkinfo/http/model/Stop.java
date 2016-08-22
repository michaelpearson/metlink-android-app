package nz.pearson.michael.metlinkinfo.http.model;

public class Stop {

    private String name;
    private String stopNumber;


    public Stop(String name, String stopNumber) {
        this.name = name;
        this.stopNumber = stopNumber;
    }

    public String getName() {
        return name;
    }

    public String getStopNumber() {
        return stopNumber;
    }
}

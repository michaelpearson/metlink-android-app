package nz.pearson.michael.metlinkinfo.http.model;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;

public class Service {

    private String serviceID;
    private Boolean isRealtime;
    private String vehicleRef;
    private String direction;
    private String operatorRef;
    private String originStopID;
    private String originStopName;
    private String destinationStopID;
    private String destinationStopName;
    private DateTime aimedArrival;
    private DateTime aimedDeparture;
    private String vehicleFeature;
    private String departureStatus;
    private DateTime expectedDeparture;
    private DateTime displayDeparture;
    private Integer displayDepartureSeconds;
    private Service service;


    private DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");


    public Service(JSONObject obj) throws JSONException {
        serviceID = obj.getString("ServiceID");
        isRealtime = obj.getBoolean("IsRealtime");
        vehicleRef = obj.getString("VehicleRef");
        direction = obj.getString("Direction");
        operatorRef = obj.getString("OperatorRef");
        originStopID = obj.getString("OriginStopID");
        originStopName = obj.getString("OriginStopName");
        destinationStopID = obj.getString("DestinationStopID");
        destinationStopName = obj.getString("DestinationStopName");
        vehicleFeature = obj.getString("VehicleFeature");
        departureStatus = obj.getString("DepartureStatus");
        displayDepartureSeconds = obj.getInt("DisplayDepartureSeconds");
        aimedArrival = parseDate(obj.getString("AimedArrival"));
        aimedDeparture = parseDate(obj.getString("AimedDeparture"));
        expectedDeparture = parseDate(obj.getString("ExpectedDeparture"));
        displayDeparture = parseDate(obj.getString("DisplayDeparture"));
    }

    private DateTime parseDate(String date) {
        try {
            return parser.parseDateTime(date);
        } catch(IllegalArgumentException ignore) {}
        return null;
    }


    public String getServiceID() {
        return serviceID;
    }

    public Boolean getRealtime() {
        return isRealtime;
    }

    public String getVehicleRef() {
        return vehicleRef;
    }

    public String getDirection() {
        return direction;
    }

    public String getOperatorRef() {
        return operatorRef;
    }

    public String getOriginStopID() {
        return originStopID;
    }

    public String getOriginStopName() {
        return originStopName;
    }

    public String getDestinationStopID() {
        return destinationStopID;
    }

    public String getDestinationStopName() {
        return destinationStopName;
    }

    public DateTime getAimedArrival() {
        return aimedArrival;
    }

    public DateTime getAimedDeparture() {
        return aimedDeparture;
    }

    public String getVehicleFeature() {
        return vehicleFeature;
    }

    public String getDepartureStatus() {
        return departureStatus;
    }

    public DateTime getExpectedDeparture() {
        return expectedDeparture;
    }

    public DateTime getDisplayDeparture() {
        return displayDeparture;
    }

    public String getDisplayDepartureSeconds() {
        int seconds = displayDepartureSeconds;
        int minutes = seconds / 60;
        minutes = minutes % 60;
        String build;
        if(isRealtime) {
            build = String.format("%2d minutes", minutes);
        } else {
            build = getAimedDeparture().toString(formatter);

        }
        return (getRealtime() ? build : "Scheduled for " + build);

    }
}
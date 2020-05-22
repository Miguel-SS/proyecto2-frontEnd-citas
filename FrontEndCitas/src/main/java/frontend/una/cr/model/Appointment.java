package frontend.una.cr.model;

import java.util.Date;

public class Appointment {

    // Attributes
    private Patient patient;
    private int id;
    private Hospital hospital;
    private Date date;

    /**
     * @param patient
     * @param id
     * @param hospital
     * @param date
     */
    public Appointment(int id, Patient patient, Hospital hospital, Date date){
        this.id = id;
        this.patient = patient;
        this.hospital = hospital;
        this.date = date;
    }

    // Getters & setters
    public Hospital getHospital() { return hospital; }

    public void setHospital(Hospital hospital) { this.hospital = hospital; }

    public Patient getPatient() { return patient; }

    public void setPatient(Patient patient) { this.patient = patient; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "Appointment{" +
                "patient='" + patient + '\'' +
                ", id=" + id +
                ", hospital='" + hospital + '\'' +
                ", date=" + date +
                '}';
    }
}

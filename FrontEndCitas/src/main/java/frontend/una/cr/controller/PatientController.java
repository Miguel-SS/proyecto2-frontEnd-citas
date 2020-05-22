package frontend.una.cr.controller;

import frontend.una.cr.model.Appointment;
import frontend.una.cr.model.Patient;
import frontend.una.cr.service.ServiceFacade;
import frontend.una.cr.utilities.Constants;
import frontend.una.cr.view.PatientView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class PatientController implements ActionListener {

    private ServiceFacade service;
    private PatientView view;
    private Patient patient;
    private Object[][] appointments;

    // View attributes
    DefaultTableModel tableModel;


    /**
     * @param view
     * @param patient
     */
    public PatientController(PatientView view, Patient patient, ServiceFacade service) {
        this.service = service;
        this.patient = patient;
        this.view = view;
        appointments = service.loadAppointmentsFromPatientObjWrapper(patient.getId());
        tableModel = view.getTableModel();

        tableModel.setDataVector(appointments, Constants.PATIENT_APPOINTMENTS_HEADER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object ev = e.getSource();
        if(ev == view.getSaveBtn()) {
            updatePersonalInfo();
        }
        if(ev == view.getSearchBtn()) {
            updateTableSearchTerms(view.getSearchTxtField().getText());
        }
        if(ev == view.getLogOutBtn()) {
            view.dispose();
            try {
                LoginView newView = new LoginView(service);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if(ev == view.getDeleteBtn()) {
            deleteAppointment(view.getDeleteTextField());
        }
        if (ev == view.getRequestBtn()) {
            createAppointment();
        }

    }

    private void updatePersonalInfo() throws NumberFormatException, NullPointerException {
        // Obtaining text from txt field...
        String name = view.getNameTxtField().getText();
        String lastName = view.getLastNameTxtField().getText();
        String id = view.getIdTxtField().getText();
        String birthYear = view.getBirthYearTxtField().getText();
        String phone = view.getPhoneTxtField().getText();
        String address = view.getAddressTxtField().getText();

        // Check if the txt fields are empty
        if(!"".equals(name) && // checking name
                !"".equals(lastName) && // checking last name
                !"".equals(id) && // checking id
                !"".equals(birthYear) && // checking birth year
                !"".equals(phone) && // checking phone
                !"".equals(address) // checking address
        ) {
            patient.setName(name);
            patient.setLastName(lastName);
            patient.setId(Integer.parseInt(id));
            patient.setBirthYear(Integer.parseInt(birthYear));
            patient.setPhone(phone);
            patient.setAddress(address);
            view.updateTopLabels();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Uno o varios cuadros de texto estan vacios", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableSearchTerms(String searchTerm) {
        if (searchTerm != null && !"".equals(searchTerm) &&
                appointments != null && appointments.length > 1) {
            Object[][] newData = new Object[appointments.length][];
            int idx = 0;
            for (Object[] obj: appointments) {
                String fullText = obj[0].toString() + obj[1].toString()
                        + obj[2].toString() + obj[3].toString();

                if (fullText.contains(searchTerm.trim())) {
                    newData[idx++] = obj;
                }
            }
            tableModel.setDataVector(newData, Constants.PATIENT_APPOINTMENTS_HEADER);
        } else {
            tableModel.setDataVector(appointments, Constants.PATIENT_APPOINTMENTS_HEADER);
        }
    }

    private void deleteAppointment(String appointmentId) {
        if (appointmentId != null && !"".equals(appointmentId)) {
            Appointment auxAppointment = service.searchAppointment(Integer.parseInt(appointmentId));
            if (auxAppointment != null) {
                service.delete(auxAppointment);
                patient.delete(auxAppointment);
                appointments = service.loadAppointmentsFromPatientObjWrapper(patient.getId());
                tableModel.setDataVector(appointments, Constants.PATIENT_APPOINTMENTS_HEADER);
            } else {
                JOptionPane.showMessageDialog(null, "Cita no encontrada\nIntentelo de nuevo...",
                        "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void createAppointment() {
        char firstLetter = view.getHourComboBox().charAt(0);
        Date date = view.getDateTxtField();
        if(date != null && firstLetter != '-') {
            if(date.getDay() != 0 && date.getDay() != 3 && date.getDay() != 6) {
                int ranID = (new Random()).nextInt();
                while (service.searchAppointment(ranID) != null || ranID < 0) {
                    ranID = (new Random()).nextInt();
                }
                Appointment newAppointment = new Appointment(ranID,
                        patient, view.getHourComboBox(), date
                );
                service.add(newAppointment);
                patient.add(newAppointment);
                appointments = service.loadAppointmentsFromPatientObjWrapper(patient.getId());
                tableModel.setDataVector(appointments, Constants.PATIENT_APPOINTMENTS_HEADER);
            } else {
                JOptionPane.showMessageDialog(null, "Dia no disponible para ser atendido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

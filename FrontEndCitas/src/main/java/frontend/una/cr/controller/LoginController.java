package frontend.una.cr.controller;

import frontend.una.cr.model.Patient;
import frontend.una.cr.service.ServiceFacade;
import frontend.una.cr.view.LoginView;
import frontend.una.cr.view.PatientView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginController implements ActionListener {

    private final ServiceFacade service;
    private final LoginView view;


    public LoginController(LoginView log, ServiceFacade service) throws IOException {
        view = log;
        if(service == null) {
            this.service = new ServiceFacade();
        } else {
            this.service = service;
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object ev = actionEvent.getSource();
        if(ev == view.getOkBtn()) {
            try {
                loginAction();
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException |
                    InstantiationException | IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(ev == view.getSignInBtn()) {
            createNewPatient();
        }
    }

    private void loginAction() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IOException, IllegalAccessException {
        String id = view.getTextId();
        String password = view.getTextPassword();

        Object auxUser = service.searchUser(Integer.parseInt(id));
        if (auxUser != null) {
            if (auxUser.getClass() == Patient.class) {
                if (verifyPass(id, password, (Patient) auxUser)) {
                    view.dispose();
                    new PatientView((Patient) auxUser, service);
                } else { view.showError("Contrasena o usuario incorrecto"); }
            }/*
            if (auxUser.getClass() == AdminModel.class) {
                if (verifyPass(id, password, (AdminModel) auxUser)) {
                    view.dispose();
                    new AdminView((AdminModel) auxUser, service);
                } else { view.showError("Contrasena o usuario incorrecto"); }
            }*/
        } else { view.showError("Usuario no encontrado"); }
    }

    private void createNewPatient() throws NullPointerException, NumberFormatException{
        // Extracting the values of the text fields
        String name = view.getNameTxtField().getText();
        String lastName = view.getLastNameTxtField().getText();
        String birthYear = view.getBirthYearTxtField().getText();
        String id = view.getIdTxtField().getText();
        String phone = view.getPhoneTxtField().getText();
        String address = view.getAddressTxtField().getText();
        String password = view.getPasswordTxtField().getText();

        // If isn't empty
        if(!"".equals(name) && !"".equals(lastName) && !"".equals(birthYear) &&
                !"".equals(id) && !"".equals(phone) && !"".equals(address) && !"".equals(password)) {
            // Create a new patient
            Patient patient = new Patient(Integer.parseInt(id), name, lastName, Integer.parseInt(birthYear), phone, address, password);
            service.add(patient);
            JOptionPane.showMessageDialog(null,
                    "Cuenta creada con exito!\nBienvenido " + patient.getName(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            view.cleanTxtField();
        } else {
            JOptionPane.showMessageDialog(null,
                    "Uno o varios cuadros de texto estan vacios", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean verifyPass(String id, String password, Patient patient) {
        if (id != null && password != null && !"".equals(id) && !"".equals(password)) {
            if (patient != null) {
                if (id.equals(patient.getId()) && password.equals(patient.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    private boolean verifyPass(String id, String password, AdminModel admin) {
        if (id != null && password != null && !"".equals(id) && !"".equals(password)) {
            if (admin != null) {
                if (id.equals(admin.getId()) && password.equals(admin.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }
    */
}

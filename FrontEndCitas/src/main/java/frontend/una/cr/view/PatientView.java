package frontend.una.cr.view;

import frontend.una.cr.controller.PatientController;
import frontend.una.cr.model.Patient;
import frontend.una.cr.service.ServiceFacade;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class PatientView extends JFrame {

    private Patient patient;

    // Form attributes
    private JPanel rootPanel = new JPanel(new BorderLayout());

    // Top panel for personal info -------------------------
    private JPanel personalInfoPanel = new JPanel();
    private JLabel idPanelInfoLbl;
    private JLabel namePanelInfoLbl;
    private JButton logOutBtn;

    private JTabbedPane optionsTabPanel = new JTabbedPane(SwingConstants.LEFT);
    // Info tab --------------------------------------------
    private JPanel infoPanel = new JPanel();
    private JPanel saveBtnPanel = new JPanel();
    private JPanel lblsPanel = new JPanel(new GridLayout(-1,2));
    private JTextField nameTxtField;
    private JTextField lastNameTxtField;
    private JTextField idTxtField;
    private JTextField birthYearTxtField;
    private JTextField phoneTxtField;
    private JTextField addressTxtField;
    private JButton saveBtn;

    private JLabel nameLbl;
    private JLabel lastNameLbl;
    private JLabel idInfoLbl;
    private JLabel birthYearLbl;
    private JLabel phoneLbl;
    private JLabel addressLbl;

    // Appointments tab -----------------------------------
    private JPanel appointmentsPanel = new JPanel(new BorderLayout());
    private JPanel topAppointmentPanel = new JPanel();
    private JButton searchBtn;
    private JTextField searchTxtField;
    private JTable appointTable;
    private DefaultTableModel tableModel;
    private JButton deleteBtn;
    private JTextField deleteTextField;

    // Request tab ---------------------------------------
    private JPanel requestPanel = new JPanel(new BorderLayout());
    private JComboBox hourComboBox;
    private JButton requestBtn;
    private JFormattedTextField dateTxtField;


    public PatientView(Patient patient, ServiceFacade service) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, IOException {
        super("Patient");
        this.patient = patient;

        add(rootPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,350);
        setLookAndFeel();

        logOutBtn = new JButton("Cerrar Sesion");
        saveBtn = new JButton("Guardar");
        searchBtn = new JButton("Buscar");

        creatingTopPaneInfo();

        // Creating tab pane...
        creatingInfoTabPane();
        creatingAppointmentsTabPane();
        creatingRequestTabPane();

        // Creating the controller...
        PatientController controller = new PatientController(this, patient, service);
        addListeners(controller);

        rootPanel.add(optionsTabPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void updateTopLabels() {
        idPanelInfoLbl.setText("ID: " + patient.getId());
        namePanelInfoLbl.setText("Nombre: "+ patient.getName() + " " + patient.getLastName());
    }

    private static void setLookAndFeel() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(
                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"
        );
    }

    private void addListeners(PatientController c) {
        saveBtn.addActionListener(c);
        searchBtn.addActionListener(c);
        logOutBtn.addActionListener(c);
        deleteBtn.addActionListener(c);
        requestBtn.addActionListener(c);
    }

    private void creatingTopPaneInfo() {
        idPanelInfoLbl = new JLabel("ID: " + patient.getId());
        namePanelInfoLbl = new JLabel("Nombre: "+ patient.getName() + " " + patient.getLastName());

        logOutBtn.setHorizontalAlignment(SwingConstants.RIGHT);

        personalInfoPanel.add(idPanelInfoLbl);
        personalInfoPanel.add(namePanelInfoLbl);
        personalInfoPanel.add(logOutBtn);

        rootPanel.add(personalInfoPanel, BorderLayout.PAGE_START);
    }

    private void creatingInfoTabPane() {
        infoPanel.setLayout(new BorderLayout());

        nameLbl = new JLabel("Nombre: ");
        lastNameLbl = new JLabel("Apellido: ");
        idInfoLbl = new JLabel("ID: ");
        birthYearLbl = new JLabel("Nacimiento: ");
        phoneLbl = new JLabel("Telefono: ");
        addressLbl = new JLabel("Direccion: ");

        nameTxtField = new JTextField(patient.getName());
        lastNameTxtField = new JTextField(patient.getLastName());
        idTxtField = new JTextField(patient.getId());
        birthYearTxtField = new JTextField(String.valueOf(patient.getBirthYear()));
        phoneTxtField = new JTextField(patient.getPhone());
        addressTxtField = new JTextField(patient.getAddress());

        nameLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        lastNameLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        idInfoLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        birthYearLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        phoneLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        addressLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        lblsPanel.add(nameLbl);
        lblsPanel.add(nameTxtField);
        lblsPanel.add(lastNameLbl);
        lblsPanel.add(lastNameTxtField);
        lblsPanel.add(idInfoLbl);
        lblsPanel.add(idTxtField);
        lblsPanel.add(birthYearLbl);
        lblsPanel.add(birthYearTxtField);
        lblsPanel.add(phoneLbl);
        lblsPanel.add(phoneTxtField);
        lblsPanel.add(addressLbl);
        lblsPanel.add(addressTxtField);
        saveBtnPanel.add(saveBtn);
        infoPanel.add(lblsPanel);
        infoPanel.add(saveBtnPanel, BorderLayout.PAGE_END);
        optionsTabPanel.addTab("Informacion", infoPanel);
    }

    private void creatingAppointmentsTabPane() {
        searchTxtField = new JTextField();
        appointTable = new JTable();
        tableModel = new DefaultTableModel();
        JPanel bottomPane = new JPanel();
        deleteBtn = new JButton("Eliminar");
        deleteTextField = new JTextField("Id...");

        appointTable.setModel(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(appointTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Citas", TitledBorder.CENTER, TitledBorder.TOP
        ));
        searchTxtField.setColumns(20);
        deleteTextField.setColumns(15);
        topAppointmentPanel.add(searchTxtField);
        topAppointmentPanel.add(searchBtn);
        bottomPane.add(deleteTextField);
        bottomPane.add(deleteBtn);
        appointmentsPanel.add(topAppointmentPanel, BorderLayout.PAGE_START);
        appointmentsPanel.add(tableScrollPane, BorderLayout.CENTER);
        appointmentsPanel.add(new JPanel(),BorderLayout.LINE_START);
        appointmentsPanel.add(new JPanel(),BorderLayout.LINE_END);
        appointmentsPanel.add(bottomPane,BorderLayout.PAGE_END);

        optionsTabPanel.addTab("Citas", appointmentsPanel);
    }

    private void creatingRequestTabPane() {
        creatingTopPaneRequest();
        creatingBottomPaneRequest();
        optionsTabPanel.addTab("Solicitar", requestPanel);
    }

    private void creatingTopPaneRequest() {
        JPanel topRequestPane = new JPanel();
        topRequestPane.setLayout(new BoxLayout(topRequestPane, BoxLayout.PAGE_AXIS));
        JPanel datePanel = new JPanel();
        JPanel hourPanel = new JPanel();
        JLabel dateLbl = new JLabel("Fecha: (dd/mm/yy)");
        hourComboBox = new JComboBox();
        dateTxtField = new JFormattedTextField(DateFormat.getDateInstance(DateFormat.SHORT));
        dateTxtField.setColumns(15);

        hourComboBox.addItem("-Selecione una hora-");
        hourComboBox.addItem("7:00");
        hourComboBox.addItem("8:00");
        hourComboBox.addItem("9:00");
        hourComboBox.addItem("10:00");
        hourComboBox.addItem("11:00");
        hourComboBox.addItem("13:00");
        hourComboBox.addItem("14:00");
        hourComboBox.addItem("15:00");
        hourComboBox.addItem("16:00");

        datePanel.add(dateLbl);
        datePanel.add(dateTxtField);
        hourPanel.add(hourComboBox);
        topRequestPane.add(datePanel);
        topRequestPane.add(hourPanel);
        requestPanel.add(topRequestPane, BorderLayout.CENTER);
    }

    private void creatingBottomPaneRequest() {
        JPanel bottomRequestPane = new JPanel();
        requestBtn = new JButton("Solicitar");
        bottomRequestPane.add(requestBtn);
        requestPanel.add(bottomRequestPane, BorderLayout.PAGE_END);
    }

    // ***************************************** Getters **********************************************

    // ------------------------- Buttons -----------------------------
    public JButton getSaveBtn() { return saveBtn; }

    public JButton getSearchBtn() { return searchBtn; }

    public JButton getLogOutBtn() { return logOutBtn; }

    public JButton getDeleteBtn() { return deleteBtn; }

    public JButton getRequestBtn() { return requestBtn; }

    // -------------------------- Text fields --------------------------------

    // Info tab
    public JTextField getNameTxtField() { return nameTxtField; }

    public JTextField getLastNameTxtField() { return lastNameTxtField; }

    public JTextField getIdTxtField() {
        return idTxtField;
    }

    public JTextField getBirthYearTxtField() { return birthYearTxtField; }

    public JTextField getPhoneTxtField() { return phoneTxtField; }

    public JTextField getAddressTxtField() { return addressTxtField; }

    // Appointment tab
    public JTextField getSearchTxtField() { return searchTxtField; }

    public DefaultTableModel getTableModel() { return tableModel; }

    public String getDeleteTextField() { return deleteTextField.getText(); }

    // Request tab
    public Date getDateTxtField() { return (Date) dateTxtField.getValue(); }

    public String getHourComboBox() { return hourComboBox.getSelectedItem().toString(); }
}

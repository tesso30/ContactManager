package com.contacts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class ContactManagerForm {
    private JPanel mainPanel;
    private JList<String> contactList;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;

    private ArrayList<Contact> contacts;
    private DefaultListModel<String> listModel;

    public ContactManagerForm() {
        contacts = new ArrayList<>();
        listModel = new DefaultListModel<>();
        contactList.setModel(listModel);
        loadContacts();
        refreshContactList();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editContact();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });
    }

    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty()) {
            contacts.add(new Contact(name, phone, email));
            saveContacts();
            refreshContactList();
            showToast("Contact added successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editContact() {
        int index = contactList.getSelectedIndex();
        if (index >= 0) {
            Contact contact = contacts.get(index);
            contact.setName(nameField.getText());
            contact.setPhone(phoneField.getText());
            contact.setEmail(emailField.getText());
            saveContacts();
            refreshContactList();
            showToast("Contact edited successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to edit", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteContact() {
        int index = contactList.getSelectedIndex();
        if (index >= 0) {
            contacts.remove(index);
            saveContacts();
            refreshContactList();
            showToast("Contact deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Please select a contact to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshContactList() {
        listModel.clear();
        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            listModel.addElement((i + 1) + ". " + contact.toString());
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    contacts.add(new Contact(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContacts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("contacts.txt"))) {
            for (Contact contact : contacts) {
                writer.println("Name: "+contact.getName() + ", Phone: " + contact.getPhone() + ", Email: " + contact.getEmail());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showToast(String message) {
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new JLabel(message, SwingConstants.CENTER), BorderLayout.CENTER);
        dialog.setSize(300, 50);
        dialog.setLocationRelativeTo(mainPanel);

        Timer timer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Contact Manager");
        frame.setContentPane(new ContactManagerForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

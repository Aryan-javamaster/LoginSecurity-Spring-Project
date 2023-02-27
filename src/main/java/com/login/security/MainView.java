package com.login.security;

import java.sql.SQLException;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;

@Route("")

public class MainView extends VerticalLayout {
    public MainView() throws SQLException {
        // create name textfield
        database.delete();
        ArrayList<String> usernames = new ArrayList<String>();
        TextField name = new TextField("Email(no duplicate entries)");
        PasswordField password = new PasswordField("Password");
        Button signup = new Button("SIGN UP");
        Button login = new Button("LOGIN");

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(name, password, signup, login);
        
        add(mainLayout);

        signup.addClickListener(clickEvent -> {
            try {
                database.commit(name.getValue(), password.getValue());
                usernames.add(name.getValue());
                Notification notification = Notification
                                .show("Account created");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.MIDDLE);
            } catch (SQLException e) {

                e.printStackTrace();
            }
        });

        login.addClickListener(clickEvent -> {
            boolean temp = false;
            for (int i = 0; i < usernames.size(); i++) {
                if (usernames.get(i).equals(name.getValue())) {
                    if (passwordcheck(name.getValue(), password.getValue())) {
                        Notification notification = Notification
                                .show("Correct password now you are loged in ");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.MIDDLE);
                        temp = true;
                    }
                    else{
                        Notification notification = Notification
                                .show("Check your password");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR); 
                        notification.setPosition(Notification.Position.MIDDLE);
                        temp = true;
                    }
                }
            }
            if(!temp){
                Notification notification = Notification
                                .show("Please sign up first");
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR); 
                        notification.setPosition(Notification.Position.MIDDLE);
            }

        });

    }

    public static boolean passwordcheck(String name, String password) {

        try {
            if (database.get(name).equals(password)) {
                return true;
            }
        } catch (SQLException e) {
            return false;

        }
        return false;
    }

}

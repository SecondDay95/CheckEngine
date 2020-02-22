package com.example.checkengine2.main;

import com.example.checkengine2.R;

public class HomeList {

    private String name;
    private int imageResourceID;

    private HomeList (String name, int imageResourceID) {
        this.name = name;
        this.imageResourceID = imageResourceID;
    }

    public static final HomeList[] listnames = {
            new HomeList("Sprawdź informacje dotyczące temperatury, jak jej:" +
                    "rzeczywisty przebieg, predykcję zmian oraz uśrednione wartości.", R.drawable.temperatura),
            new HomeList("Sprawdź informacje dotyczące prądu, jak jego:" +
                    "rzeczywisty przebieg, predykcję zmian oraz uśrednione wartości.", R.drawable.prad),
            new HomeList("Przejdź do ustawień konta.", R.drawable.uzytkownik),
            new HomeList("Rozpocznij proces wylogowania", R.drawable.logout)
    };

    public String getName() {
        return name;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }
}

package com.example.checkengine2.main;

//Klasa ta umożliwia powrót po wylaczeniu aplikacji bez logowania powrót do HomeActivity, a nie do MainActivity.
//Jeżeli uzytkownik jest zalogowany to uruchamiana jest aktywnosc HomeActivity, a jesli nie to MainActivity.
//Klasa Home rozszerza klase głównej aplikacji (Application), wiec jest to klasa, która monitoruje stan
//aplikacji i jest wykonywana na samym początku tworzenia aplikacji.
//Aby początkowo podjac probe uruchomienia klasy HomeClass a dopiero pozniej MainActivity, trzeba dodac w pliku
//AndroidMainifest.xml nastepujaca linie kodu na poczatku pliku:
//

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Inicjalizacja instancji autoryzacji uzytkownika:
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //Próba pobrania obecnie zalogowanego uzytkownika:
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //Jeżeli próba pobrania zalogowanego uzytkownika sie powidla (firebaseUser != null):
        if (firebaseUser != null)
        {
            //Uruchomienie aktywnosci HomeActivity zamiast MainActivity:
            Intent intent = new Intent(HomeClass.this, HomeActivity.class);
            startActivity(intent);
        }

        //Jezeli próba pobrania zalogowanego uzytkownika sie nie powiedzie to domyslnie zostanie uruchomiona
        //aktywnosc MainActivity. Nieudana próba oznacza ze uzytkownik ten nie jest zalogowany.
    }
}

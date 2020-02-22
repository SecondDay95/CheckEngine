package com.example.checkengine2.main;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.checkengine2.R;

public class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {

    private Listener listener;

    private String captions[];
    private int imageIds[];

    //Interfejs zawierający metodę obsługującą kliknięcia widoku CardView:
    public interface Listener {
        public void onClick (int position);
    }

    // implementacja wzorca ViewHolder
    // każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    // dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    //ViewHolder Przechowuje referencję do związanych instancji widoku w postaci
    // obiektu typu ViewHolder i w razie potrzeby udostępnia je do ponownego użycia.
    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Przypisanie do zmiennej typu cardView referencji przetrzymywanej w obiekcie viewHolder
            cardView = (CardView) itemView;
        }
    }

    //Metoda ustawiająca obiekt nasłuchujący:
    public void setListener(Listener listener) {
        this.listener = listener;
    }


    //Tworznie obiektu ViewHolder, który przechowuje referencje do poszczególnych obiektów CardView.
    //W efekcie używanych jest jedynie tyle obiektów CardView ile mieści się na ekreania.
    @NonNull
    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Okreslenie jaki uklad ma byc przechowywany w obiektach ViewHolder:
        CardView cv = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.card_captioned_image, viewGroup, false );
        return new ViewHolder(cv);
    }

    //Konstruktor adaptera CaptionedImagesAdapter.
    //W naszym przypadku obiekt ViewHolder zawiera widoki CardView, w których
    //musimy określić wyświetlane zdjęcie i jego podpis. W tym celu dodamy do adaptera konstruktor, dzięki któremu widok
    //RecyclerView będzie mógł przekazywać do niego dane. Następnie użyjemy metody onBindViewHolder()
    //do powiązania danych z widokiem CardView
    public CaptionedImagesAdapter(String captions[], int imageIds[]) {
        this.captions = captions;
        this.imageIds = imageIds;
    }

    //Metoda umożliwiająca wypełnienie obiektów CardView danymi.
    //Metoda onBindViewHolder() jest wywoływana za każdym razem, gdy widok RecyclerView musi wyświetlić dane w obiekcie ViewHolder,
    //który przechowuje referencje do obiektu CardView zawierającego obrazek oraz pole tekstowe.
    //Dzieje sie to np. podczas przewijania, lub tworzenie RecyclerView.  Metoda ta ma dwa parametry: obiekt ViewHolder,
    //z którym należy powiązać dane, i indeks tych danych w zbiorze danych adaptera.
    @Override
    public void onBindViewHolder(@NonNull CaptionedImagesAdapter.ViewHolder viewHolder, final int position) {

        //Pobranie referencji cardView z obiektu viewHolder
        CardView cardView = viewHolder.cardView;
        //Pobranie referencji do obrazka w cardView:
        ImageView imageView = (ImageView) cardView.findViewById(R.id.info_image);
        //Pobranie z zasobób obrazka o pozycji wyświetlanej obecnie w RecyclerView
        Drawable drawable = cardView.getResources().getDrawable(imageIds[position]);
        //Określenie że pobrany obrazek ma być wyświetlony w widoku ImageView
        imageView.setImageDrawable(drawable);
        //Określenie napisu gdy obrazek bedzie niedostepny
        imageView.setContentDescription(captions[position]);
        //Pobranie referencji do TextView w cardView:
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        //Przypisanie tekstu do widoku tekstowego z listy z pozycji odpowiadającej pozycji RecyclerView:
        textView.setText(captions[position]);

        //Przypisanie do widoku CardView obiektu nasłuchującego:
        ((ViewHolder)viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    //Wywołanie metody z interfejsu:
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return captions.length;
    }
}

package trainedge.zapdiet.fragment;

import com.google.firebase.database.DataSnapshot;

class InfoModel {
    String item;
    String carb;
    String cal;
    String fat;
    String protein;

    public InfoModel(){

    }
    public InfoModel(DataSnapshot dataSnapshot){
        this.item = dataSnapshot.child("Item").getValue(String.class);
        this.carb = dataSnapshot.child("Carbohydrate").getValue(String.class);
        this.cal = dataSnapshot.child("Kcal").getValue(String.class);
        this.fat = dataSnapshot.child("Fat").getValue(String.class);
        this.protein = dataSnapshot.child("Proteins").getValue(String.class);
    }

}

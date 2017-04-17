package trainedge.zapdiet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import trainedge.zapdiet.R;

public class userinput extends Fragment {

    String[] spinlist =new String[]{"Kg","Pound"};

    public userinput() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinput, container, false);
        Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, spinlist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);
        return view;
    }

}

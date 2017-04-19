package trainedge.zapdiet.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import trainedge.zapdiet.R;
import trainedge.zapdiet.acc_details;

public class userinput extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private static final String TAG = "userinput";
    String[] weiglist =new String[]{"Kg","Pound"};
    String[] genderlist =new String[] {"Male","Female"};
    String[] heiglist =new String[] {"cm", "ft & in"};
    private String gender;
    private String heigstr;
    private String weigstr;
    private EditText heig1;
    private EditText heig2;
    private EditText weig1;
    private EditText nam;
    private EditText allerg;
    private EditText diseas;
    private EditText age1;
    private double height1;
    private double weightfloat;
    private double bmi1;
    private TextView text;

    public userinput() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userinput, container, false);
        Spinner weig = (Spinner) view.findViewById(R.id.weight_in);
        Spinner gend = (Spinner) view.findViewById(R.id.gender);
        Spinner heig = (Spinner) view.findViewById(R.id.height_in);
        heig2 = (EditText) view.findViewById(R.id.height2);
        heig1 = (EditText) view.findViewById(R.id.height1);
        weig1 = (EditText) view.findViewById(R.id.weight);
        nam = (EditText) view.findViewById(R.id.namep);
        allerg = (EditText) view.findViewById(R.id.allergy);
        diseas = (EditText) view.findViewById(R.id.disease);
        age1 = (EditText) view.findViewById(R.id.age);
        text = (TextView) view.findViewById(R.id.bmi);
        Button submit = (Button) view.findViewById(R.id.submit_data);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, weiglist);
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genderlist);
        ArrayAdapter<String> arrayadapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, heiglist);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayadapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weig.setAdapter(arrayAdapter);
        gend.setAdapter(arrayadapter);
        heig.setAdapter(arrayadapter1);
        weig.setOnItemSelectedListener(this);
        gend.setOnItemSelectedListener(this);
        heig.setOnItemSelectedListener(this);
        nam.addTextChangedListener(GenericTextWatcher);
        allerg.addTextChangedListener(GenericTextWatcher);
        age1.addTextChangedListener(GenericTextWatcher);
        heig1.addTextChangedListener(GenericTextWatcher);
        heig2.addTextChangedListener(GenericTextWatcher);
        weig1.addTextChangedListener(GenericTextWatcher);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.weight_in:
                if(position == 0){
                    weigstr = "Kg";
                    weig1.setHint("Kg");
                }
                else if(position == 1){
                    weigstr = "Pound";
                    weig1.setHint("Pound");
                }
                break;
            case R.id.gender:
                if(position == 0){
                    gender="Male";
                }
                else if(position == 1){
                    gender="Female";
                }
                break;
            case R.id.height_in:
                if(position == 0){
                    heig2.setVisibility(View.GONE);
                    heigstr = "cm";
                    heig1.setHint("cm");
                }
                else if(position == 1) {
                    heig2.setVisibility(View.VISIBLE);
                    heigstr = "ft & in";
                    heig1.setHint("ft");
                    heig2.setHint("in");
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch(parent.getId()) {
            case R.id.weight_in:
                weigstr = "Kg";
                break;
            case R.id.gender:
                gender = "Male";
                break;
            case R.id.height_in:
                heigstr = "cm";
                break;
        }
    }

    private TextWatcher GenericTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            if (nam.getText().hashCode() == s.hashCode())
            {
                if(s.toString().length()<8){
                    nam.setError("Atleast 8 characters");
                }
            }
            else if (age1.getText().hashCode() == s.hashCode())
            {
                if(Integer.parseInt(s.toString())<12 || Integer.parseInt(s.toString())>100){
                    age1.setError("Age between 12-100");
                }
            }
            else if(allerg.getText().hashCode() == s.hashCode()){
                if(s.toString().length()<3){
                    allerg.setError("seprate multiple using \",\" ");
                }
            }
            else if(heig1.getText().hashCode() == s.hashCode()){
                calculate();
                bmi1 = bmi(weightfloat,height1);
                text.setText(String.valueOf(bmi1));
            }
            else if(heig2.getText().hashCode() == s.hashCode()){
                calculate();
                bmi1 = bmi(weightfloat,height1);
                text.setText(String.valueOf(bmi1));
            }
            else if(weig1.getText().hashCode() == s.hashCode()){
                calculate();
                bmi1 = bmi(weightfloat,height1);
                text.setText(String.valueOf(bmi1));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {}

    };

    @Override
    public void onClick(View v) {
        String namestr = nam.getText().toString();
        String allergystr = allerg.getText().toString();
        String diseasestr = diseas.getText().toString();
        String heigstr1 = heig1.getText().toString();
        String agestr = age1.getText().toString();
        String weigstr1 = weig1.getText().toString();
        if(namestr.isEmpty() || namestr.length()<8){
            nam.setError("Recquired");
            return;
        }
        else if(agestr.isEmpty()){
            age1.setError("Recquired");
            return;
        }
        else if(weigstr1.isEmpty()){
            weig1.setError("Recquired");
            return;
        }
        else if(heigstr1.isEmpty()){
            heig1.setError("Recquired");
            return;
        }
        int ageint = Integer.parseInt(agestr);

        calculate();

        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(namestr)
                .build();
        user1.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String userId = mDatabase.push().getKey();
        User user = new User(gender,ageint,height1,weightfloat,bmi1,allergystr,diseasestr);
        mDatabase.child(userId).setValue(user);

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void calculate(){
        String heigstr1 = heig1.getText().toString();
        String weigstr1 = weig1.getText().toString();
        if(heigstr1.isEmpty()){
            height1 = 0;
        }
        else{
            height1 = Double.parseDouble(heigstr1);
        }
        if(weigstr1.isEmpty()){
            weightfloat = 0;
        }
        else{
            weightfloat = Double.parseDouble(weigstr1);
        }
        if(weigstr.contains("Pound")){
            weightfloat = weightfloat*0.453592; //converting pound to kg.
        }
        if(heigstr.contains("ft & in")){
            String heigstr2 = heig2.getText().toString();
            if(!heigstr2.isEmpty()){
                double height2 = Double.parseDouble(heigstr2);
                height1 = height1+height2*0.083333; //converting all height into feet.
            }
            height1 = height1*30.48; // converting feet to cm.
        }
        height1 = height1/100; //converting height into meter

    }

    private double bmi(double a,double b){
        double bmia;
        bmia = a/(b*b);
        bmia=(double)Math.round(bmia * 100d) / 100d;
        return bmia;
    }

    @IgnoreExtraProperties
    public class User {

        public String Gender;
        public int Age;
        public double Height;
        public double Weight;
        public double BMI;
        public String Allergy;
        public String Disease;

        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)
        public User() {
        }

        public User( String gen, int ag, double hei, double wei,double bm, String all, String dise) {
            this.Gender = gen;
            this.Age = ag;
            this.Height = hei;
            this.Weight = wei;
            this.BMI = bm;
            this.Allergy = all;
            this.Disease = dise;
        }
    }
}

package bimerso.cliente.firebasemvvm.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import bimerso.cliente.firebasemvvm.R;
import bimerso.cliente.firebasemvvm.models.Cliente_Entidad;
import bimerso.cliente.firebasemvvm.viewmodel.AutenticacionViewModel;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private EditText etCelular;
    private CountryCodePicker mCountryCode;
    private AutenticacionViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AutenticacionViewModel.class);
        observerUser();
        setContentView(R.layout.activity_login);
        castView();
        btnLogin.setOnClickListener(v -> {
            if(validar()){
                String number =mCountryCode.getSelectedCountryCodeWithPlus() +  etCelular.getText().toString().trim();
                Intent i = new Intent(Login.this,Codigo.class);
                i.putExtra("number",number);
                startActivity(i);
            }
        });
    }
    private void observerUser(){
        viewModel.getUserData().observe(this, firebaseUser -> {
            if (firebaseUser != null && firebaseUser.getUid()!=null){
                observerVadidateDataBase();
                viewModel.validateUser();
            }
        });
    }
    private void observerVadidateDataBase() {
        viewModel.getCliente().observe(this, entidad -> {
            if(entidad!=null){
                Intent i = new Intent(Login.this,Principal.class);
                startActivity(i);
                finish();
            }
            else {
                Intent i = new Intent(Login.this,CrearCliente.class);
                startActivity(i);
                finish();
            }
        });
    }
    private boolean validar() {
        if(TextUtils.isEmpty(etCelular.getText().toString().trim())){
            Toast.makeText(this, "Ingrese su número de celular", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void castView(){
        btnLogin = findViewById(R.id.btnEnviarCodigo);
        etCelular = findViewById(R.id.editTextPhone);
        mCountryCode = findViewById(R.id.ccp);

    }
}
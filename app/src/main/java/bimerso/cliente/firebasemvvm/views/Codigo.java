package bimerso.cliente.firebasemvvm.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import bimerso.cliente.firebasemvvm.R;
import bimerso.cliente.firebasemvvm.viewmodel.AutenticacionViewModel;

public class Codigo extends AppCompatActivity {
    private AutenticacionViewModel viewModel;
    private String number="";
    private Button btnIngresar;
    private EditText etCodigo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codigo);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AutenticacionViewModel.class);
        castView();
        btnIngresar.setOnClickListener(v -> {
            if (validar()) {
                viewModel.validateCode(etCodigo.getText().toString().trim());
            }
        });
    }
    private boolean validar() {
        if(TextUtils.isEmpty(etCodigo.getText().toString().trim())){
            Toast.makeText(this, "Ingrese el código de verificación", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void castView(){
        etCodigo = findViewById(R.id.editTextCodeVerification);
        btnIngresar = findViewById(R.id.btnCodeVerification);
        if(getIntent()!=null && getIntent().getExtras()!=null){
            number = getIntent().getExtras().getString("number");
            viewModel.loginPhone(number,Codigo.this);
            observerLoginPhone();
            observerValidateCode();
        }
    }

    private void observerValidateCode() {
        viewModel.getAcceso().observe(this, aBoolean -> {
            if(aBoolean){
                Intent i = new Intent(Codigo.this,Principal.class);
                startActivity(i);
                finish();
            }
            else {
                Intent i = new Intent(Codigo.this,CrearCliente.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void observerLoginPhone() {
        viewModel.getCodigo().observe(this, s -> etCodigo.setText(s));
    }
}
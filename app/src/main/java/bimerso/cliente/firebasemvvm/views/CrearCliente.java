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

public class CrearCliente extends AppCompatActivity {
    private Button btnRegistrar;
    private EditText etNombre,etApellidos,etEdad,etFechadenacimiento;
    private AutenticacionViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);
        viewModel = new ViewModelProvider(this , ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(AutenticacionViewModel.class);
        castView();
        btnRegistrar.setOnClickListener(v -> {
            if(validar()){
                Cliente_Entidad entidad = new Cliente_Entidad();
                entidad.setNombre(etNombre.getText().toString().trim());
                entidad.setApellidos(etApellidos.getText().toString().trim());
                entidad.setEdad(Integer.parseInt(etEdad.getText().toString().trim()));
                entidad.setFechaNacimiento(etFechadenacimiento.getText().toString().trim());
                observerRegisterUser();
                viewModel.register(entidad);
            }
        });
    }
    private void observerRegisterUser() {
        viewModel.getUserRegistered().observe(this, aBoolean -> {
            if(aBoolean){
                Intent i = new Intent(CrearCliente.this,Principal.class);
                startActivity(i);
                finish();
            }
            else
                Toast.makeText(CrearCliente.this, "Hubo un error.", Toast.LENGTH_SHORT).show();
        });
    }

    private void castView() {
        btnRegistrar = findViewById(R.id.btnRegistrar);
        etNombre = findViewById(R.id.etnombre_crear);
        etApellidos = findViewById(R.id.etapellido_crear);
        etEdad = findViewById(R.id.etedad_crear);
        etFechadenacimiento = findViewById(R.id.etfecha_crear);
    }
    private boolean validar(){
        if(TextUtils.isEmpty(etNombre.getText().toString().trim())){
            Toast.makeText(this, "Ingrese nombre.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(etApellidos.getText().toString().trim())){
            Toast.makeText(this, "Ingrese sus apellidos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(etEdad.getText().toString().trim())){
            Toast.makeText(this, "Ingrese su edad.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(etFechadenacimiento.getText().toString().trim())){
            Toast.makeText(this, "Ingrese su fecha de nacimiento.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
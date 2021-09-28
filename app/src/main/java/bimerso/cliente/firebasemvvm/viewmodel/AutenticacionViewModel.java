package bimerso.cliente.firebasemvvm.viewmodel;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import bimerso.cliente.firebasemvvm.models.Cliente_Entidad;
import bimerso.cliente.firebasemvvm.repository.AutenticacionRepository;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class AutenticacionViewModel extends AndroidViewModel {

    private AutenticacionRepository repository;
    private MutableLiveData<FirebaseUser> userData;
    private MutableLiveData<Boolean> acceso;
    private MutableLiveData<Boolean> userRegistered;
    private MutableLiveData<String> codigo;
    private MutableLiveData<Cliente_Entidad> cliente;
    public MutableLiveData<FirebaseUser> getUserData() {
        return userData;
    }

    public MutableLiveData<Cliente_Entidad> getCliente() {
        return cliente;
    }

    public MutableLiveData<Boolean> getUserRegistered() {
        return userRegistered;
    }

    public MutableLiveData<Boolean> getAcceso() {
        return acceso;
    }

    public MutableLiveData<String> getCodigo() {
        return codigo;
    }

    public AutenticacionViewModel(@NonNull  Application application) {
        super(application);
        repository = new AutenticacionRepository(application);
        userData = repository.getFirebaseUserMutableLiveData();
        acceso = repository.getAcceso();
        codigo = repository.getCodigo();
        userRegistered = repository.getUserRegistered();
        cliente = repository.getCliente();
    }

    public void register(Cliente_Entidad entidad){
        repository.register(entidad);
    }
    public void loginPhone(String number , Context context){
        repository.login(number, context);
    }
    public void validateCode(String code){
        repository.validateCode(code);
    }
    public void validateUser(){
        repository.validateUser();
    }
    public void signOut(){
        repository.signOut();
    }
}

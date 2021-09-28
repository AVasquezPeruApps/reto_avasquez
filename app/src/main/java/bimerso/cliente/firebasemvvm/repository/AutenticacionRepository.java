package bimerso.cliente.firebasemvvm.repository;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import bimerso.cliente.firebasemvvm.models.Cliente_Entidad;

public class AutenticacionRepository {
    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> userLoggedMutableLiveData;
    private FirebaseAuth auth;
    private MutableLiveData<String> codigo;
    private MutableLiveData<Boolean> acceso;
    private MutableLiveData<Cliente_Entidad> cliente;
    private MutableLiveData<Boolean> userRegistered;
    private String mVerificationId;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Cliente_Entidad> getCliente() {
        return cliente;
    }

    public MutableLiveData<Boolean> getUserRegistered() {
        return userRegistered;
    }

    public void setCliente(MutableLiveData<Cliente_Entidad> cliente) {
        this.cliente = cliente;
    }

    public Application getApplication() {
        return application;
    }

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return userLoggedMutableLiveData;
    }

    public MutableLiveData<String> getCodigo() {
        return codigo;
    }

    public MutableLiveData<Boolean> getAcceso() {
        return acceso;
    }

    public AutenticacionRepository(Application application){
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedMutableLiveData = new MutableLiveData<>();
        codigo = new MutableLiveData<>();
        acceso = new MutableLiveData<>();
        cliente = new MutableLiveData<>();
        userRegistered = new MutableLiveData<>();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }
    }

    public void register(Cliente_Entidad entidad){
        ref.child("Clientes").child(auth.getCurrentUser().getUid()).setValue(entidad).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                userRegistered.postValue(true);
            }
            else
                userRegistered.postValue(false);
        });
    }
    public void validateUser(){
        ref.child("Clientes").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Cliente_Entidad entidad = dataSnapshot.getValue(Cliente_Entidad.class);
                    cliente.postValue(entidad);
                }
                else
                    cliente.postValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void login(String phoneNumber,Context context){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity((Activity)context)
                        .setCallbacks(callback)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    public void validateCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ref.child("Clientes").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            acceso.postValue(true);
                        }
                        else{
                            acceso.postValue(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        });
    }
    public void signOut(){
        auth.signOut();
        userLoggedMutableLiveData.postValue(true);
    }
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                codigo.postValue(code);
                validateCode(code);
            }

        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }
        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken);
            mVerificationId = verificationId;
        }
    };
}



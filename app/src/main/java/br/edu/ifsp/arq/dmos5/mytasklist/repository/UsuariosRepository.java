package br.edu.ifsp.arq.dmos5.mytasklist.repository;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import br.edu.ifsp.arq.dmos5.mytasklist.model.Usuario;
import br.edu.ifsp.arq.dmos5.mytasklist.viewmodel.UsuarioViewModel;

public class UsuariosRepository {

    private static final String BASE_URL = "https://identitytoolkit.googleapis.com/v1/";
    private static final String SIGNUP = "accounts:signUp";
    private static final String SIGNIN = "accounts:signInWithPassword";
    private static final String PASSWORD_RESET = "accounts:sendOobCode";
    private static final String KEY = "?key=AIzaSyAfUgVXTalklz83gYxnFtVtl-MCwvkd8j4";

    private FirebaseFirestore firestore;

    private RequestQueue queue;

    private SharedPreferences preference;

    public UsuariosRepository(Application application) {
        firestore = FirebaseFirestore.getInstance();
        queue = Volley.newRequestQueue(application);
        preference = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public LiveData<Usuario> login(String email, String senha){
        MutableLiveData<Usuario> liveData = new MutableLiveData<>();

        JSONObject parametros = new JSONObject();
        try {
            parametros.put("email", email);
            parametros.put("password", senha);
            parametros.put("returnSecureToken", true);
            parametros.put("Content-Type", "application/json; charset=utf-8");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + SIGNIN + KEY, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String localId = response.getString("localId");
                            String idToken = response.getString("idToken");

                            firestore.collection("usuario").document(localId).get().addOnSuccessListener(snapshot -> {

                                Log.d(this.toString(), snapshot.toString());

                                Usuario usuario = snapshot.toObject(Usuario.class);

                                usuario.setId(localId);
                                usuario.setSenha(idToken);

                                liveData.setValue(usuario);

                                preference.edit().putString(UsuarioViewModel.USUARIO_ID, localId).apply();

                                firestore.collection("usuario").document(localId).set(usuario);
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                JSONObject obj = new JSONObject(res);
                                Log.d(this.toString(), obj.toString());
                            } catch ( UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            } catch (JSONException e2) {
                                e2.printStackTrace();
                            }
                        }
                        liveData.setValue(null);
                    }
                });

        queue.add(request);

        return liveData;
    }

    public LiveData<Usuario> load(String usuarioId){
        MutableLiveData<Usuario> liveData = new MutableLiveData<>();

        DocumentReference userRef = firestore.collection("usuario").document(usuarioId);

        userRef.get().addOnSuccessListener(snapshot -> {
            Usuario usuario = snapshot.toObject(Usuario.class);

            liveData.setValue(usuario);
        });

        return liveData;
    }

    public void createUsuario(Usuario usuario)  {
        JSONObject parametros = new JSONObject();
        try {
            parametros.put("email", usuario.getEmail());
            parametros.put("password", usuario.getSenha());
            parametros.put("returnSecureToken", true);
            parametros.put("Content-Type", "application/json; charset=utf-8");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + SIGNUP + KEY, parametros,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            usuario.setId(response.getString("localId"));
                            usuario.setSenha(response.getString("idToken"));

                            firestore.collection("usuario").document(usuario.getId()).set(usuario).addOnSuccessListener(unused -> {
                                Log.d(this.toString(), "Usuário " + usuario.getEmail() + "cadastrado com scuesso.");
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(this.toString(), "error");
                    }
                });

        queue.add(request);
    }

    public Boolean update(Usuario usuario){
        final Boolean[] atualizado = {false};

        DocumentReference usuarioRef = firestore.collection("usuario").document(usuario.getId());

        usuarioRef.set(usuario).addOnSuccessListener(unused -> {
            atualizado[0] = true;
        });

        return atualizado[0];
    }
}

package br.edu.ifsp.arq.dmos5.mytasklist;

import android.os.Bundle;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    ListPreference listPreference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
//        listPreference = findPreference("theme_chooser");
//        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//
//                int numCor = encontrarNomeCor(newValue.toString());
//                if (numCor!=-1){
//                    Intent intent = new Intent(getContext(), SettingsActivity.class);
//                    intent.putExtra("theme", numCor);
//                    startActivity(intent);
//                    return true;
//                }
//
//                Toast.makeText(getContext(), "Não tenha vergonha, escolha seu Tema Preferido!", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

//    private int encontrarNomeCor(String corHexEscolhida) {
//        switch (corHexEscolhida){
//            case "#FF00FB":
//                return 1;
//            case "#0091EA":
//                return 2;
//            case "#636161":
//                return 3;
//            case "#64DD17":
//                return 4;
//            case "#AA00FF":
//                return 5;
//            case "#FFD600":
//                return 6;
//            default:
//                return -1;
//        }
//    }
}

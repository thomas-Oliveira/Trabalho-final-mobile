package com.example.thomas.denuncie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class MenuDialogFragment extends DialogFragment {

    final CharSequence[] array = {"Vizualizar" , "Editar", "Remover"};

    MenuClickListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Opções").setItems(array, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                listener.onDenunciaItemClick(MenuDialogFragment.this.getArguments().getInt("id"), which);
            }


        });

        return builder.create();
    }

    public interface MenuClickListener {
        public void onDenunciaItemClick(int posicao, int item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (MenuClickListener) context;

    }

}
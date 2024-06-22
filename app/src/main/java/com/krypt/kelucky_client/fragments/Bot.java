package com.krypt.kelucky_client.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.krypt.kelucky_client.Countdown;
import com.krypt.kelucky_client.MainActivity;
import com.krypt.kelucky_client.R;

public class Bot  extends Fragment {
    EditText edt_select,amount,timeperround,amountperaound;
    TextView amn;
    Button toCont;
    String accnm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt_select=view.findViewById(R.id.selectgame);
        timeperround=view.findViewById(R.id.selectgamet);
        amountperaound=view.findViewById(R.id.selectgamett);
        amount=view.findViewById(R.id.selectgameamnt);
        amn=view.findViewById(R.id.accnm);
        toCont=view.findViewById(R.id.toCont);
        TextView marqueeText = view.findViewById(R.id.marqueeText);
        marqueeText.setSelected(true);
        edt_select.setFocusable(false);
        amount.setFocusable(false);
        timeperround.setFocusable(false);
        amountperaound.setFocusable(false);
        Intent intent = getActivity().getIntent();
        String data = intent.getStringExtra("key");
        amn.setText("Account  :"+data);
        edt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(v);
            }
        });
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecta(v);
            }
        });
        amountperaound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectamt(v);
            }
        });
        timeperround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectt(v);
            }
        });
        toCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tp=timeperround.getText().toString();
                String sn=amountperaound.getText().toString();
                String amou=amount.getText().toString();
                String smn=edt_select.getText().toString();
                if (tp.isEmpty()||sn.isEmpty()||amou.isEmpty()||smn.isEmpty()){
                    Toast.makeText(getContext(),"Select all fields",Toast.LENGTH_LONG).show();
                    return;
                }
                startActivity(new Intent(getContext(), Countdown.class));

            }
        });

    }
    public void select(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select");
        final String[] array = {"mines", "crash", "fiery bot ","ring", "Driver", "ninja crash","roulete","keno"};

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                edt_select.setText(array[i]);
                Toast.makeText(getContext(), array[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

                builder.setNegativeButton("Close", null);


            }
        });
        builder.show();

    }
    public void selecta(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select");
        final String[] array = {"500", "1000", "2000 ","5000"};

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                amount.setText(array[i]);
                Toast.makeText(getContext(), array[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

                builder.setNegativeButton("Close", null);


            }
        });
        builder.show();

    }
    public void selectt(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select");
        final String[] array = {"15", "20", "25 "};

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timeperround.setText(array[i]);
                Toast.makeText(getContext(), array[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

                builder.setNegativeButton("Close", null);


            }
        });
        builder.show();

    }
    public void selectamt(View v) {
        final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Select");
        final String[] array = {"50", "100", "200","300"};

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                amountperaound.setText(array[i]);
                Toast.makeText(getContext(), array[i], Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();

                builder.setNegativeButton("Close", null);


            }
        });
        builder.show();

    }

}

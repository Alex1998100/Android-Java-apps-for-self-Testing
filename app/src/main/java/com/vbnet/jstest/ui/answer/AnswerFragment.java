package com.vbnet.jstest.ui.answer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vbnet.jstest.MainActivity;
import com.vbnet.jstest.databinding.FragmentAnswerBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AnswerFragment extends Fragment {

    private FragmentAnswerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //AnswerViewModel answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);

        binding = FragmentAnswerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String answerFile = ((MainActivity)this.getContext()).currentQuestion+".txt";

        final TextView textView = binding.textAnswer;
        textView.setText(readTextFile(answerFile));
        //answerViewModel".getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    public  String readTextFile(String fileName) {
        BufferedReader reader;
        StringBuilder sb = new StringBuilder();
       try{
            final InputStream file = this.getContext().getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(file));
            String line = reader.readLine();
            while(line != null){
                line = reader.readLine();
                sb.append(line);
                sb.append("\n");
            }

        } catch(IOException ioe){
            ioe.printStackTrace();
        }
        return  sb.toString().replace("\nnull\n", "\n");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
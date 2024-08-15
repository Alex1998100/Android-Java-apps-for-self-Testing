package com.vbnet.jstest;

import static android.util.Log.println;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.vbnet.jstest.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //public String imagePath = "/storage/emulated/0/Download/ES#130.png";
    public String currentQuestion ="";
    public void getNext() {
        Random rand = new Random();
        currentQuestion = fileList.get(rand.nextInt(fileList.size()));
    }
    private Integer index =0;


    private String extension(String fullPath) {
        int dot = fullPath.lastIndexOf('.');
        return fullPath.substring(dot + 1);
    }

    private String filename(String fullPath) {
        int dot = fullPath.lastIndexOf('.');
        int sep = fullPath.lastIndexOf("/");
        return fullPath.substring(sep + 1, dot);
    }

    private List<String> fileList;
    private List<String> buildQuestionList() throws IOException {
        List<String> fileList = new ArrayList<String>();
        /*File f = new File("/storage/emulated/0/Download/");
        File[] fullFileList = f.listFiles();*/
        String[] fullFileList = getAssets().list("");
        for (int i = 0; i < fullFileList.length; i++) {
            if(extension(String.valueOf(fullFileList[i])).equals("png") &&
                    filename(String.valueOf(fullFileList[i])).substring(2,3).equals("#") ){
                for (int j = 0; j < fullFileList.length; j++) {
                    if(extension(String.valueOf(fullFileList[j])).equals("txt") &&
                            filename(String.valueOf(fullFileList[i])).equals(filename(String.valueOf(fullFileList[j]))) ){
                        println(Log.VERBOSE,"1", String.valueOf(fullFileList[i]));
                        fileList.add(filename(String.valueOf(fullFileList[i])));
                    }
                }
            }
        }
        return fileList;
    }
    public void nextQuestion(View view)
    {
        getNext();
        recreate();
    }

    public void applicationStart(){
        try {
            fileList = buildQuestionList();
            getNext();
        } catch (IOException e) {
            println(Log.ERROR,"1", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applicationStart();
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_question, R.id.navigation_answer, R.id.navigation_notes)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}
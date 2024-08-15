package com.vbnet.jstest.ui.question;

import static android.util.Log.println;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.vbnet.jstest.MainActivity;
import com.vbnet.jstest.R;
import com.vbnet.jstest.databinding.FragmentQuestionBinding;

public class QuestionFragment extends Fragment {

    private FragmentQuestionBinding binding;

    // inflater - PhoneLayoutInflater@21173
    // container - androidx.fragment.app.FragmentContainerView#7f080131 app:id/nav_host_fragment_activity_main}
    // savedInstanceState - null
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)  {

        binding = FragmentQuestionBinding.inflate(inflater, container, false);

        // creating an image file
        println(Log.VERBOSE,"2", ((MainActivity)this.getContext()).currentQuestion);
        String imgFile = ((MainActivity)this.getContext()).currentQuestion+".png";

        //  initializing variables with ref to ImageView - not working !
        // ImageView imageViewRef = (ImageView) container.findViewById(R.id.imageView);

        /*
        File imgFile = new File(((MainActivity)this.getContext()).imagePath);
        if (imgFile.exists()) {
            Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            binding.imageView.setImageBitmap(imgBitmap);
        }
        */
        Bitmap imgBitmap = getBitmapFromAsset(this.getContext(),imgFile);
        binding.imageView.setImageBitmap(imgBitmap);


        View root = binding.getRoot();
        return root;
    }

    public static Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(strName);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }
        return bitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.vbnet.jstest.ui.notes;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.vbnet.jstest.MainActivity;
import com.vbnet.jstest.databinding.FragmentNotesBinding;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class NotesFragment extends Fragment {

    private FragmentNotesBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonymousAuth";
    String answerFile;
    EditText editText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //NotesViewModel notesViewModel =  new ViewModelProvider(this).get(NotesViewModel.class);

        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        answerFile = ((MainActivity) this.getContext()).currentQuestion;

        final TextView textView = binding.title;
        textView.setText(answerFile);
        editText = binding.notes;


        mDatabase = FirebaseDatabase.getInstance().getReference("notes");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        binding.submitNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

        return root;
    }

    private void submitPost() {
        String REQUIRED = "Required";
        final String title = binding.title.getText().toString();
        final String body = binding.notes.getText().toString();
        // Body is required
        if (TextUtils.isEmpty(body)) {
            binding.notes.setError(REQUIRED);
            return;
        }
        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(getContext(), "Posting...", Toast.LENGTH_SHORT).show();

        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                }
            }
        });

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String key = mDatabase.push().getKey();
                    Map<String, Object> newNotes = new HashMap<>();
                    newNotes.put( key, new Object() {
                        public final String Q = title;
                        public final String T = body;
                        public final String D = String.valueOf(ZonedDateTime.now());
                    });
                    Task<Void> voidTask = mDatabase.updateChildren(newNotes);
                    setEditingEnabled(true);
                    editText.getText().clear();
                }
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                //Toast.makeText(getContext(), databaseError.getCode(), Toast.LENGTH_SHORT).show();
                setEditingEnabled(true);
            };
        });
    }

    ;


    private void setEditingEnabled(boolean enabled) {
        binding.title.setEnabled(enabled);
        binding.notes.setEnabled(enabled);
        if (enabled) {
            binding.submitNotes.show();
        } else {
            binding.submitNotes.hide();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
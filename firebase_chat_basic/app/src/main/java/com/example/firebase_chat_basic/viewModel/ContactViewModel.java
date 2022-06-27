package com.example.firebase_chat_basic.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebase_chat_basic.Interface.BaseInterface;
import com.example.firebase_chat_basic.Interface.FirebaseInterface;
import com.example.firebase_chat_basic.adapters.ContactRecyclerAdapter;
import com.example.firebase_chat_basic.model.ContactModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;


public class ContactViewModel extends AndroidViewModel implements FirebaseInterface {
    private static final String realTimeDataBaseUserUrl = "https://fir-chat-basic-dfd08-default-rtdb.firebaseio.com/";
    private final DatabaseReference databaseReference;
    private ArrayList<ContactModel> contactModelList;
    private ContactRecyclerAdapter contactRecyclerAdapter;
    private String firebase_my_key;
    private String firebase_my_phone_number;
    private String contact_profile_image;
    private String contact_name;
    private String contact_state_message;
    private String contact_phone_number;
    private String contact_online_state;

    // constructor init
    public ContactViewModel(Application application, String getCurrentMyUID) {
        super(application);
        if (getCurrentMyUID != null) {
            firebase_my_key = getCurrentMyUID;
        }
        Log.d("firebase_my_key", String.valueOf(firebase_my_key));
        contactModelList = new ArrayList<>();
        contactRecyclerAdapter = new ContactRecyclerAdapter(this);
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(realTimeDataBaseUserUrl);


        getUserDataBase();
    }


    @Override
    public void getUserDataBase() {
        FirebaseInterface.super.getUserDataBase();
        // user의 정보를 딱 한번만 가지고 와서 리스트에 넣어준다.
        // 구지 수신을 대기할 필요가 없음.
        // @TODO Online 상태랑 상태메세지 users에 넣어야됨.
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("ContactModel Create", "");
                final String getUserKey = snapshot.getKey();
                Log.d("getUserKey", getUserKey);
                Log.d("myKey", String.valueOf(firebase_my_key));

                assert getUserKey != null;
                if (!getUserKey.equals(firebase_my_key)) {
                    contact_profile_image = snapshot.child("profileImage").getValue(String.class);
                    contact_name = snapshot.child("name").getValue(String.class);
                    contact_phone_number = snapshot.child("phoneNumber").getValue(String.class);
                    contactModelList.add(new ContactModel(contact_profile_image, contact_name, "", contact_phone_number, ""));
                    contactRecyclerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public String getUserName(int pos) {
        return contactModelList.get(pos).getContact_name();
    }

    @Override
    public String getPhoneNumber(int pos) {
        return contactModelList.get(pos).getContact_phone_number();
    }

    @Override
    public String getProfileImage(int pos) {
        return contactModelList.get(pos).getContact_profile_image();
    }

    public ContactRecyclerAdapter getContactRecyclerAdapter() {
        return contactRecyclerAdapter;
    }

    public ArrayList<ContactModel> getContactModelList() {
        return contactModelList;
    }


}

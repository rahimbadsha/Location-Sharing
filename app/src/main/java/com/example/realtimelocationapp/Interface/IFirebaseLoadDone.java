package com.example.realtimelocationapp.Interface;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadUserNameDone(List<String> lstEmail);
    void onFirebaseLoadFailed(String message);
}

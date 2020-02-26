package com.example.realtimelocationapp.Remote;

import com.example.realtimelocationapp.Model.MyResponse;
import com.example.realtimelocationapp.Model.Request;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {

    @Headers({

            "Content-Type:application/json",
            "Authorization:key=AAAAC6PRMQ8:APA91bFSfMUIrtPj0Ox5EyTx-zrNseHRJvSsVNAa3PP02Sr5FsNgrASPAET8oImuibIzdZkdzCbN5APRuj8Xg-JMKVffs0ipBPYq4-fwCYYnDpkHV0srH1y7a1NpCVRvxwKc0hy9vZ_V"
    })
    @POST("fcm/send")
    Observable<MyResponse> sendFriendRequestToUser(@Body Request body);
}

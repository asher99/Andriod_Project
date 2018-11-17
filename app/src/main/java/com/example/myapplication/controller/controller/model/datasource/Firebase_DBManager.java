package com.example.myapplication.controller.controller.model.datasource;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.myapplication.controller.controller.model.entities.Ride;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Firebase_DBManager {

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    static DatabaseReference RidesRef;
    static List<Ride> RideList;

    static {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("Rides");
        RideList = new ArrayList<>();
    }


    public static void addRide(final Ride Ride, final Action<Long> action) {

        String key = Ride.getPhone().toString();
        RidesRef.child(key).setValue(Ride).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.onSuccess(Ride.getPhone());
                action.onProgress("upload Ride data", 100);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.onFailure(e);
                action.onProgress("error upload Ride data", 100);

            }
        });
    }

    public static void removeRide(long phone, final Action<Long> action) {

        final String key = ((Long) phone).toString();

        RidesRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Ride value = dataSnapshot.getValue(Ride.class);
                if (value == null)
                    action.onFailure(new Exception("Ride not find ..."));
                else {
                    RidesRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            action.onSuccess(value.getPhone());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            action.onFailure(e);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                action.onFailure(databaseError.toException());
            }
        });
    }

    public static void updateRide(final Ride toUpdate, final Action<Long> action) {
        //final String key = ((Long) toUpdate.getPhone()).toString();

        removeRide(toUpdate.getPhone(), new Action<Long>() {
            @Override
            public void onSuccess(Long obj) {
                addRide(toUpdate, action);
            }

            @Override
            public void onFailure(Exception exception) {
                action.onFailure(exception);
            }

            @Override
            public void onProgress(String status, double percent) {
                action.onProgress(status, percent);
            }
        });
    }

    private static ChildEventListener RideRefChildEventListener;

    public static void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {

            if (RideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify Ride list"));
                return;
            }
            RideList.clear();

            RideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride Ride = dataSnapshot.getValue(Ride.class);
                    String phone = dataSnapshot.getKey();
                    Ride.setPhone(Long.parseLong(phone));
                    RideList.add(Ride);


                    notifyDataChange.OnDataChanged(RideList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ride Ride = dataSnapshot.getValue(Ride.class);
                    Long phone = Long.parseLong(dataSnapshot.getKey());
                    Ride.setPhone(phone);


                    for (int i = 0; i < RideList.size(); i++) {
                        if (RideList.get(i).getPhone().equals(phone)) {
                            RideList.set(i, Ride);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(RideList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Ride Ride = dataSnapshot.getValue(Ride.class);
                    Long phone = Long.parseLong(dataSnapshot.getKey());
                    Ride.setPhone(phone);

                    for (int i = 0; i < RideList.size(); i++) {
                        if (RideList.get(i).getPhone() == phone) {
                            RideList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(RideList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };
            RidesRef.addChildEventListener(RideRefChildEventListener);
        }
    }

    public static void stopNotifyToRideList() {
        if (RideRefChildEventListener != null) {
            RidesRef.removeEventListener(RideRefChildEventListener);
            RideRefChildEventListener = null;
        }
    }

}

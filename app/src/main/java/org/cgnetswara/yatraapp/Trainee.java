package org.cgnetswara.yatraapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "trainee_table")
public class Trainee {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "trainee_number")
    private String traineeNumber;

    @ColumnInfo(name = "is_synced")
    private int isSynced;

    @ColumnInfo(name = "datetime")
    private String dateTime;

    public String getTraineeNumber() {
        return traineeNumber;
    }

    public void setTraineeNumber(String traineeNumber) {
        this.traineeNumber = traineeNumber;
    }

    public int getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(int isSynced) {
        this.isSynced = isSynced;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}

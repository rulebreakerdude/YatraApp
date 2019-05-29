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

    @ColumnInfo(name = "trainee_name")
    private String traineeName;

    @ColumnInfo(name = "trainer_name")
    private String trainerName;

    @ColumnInfo(name = "trainer_number")
    private String trainerNumber;

    @ColumnInfo(name = "is_synced")
    private int isSynced;

    @ColumnInfo(name = "is_answered")
    private int isAnswered;

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

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerNumber() {
        return trainerNumber;
    }

    public void setTrainerNumber(String trainerNumber) {
        this.trainerNumber = trainerNumber;
    }

    public int getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(int isAnswered) {
        this.isAnswered = isAnswered;
    }
}

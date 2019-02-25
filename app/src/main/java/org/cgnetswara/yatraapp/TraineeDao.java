package org.cgnetswara.yatraapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TraineeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trainee trainee);

    @Query("SELECT * from trainee_table ORDER BY datetime DESC")
    LiveData<List<Trainee>> getAllTrainees();

    @Query("SELECT * from trainee_table ORDER BY datetime DESC")
    List<Trainee> getAllTraineesAsList();

    @Query("UPDATE trainee_table SET is_synced = 1 WHERE (trainee_number = :traineeNumber)")
    void updateSyncStatus(String traineeNumber);

    @Query("DELETE FROM trainee_table")
    void deleteAll();

    @Query("DELETE FROM trainee_table WHERE (trainee_number = :traineeNumber)")
    void deleteAnEntry(String traineeNumber);

}

package org.cgnetswara.yatraapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Trainee.class}, version = 2)
public abstract class TraineeDatabase extends RoomDatabase {

    public abstract TraineeDao traineeDao();

    private static volatile TraineeDatabase INSTANCE;

    static TraineeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TraineeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TraineeDatabase.class, "trainee_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

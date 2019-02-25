package org.cgnetswara.yatraapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class TraineeViewModel extends AndroidViewModel {

    private TraineeRepository mRepository;

    private LiveData<List<Trainee>> mAllTrainees;

    public TraineeViewModel (Application application) {
        super(application);
        mRepository = new TraineeRepository(application);
        mAllTrainees = mRepository.getAllTrainees();
    }

    LiveData<List<Trainee>> getAllTrainees() { return mAllTrainees; }

    public void insert(Trainee trainee) { mRepository.insert(trainee); }
    public void deleteAll() { mRepository.deleteAll(); }
    public void updateSyncStatus(String receiver) { mRepository.updateSyncStatus(receiver); }
    public void deleteAnEntry(String traineeToBeDeleted) { mRepository.deleteAnEntry(traineeToBeDeleted); }
    public List<Trainee> getAllTraineesAsList(){ return mRepository.getAllTraineesAsList(); }
}
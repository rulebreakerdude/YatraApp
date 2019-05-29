package org.cgnetswara.yatraapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TraineeRepository {

    private TraineeDao mTraineeDao;
    private LiveData<List<Trainee>> mAllTrainees;
    private List<Trainee> mTraineeList;

    TraineeRepository(Application application) {
        TraineeDatabase db = TraineeDatabase.getDatabase(application);
        mTraineeDao = db.traineeDao();
        mAllTrainees = mTraineeDao.getAllTrainees();
    }

    LiveData<List<Trainee>> getAllTrainees() {
        return mAllTrainees;
    }

    List<Trainee> getAllTraineesAsList(){
        new getAllTraineesAsListAsyncTask(mTraineeDao).execute();
        return mTraineeList;
    }

    private class getAllTraineesAsListAsyncTask extends AsyncTask<Void, Void, Void>{

        private TraineeDao mAsyncTaskDao;

        getAllTraineesAsListAsyncTask(TraineeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mTraineeList=mAsyncTaskDao.getAllTraineesAsList();
            return null;
        }
    }

    public void updateSyncStatus(String receiver){
        new updateSyncStatusAsyncTask(mTraineeDao).execute(receiver);
    }

    private static class updateSyncStatusAsyncTask extends AsyncTask<String, Void, Void> {

        private TraineeDao mAsyncTaskDao;

        updateSyncStatusAsyncTask(TraineeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateSyncStatus(params[0]);
            return null;
        }
    }

    public void updateAnsweredStatus(String receiver){
        new updateAnsweredStatusAsyncTask(mTraineeDao).execute(receiver);
    }

    private static class updateAnsweredStatusAsyncTask extends AsyncTask<String, Void, Void> {

        private TraineeDao mAsyncTaskDao;

        updateAnsweredStatusAsyncTask(TraineeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.updateAnsweredStatus(params[0]);
            return null;
        }
    }


    public void deleteAnEntry(String traineeToBeDeleted){
        new deleteAnEntryAsyncTask(mTraineeDao).execute(traineeToBeDeleted);
    }

    private static class deleteAnEntryAsyncTask extends AsyncTask<String, Void, Void> {

        private TraineeDao mAsyncTaskDao;

        deleteAnEntryAsyncTask(TraineeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteAnEntry(params[0]);
            return null;
        }
    }


    public void insert (Trainee trainee) {
        new insertAsyncTask(mTraineeDao).execute(trainee);
    }

    private static class insertAsyncTask extends AsyncTask<Trainee, Void, Void> {

        private TraineeDao mAsyncTaskDao;

        insertAsyncTask(TraineeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trainee... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deleteAll(){
        new deleteAsyncTask(mTraineeDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private TraineeDao mAsyncTaskDao;

        deleteAsyncTask(TraineeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}

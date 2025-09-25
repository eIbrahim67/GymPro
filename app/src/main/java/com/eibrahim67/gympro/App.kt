package com.eibrahim67.gympro

import android.app.Application
import com.eibrahim67.gympro.data.local.repository.UserRepositoryImpl
import com.eibrahim67.gympro.data.local.source.LocalDateSourceImpl
import com.eibrahim67.gympro.data.local.source.UserDatabase
import com.eibrahim67.gympro.data.remote.repository.RemoteRepositoryImpl
import com.eibrahim67.gympro.data.remote.source.RemoteDataSourceImpl
import com.google.firebase.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class App : Application() {

    // Expose repositories as singletons (accessible globally)
    lateinit var userRepository: UserRepositoryImpl
        private set

    lateinit var remoteRepository: RemoteRepositoryImpl
        private set

    override fun onCreate() {
        super.onCreate()

        // ✅ Initialize Firebase
        FirebaseApp.initializeApp(this)

        // ✅ Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // ✅ Setup repositories once at app start
        val dao = UserDatabase.getDatabaseInstance(this).userDao()
        val localDataSource = LocalDateSourceImpl(dao)
        userRepository = UserRepositoryImpl(localDataSource)

        val remoteDataSource = RemoteDataSourceImpl(FirebaseFirestore.getInstance())
        remoteRepository = RemoteRepositoryImpl(remoteDataSource)
    }
}

package at.fhj.swd.travx.dao;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import at.fhj.swd.travx.domain.Journey;

@androidx.room.Database(entities = {Journey.class}, version = 4)
public abstract class Database extends RoomDatabase {

    private static Database instance;

    public abstract JourneyDao journeyDao();

    public synchronized static Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, Database.class, "travx").fallbackToDestructiveMigration().build();
        }

        return instance;
    }
}

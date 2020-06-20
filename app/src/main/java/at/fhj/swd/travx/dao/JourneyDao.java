package at.fhj.swd.travx.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import at.fhj.swd.travx.domain.Journey;

@Dao
public interface JourneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Journey journey);

    @Query("SELECT * FROM journey")
    List<Journey> findAll();

    @Query("DELETE FROM journey WHERE title LIKE :title")
    void remove(String title);

    @Query("SELECT * FROM journey WHERE title LIKE :title")
    Journey findByName(String title);
}

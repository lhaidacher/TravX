package at.fhj.swd.travx.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import at.fhj.swd.travx.domain.Bill;

@Dao
public interface BillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long add(Bill bill);

    @Query("SELECT * FROM bill WHERE id = :id")
    Bill findById(Long id);

    @Query("SELECT * FROM bill WHERE journey_title LIKE :journeyTitle")
    List<Bill> findAllByJourneyTitle(String journeyTitle);

}

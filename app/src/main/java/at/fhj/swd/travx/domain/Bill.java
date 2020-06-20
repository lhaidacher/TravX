package at.fhj.swd.travx.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import at.fhj.swd.travx.dao.DateConverter;

@Entity(tableName = "bill")
public class Bill {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @NonNull
    @ColumnInfo(name = "journey_title")
    private String journeyTitle;

    @NonNull
    @ColumnInfo(name = "value")
    private Long value;

    @NonNull
    @ColumnInfo(name = "log")
    private Float latitude;

    @NonNull
    @ColumnInfo(name = "lat")
    private Float longitude;

    @NonNull
    @ColumnInfo(name = "created_at")
    @TypeConverters({DateConverter.class})
    private Date createdAt;

    public Bill(@NonNull String journeyTitle, @NonNull Long value, @NonNull Float latitude, @NonNull Float longitude) {
        this.value = value;
        this.journeyTitle = journeyTitle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = new Date();
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getJourneyTitle() {
        return journeyTitle;
    }

    public void setJourneyTitle(@NonNull String journeyTitle) {
        this.journeyTitle = journeyTitle;
    }

    @NonNull
    public Long getValue() {
        return value;
    }

    public void setValue(@NonNull Long value) {
        this.value = value;
    }

    @NonNull
    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(@NonNull Float latitude) {
        this.latitude = latitude;
    }

    @NonNull
    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull Float longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }
}

package at.fhj.swd.travx.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import at.fhj.swd.travx.dao.DateConverter;

public class Bill {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private Long id;

    @NonNull
    @ColumnInfo(name = "value")
    private Long value;

    @NonNull
    @ColumnInfo(name = "created_at")
    @TypeConverters({DateConverter.class})
    private Date createdAt;

    public Bill(@NonNull Long value) {
        this.value = value;
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
    public Long getValue() {
        return value;
    }

    public void setValue(@NonNull Long value) {
        this.value = value;
    }

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }
}

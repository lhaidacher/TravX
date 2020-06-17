package at.fhj.swd.travx.domain;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journey")
public class Journey {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "description")
    private String description;

    @NonNull
    @ColumnInfo(name = "budget")
    private Long budget;

    public Journey(@NonNull String title, @NonNull String description, @NonNull Long budget) {
        this.title = title;
        this.description = description;
        this.budget = budget;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public Long getBudget() {
        return budget;
    }

    public void setBudget(@NonNull Long budget) {
        this.budget = budget;
    }
}

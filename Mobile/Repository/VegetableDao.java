package ubb.barcoaie.lab4_ma.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ubb.barcoaie.lab4_ma.Model.Vegetable;

@Dao
public interface VegetableDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vegetable vegetable);

    @Query("DELETE FROM vegetable_table where v_id = :vegetable_id")
    void delete(int vegetable_id);

    @Query("UPDATE vegetable_table SET name = :Vegetable_name, price = :Vegetable_price where v_id = :Vegetable_id")
    void update(int Vegetable_id, String Vegetable_name, int Vegetable_price);

    @Query("DELETE FROM vegetable_table")
    void deleteAll();

    @Query("SELECT * from vegetable_table ORDER BY name ASC")
    LiveData<List<Vegetable>> getVegetables();
}

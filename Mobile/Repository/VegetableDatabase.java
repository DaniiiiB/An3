package ubb.barcoaie.lab4_ma.Repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ubb.barcoaie.lab4_ma.Model.Vegetable;

@Database(entities = {Vegetable.class}, version = 1, exportSchema = false)
public abstract class VegetableDatabase extends RoomDatabase {

    public abstract VegetableDao vegetableDao();

    private static volatile VegetableDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static VegetableDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (VegetableDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            VegetableDatabase.class, "vegetable_database").addCallback(RoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback RoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                VegetableDao dao = INSTANCE.vegetableDao();
                dao.deleteAll();

                Vegetable vegetable = new Vegetable("carrot",5);
                dao.insert(vegetable);
                vegetable = new Vegetable("lettuce", 6);
                dao.insert(vegetable);
            });
        }
    };
}

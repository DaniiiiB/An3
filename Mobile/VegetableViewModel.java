package ubb.barcoaie.lab4_ma;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import ubb.barcoaie.lab4_ma.Model.Vegetable;
import ubb.barcoaie.lab4_ma.Repository.VegetableRepo;

public class VegetableViewModel extends AndroidViewModel {

    private VegetableRepo vegetableRepo;

    private final LiveData<List<Vegetable>> allVegetables;

    public VegetableViewModel(Application application) {
        super(application);
        vegetableRepo = new VegetableRepo(application);
        allVegetables = vegetableRepo.getAllVegetables();
    }

    public LiveData<List<Vegetable>> getAllVegetables() {
        return allVegetables;
    }

    /*
    public void insert(Vegetable vegetable) {
        vegetableRepo.insert(vegetable);
    }

    public void delete(Vegetable vegetable) { vegetableRepo.delete(vegetable);}
    */
}

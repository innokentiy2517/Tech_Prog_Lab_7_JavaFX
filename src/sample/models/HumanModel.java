package sample.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sound.sampled.AudioFileFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class HumanModel {
    ArrayList<Human> humanList = new ArrayList<>();
    private int counter = 1;
    Class<? extends Human> humanFilter = Human.class;

    public void setHumanFilter(Class<? extends Human> humanFilter){
        this.humanFilter = humanFilter;
        this.emitDataChanged();
    }

    public void saveToFile(String path) {
        try (Writer writer = new FileWriter(path)){
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerFor(new TypeReference<ArrayList<Human>>() {}).withDefaultPrettyPrinter().writeValue(writer,humanList);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadFromFile(String path) {
        try (Reader reader = new FileReader(path)){
            ObjectMapper mapper = new ObjectMapper();
            humanList = mapper.readerFor(new TypeReference<ArrayList<Human>>(){}).readValue(reader);
        }catch (IOException e){
            e.printStackTrace();
        }
        this.emitDataChanged();
    }

    public interface DataChangedListener{
        void dataChanged(ArrayList<Human> humanlist);
    }

    private ArrayList<DataChangedListener> dataChangedListener = new ArrayList<>();

    public void addDataChangedListener(DataChangedListener listener){
        this.dataChangedListener.add(listener);
    }

    public void load(){
        humanList.clear();
/**
        this.add(new Student("Ken", 21, 16000, 3),false);
        this.add(new Student("Alex", 20, 0, 4),false);
        this.add(new Student("Viva", 19, 3700, 2),false);
        this.add(new Employee("Eugene",21,15000, Employee.Education.University),false);
        this.add(new Teacher("Michael",25, 30000,Employee.Education.University,false),false);
*/
        this.emitDataChanged();
    }

    public void add(Human human){
        add(human,true);
    }

    public void add(Human human, boolean emit){
        human.id = counter;
        counter += 1;
        this.humanList.add(human);
        if (emit){
            this.emitDataChanged();
        }
    }

    public void edit(Human human){
        for (int i = 0; i < this.humanList.size(); ++i) {
            if (this.humanList.get(i).id == human.id){
                this.humanList.set(i,human);
                break;
            }
        }
        this.emitDataChanged();
    }

    public void delete(int id){
        for (int i = 0; i < this.humanList.size(); ++i) {
            if (this.humanList.get(i).id == id){
                this.humanList.remove(i);
                break;
            }
        }
        this.emitDataChanged();
    }

    private void emitDataChanged(){
        for (DataChangedListener listener: dataChangedListener){
            ArrayList<Human> filteredList = new ArrayList<>(
                    humanList.stream().filter(human -> humanFilter.isInstance(human)).collect(Collectors.toList())
            );
            listener.dataChanged(filteredList);
        }
    }
}

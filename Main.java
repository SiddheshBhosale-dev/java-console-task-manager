import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Task implements Serializable{
    int id;
    String taskName;
    String priority;
    String status;
    LocalDate dueDate;

    Task(int id, String taskName, String priority, LocalDate dueDate){
        this.id = id;
        this.taskName = taskName;
        this.priority = priority;
        this.status = "PENDING";
        this.dueDate = dueDate;
    }

    void complete(){
        status = "COMPLETED";
    }

    public String toString(){
        return id + " | Task Name: " + taskName + " | Priority: " + priority + " | Status: " + status + " | Due: " + dueDate;
    }
}

class TaskManager{
    List<Task> tasks = new ArrayList<>();
    final String FILE = "tasks.dat";


    TaskManager(){
    load();
}

    void add(Task t){
        tasks.add(t);
        save();
    }

    void delete(int id){
        tasks.removeIf(t -> t.id == id);
        save();
    }

    void complete(int id){
        for(Task t : tasks){
            if(t.id == id){
                t.complete();
                break;
            }
        }
        save(); 
    }

    void showAll(){
        if(tasks.isEmpty()){
            System.out.println("No tasks found");
            return;
        }
        for(Task t : tasks){
            System.out.println(t);
        }
    }

    void save(){

        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE))){
            out.writeObject(tasks);
        }
        catch(IOException e){
            System.out.println("Save failed");
        }
    }

    @SuppressWarnings("unchecked")
    void load(){
        File f = new File(FILE);
        if(!f.exists()) 
        return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE))){
            tasks = (List<Task>) in.readObject();
            
        } catch (Exception e) {
            System.out.println("Load Failed");
        }
    }
}




public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        TaskManager manager = new TaskManager();

        int id = 1;

        while (true) {
            System.out.println("\n 1.Add Task  2.View Tasks  3.Mark Completed  4.Delete Task  5.Exit");
            System.out.print("Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    System.out.println("Task Name : ");
                    String taskName = sc.nextLine();
                    System.out.println("Priority (LOW/MEDIUM/HIGH) : ");
                    String priority = sc.nextLine();
                    System.out.println("Due Date (YYYY-MM-DD)");
                    LocalDate dueDate = LocalDate.parse(sc.nextLine());

                    manager.add(new Task(id, taskName, priority, dueDate));
                    System.out.println("Task added successfully");
                    break;

                case 2:
                    manager.showAll();
                    break;
                
                case 3:
                    System.out.println("Task id: ");
                    manager.complete(Integer.parseInt(sc.nextLine()));
                    break;

                case 4:
                    System.out.println("Task id: ");
                    manager.delete(Integer.parseInt(sc.nextLine()));
                    break;

                case 5:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}

package chat0pt.storage;

import chat0pt.tasks.Deadline;
import chat0pt.tasks.Event;
import chat0pt.tasks.Task;
import chat0pt.tasks.Todo;
import chat0pt.ui.Ui;

import java.io.*;
import java.util.ArrayList;

public class Storage {
    private static String FILEPATH = "./chat0pt.txt";
    private static final File FILE = new File(FILEPATH);
    private static Ui ui;

    public Storage(Ui ui) {
        this.ui = ui;
    }

    /**
     * Reads file if it already exists, otherwise creates a new file.
     * @return Tasks ArrayList after parsing the file, if not return an empty ArrayList
     */
    public ArrayList<Task> onStart() {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            if (FILE.exists()) {
                tasks = readFile();
            } else {
                FILE.createNewFile();
            }

        } catch (IOException ex) {
            ui.failedFile();
        }
        return tasks;
    }

    /**
     * Reads the file and adds tasks.
     * @return Returns the tasklist after parsing the file
     * @throws IOException
     */
    private static ArrayList<Task> readFile() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(FILEPATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("#");
            switch (tokens[0]) {
            case "T":
                if (tokens.length == 3) {
                    boolean marked = Boolean.parseBoolean(tokens[1]);
                    String todoString = tokens[2];
                    Task todoTask = new Todo(todoString);
                    todoTask.setMarked(marked);
                    tasks.add(todoTask);
                }
                break;
            case "D":
                if (tokens.length == 4) {
                    boolean marked = Boolean.parseBoolean(tokens[1]);
                    String deadlineString = tokens[2];
                    String byString = tokens[3];
                    Task deadlineTask = new Deadline(deadlineString, byString);
                    deadlineTask.setMarked(marked);
                    tasks.add(deadlineTask);
                }
                break;
            case "E":
                if (tokens.length == 5) {
                    boolean marked = Boolean.parseBoolean(tokens[1]);
                    String eventString = tokens[2];
                    String fromString = tokens[3];
                    String toString = tokens[4];
                    Task eventTask = new Event(eventString, fromString, toString);
                    eventTask.setMarked(marked);
                    tasks.add(eventTask);
                }
                break;
            default:
                break;
            }
        }
        return tasks;
    }

    /**
     * Writes file after every action
     * @param tasks Current tasklist
     */
    public void writeFile(ArrayList<Task> tasks) {
        try {
            FileWriter fileWriter = new FileWriter(FILEPATH);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (Task t : tasks) {
                writer.write(t.toFile() + "\n");
            }
            writer.close();
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

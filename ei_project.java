import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

enum Level {
    HIGH, MEDIUM, LOW
}

class Plan implements Comparable<Plan> {
    private String name;
    private LocalTime start;
    private LocalTime end;
    private Level level;
    private boolean done;

    public Plan(String name, LocalTime start, LocalTime end, Level level) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.level = level;
        this.done = false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalTime getStart() { return start; }
    public void setStart(LocalTime start) { this.start = start; }
    public LocalTime getEnd() { return end; }
    public void setEnd(LocalTime end) { this.end = end; }
    public Level getLevel() { return level; }
    public void setLevel(Level level) { this.level = level; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    @Override
    public int compareTo(Plan other) {
        return this.start.compareTo(other.start);
    }

    @Override
    public String toString() {
        return start + "-" + end + " : " + name + " [" + level + "]" +
                (done ? " Completed" : "");
    }
}

interface Listener {
    void notify(String msg);
}

class ScreenNotifier implements Listener {
    @Override
    public void notify(String msg) {
        System.out.println(msg);
    }
}

class DayPlanner {
    private static DayPlanner onlyOne;
    private final List<Plan> plans = new ArrayList<>();
    private final List<Listener> listeners = new ArrayList<>();

    private DayPlanner() {}

    public static DayPlanner get() {
        if (onlyOne == null) {
            onlyOne = new DayPlanner();
        }
        return onlyOne;
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    private void inform(String msg) {
        for (Listener l : listeners) l.notify(msg);
    }

    public void addPlan(Plan p) {
        if (!p.getStart().isBefore(p.getEnd())) {
            inform("Start must be before End!");
            return;
        }
        for (Plan old : plans) {
            if (p.getStart().isBefore(old.getEnd()) && p.getEnd().isAfter(old.getStart())) {
                inform("Clash with: " + old.getName());
                return;
            }
        }
        plans.add(p);
        inform("Added: " + p.getName());
    }

    public void showPlans() {
        if (plans.isEmpty()) {
            inform("No plans today.");
            return;
        }
        List<Plan> copy = new ArrayList<>(plans);
        Collections.sort(copy);
        for (Plan p : copy) System.out.println(p);
    }

    public void removePlan(String name) {
        Plan found = find(name);
        if (found != null) {
            plans.remove(found);
            inform("Removed " + name);
        } else {
            inform("Not found!");
        }
    }

    public void finish(String name) {
        Plan found = find(name);
        if (found != null) {
            found.setDone(true);
            inform("Marked completed.");
        } else {
            inform("Not found!");
        }
    }

    public void edit(String oldName, String newName, String s, String e, String lvl) {
        Plan found = find(oldName);
        if (found == null) {
            inform("Not found!");
            return;
        }

        try {
            LocalTime ns = LocalTime.parse(s);
            LocalTime ne = LocalTime.parse(e);
            Level nl = Level.valueOf(lvl.toUpperCase());

            plans.remove(found);
            for (Plan other : plans) {
                if (ns.isBefore(other.getEnd()) && ne.isAfter(other.getStart())) {
                    inform("Edit clashes with: " + other.getName());
                    plans.add(found);
                    return;
                }
            }

            found.setName(newName);
            found.setStart(ns);
            found.setEnd(ne);
            found.setLevel(nl);
            plans.add(found);

            inform("Edited successfully.");

        } catch (DateTimeParseException ex) {
            inform("Bad time format.");
        } catch (Exception ex) {
            inform("Bad input.");
        }
    }

    public void showByLevel(String lvl) {
        try {
            Level l = Level.valueOf(lvl.toUpperCase());
            List<Plan> result = new ArrayList<>();
            for (Plan p : plans) {
                if (p.getLevel() == l) result.add(p);
            }
            if (result.isEmpty()) {
                inform("No tasks with " + lvl);
                return;
            }
            Collections.sort(result);
            for (Plan p : result) System.out.println(p);
        } catch (Exception e) {
            inform("Invalid level.");
        }
    }

    private Plan find(String name) {
        for (Plan p : plans) {
            if (p.getName().equalsIgnoreCase(name)) return p;
        }
        return null;
    }
}

public class AstronautPlannerApp {
    public static void main(String[] args) {
        DayPlanner planner = DayPlanner.get();
        planner.addListener(new ScreenNotifier());
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Astronaut Day Planner ===");
        boolean go = true;
        while (go) {
            System.out.println("\n1. Add plan");
            System.out.println("2. Remove plan");
            System.out.println("3. View all");
            System.out.println("4. Edit");
            System.out.println("5. Mark done");
            System.out.println("6. View by priority");
            System.out.println("7. Exit");
            System.out.print("Option: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Name: ");
                    String n = sc.nextLine();
                    System.out.print("Start (HH:mm): ");
                    String st = sc.nextLine();
                    System.out.print("End (HH:mm): ");
                    String et = sc.nextLine();
                    System.out.print("Priority (High/Medium/Low): ");
                    String lvl = sc.nextLine();
                    try {
                        Plan p = new Plan(n, LocalTime.parse(st), LocalTime.parse(et), Level.valueOf(lvl.toUpperCase()));
                        planner.addPlan(p);
                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                    }
                    break;
                case "2":
                    System.out.print("Name to remove: ");
                    planner.removePlan(sc.nextLine());
                    break;
                case "3":
                    planner.showPlans();
                    break;
                case "4":
                    System.out.print("Old name: ");
                    String on = sc.nextLine();
                    System.out.print("New name: ");
                    String nn = sc.nextLine();
                    System.out.print("New start: ");
                    String ns = sc.nextLine();
                    System.out.print("New end: ");
                    String ne = sc.nextLine();
                    System.out.print("New priority: ");
                    String nl = sc.nextLine();
                    planner.edit(on, nn, ns, ne, nl);
                    break;
                case "5":
                    System.out.print("Name: ");
                    planner.finish(sc.nextLine());
                    break;
                case "6":
                    System.out.print("Priority: ");
                    planner.showByLevel(sc.nextLine());
                    break;
                case "7":
                    go = false;
                    System.out.println("Bye!");
                    break;
                default:
                    System.out.println("Invalid!");
            }
        }
        sc.close();
    }
}

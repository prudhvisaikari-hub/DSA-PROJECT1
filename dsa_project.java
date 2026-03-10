import java.util.*;
enum Role {
    PARENT, DOCTOR, CARETAKER
}

enum Domain {
    SOCIAL, COMMUNICATION, SENSORY, PHYSICAL, COGNITIVE
}

enum Priority {
    LOW(1), MEDIUM(2), HIGH(3), CRITICAL(4);

    private final int level;

    Priority(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

class User {
    String id;
    String email;
    Role role;

    public User(String id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }
}

class Milestone {
    String description;
    Domain domain;
    long timestamp; // Represents time

    public Milestone(String description, Domain domain) {
        this.description = description;
        this.domain = domain;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "[" + domain + "] " + description;
    }
}

class Trigger implements Comparable<Trigger> {
    String description;
    Priority priority;
    long timestamp;

    public Trigger(String description, Priority priority) {
        this.description = description;
        this.priority = priority;
        this.timestamp = System.currentTimeMillis(); 
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        } 
    }

    @Override
    public int compareTo(Trigger other) {
        
        if (this.priority.getLevel() != other.priority.getLevel()) {
            return Integer.compare(other.priority.getLevel(), this.priority.getLevel());
        }
    
        return Long.compare(this.timestamp, other.timestamp);
    }

    @Override
    public String toString() {
        return "[Priority: " + priority + "] " + description;
    }
}

class Child {
    String id;
    String name;
    int age;
    String diagnosis;

    
    LinkedList<Milestone> milestones;

    
    PriorityQueue<Trigger> triggersQueue;

    public Child(String id, String name, int age, String diagnosis) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.diagnosis = diagnosis;
        this.milestones = new LinkedList<>();
        this.triggersQueue = new PriorityQueue<>();
    }

    
    public void addMilestone(Milestone m) {
        milestones.add(m);
    }

    
    public void addTrigger(Trigger t) {
        triggersQueue.add(t);
    }
}


class BrightFuturesSystem {
   
    private Map<String, User> users;
    private Map<String, Child> children;

    
    private Map<String, Set<String>> userChildAccessMap;

    public BrightFuturesSystem() {
        users = new HashMap<>();
        children = new HashMap<>();
        userChildAccessMap = new HashMap<>();
    }

  
    public void registerUser(User u) {
        users.put(u.id, u);
        userChildAccessMap.putIfAbsent(u.id, new HashSet<>());
        System.out.println("Registered User: " + u.email + " as " + u.role);
    }

   
    public void registerChild(Child c) {
        children.put(c.id, c);
        System.out.println("Registered Child profile: " + c.name);
    }

    
    public void grantAccess(String userId, String childId) {
        if (users.containsKey(userId) && children.containsKey(childId)) {
            userChildAccessMap.get(userId).add(childId);
            System.out.println(
                    "Granted user " + users.get(userId).email + " access to child " + children.get(childId).name);
        }
    }

   
    public boolean hasAccess(String userId, String childId) {
        return userChildAccessMap.containsKey(userId) && userChildAccessMap.get(userId).contains(childId);
    }

    public void logMilestone(String userId, String childId, String description, Domain domain) {
        if (hasAccess(userId, childId)) {
            Child c = children.get(childId);
            c.addMilestone(new Milestone(description, domain));
            System.out.println("Milestone logged for " + c.name + " by " + users.get(userId).role);
        } else {
            System.out.println("Access Denied: Cannot log milestone.");
        }
    }

    public void logTrigger(String userId, String childId, String description, Priority priority) {
        if (hasAccess(userId, childId)) {
            Child c = children.get(childId);
            c.addTrigger(new Trigger(description, priority));
            System.out.println("Trigger logged for " + c.name + " with priority " + priority.name());
        } else {
            System.out.println("Access Denied: Cannot log trigger.");
        }
    }

    t
    public void processTriggers(String userId, String childId) {
        if (hasAccess(userId, childId)) {
            Child c = children.get(childId);
            System.out.println("--- Processing Triggers for " + c.name + " ---");
            if (c.triggersQueue.isEmpty()) {
                System.out.println("No pending triggers.");
                return;
            }
            while (!c.triggersQueue.isEmpty()) {
                Trigger t = c.triggersQueue.poll();
                System.out.println("Addressing Trigger: " + t);
            }
        }
    }

   
    public void viewMilestones(String userId, String childId, Domain filter) {
        if (hasAccess(userId, childId)) {
            Child c = children.get(childId);
            System.out.println("--- Milestones for " + c.name + (filter != null ? " (" + filter + ")" : "") + " ---");
            boolean found = false;
            for (Milestone m : c.milestones) {
                if (filter == null || m.domain == filter) {
                    System.out.println(m);
                    found = true;
                }
            }
            if (!found)
                System.out.println("No milestones found under this filter.");
        }
    }

    
    public List<User> getCareTeamForChild(String childId) {
        List<User> careTeam = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : userChildAccessMap.entrySet()) {
            if (entry.getValue().contains(childId)) {
                careTeam.add(users.get(entry.getKey()));
            }
        }
        return careTeam;
    }
}

public class dsa_project {
    public static void main(String[] args) {
        System.out.println("=== Bright Futures System Demo (Data Structures & Algorithms Implementation) ===\n");

        BrightFuturesSystem system = new BrightFuturesSystem();

       
        User parent = new User("U1", "parent@demo.com", Role.PARENT);
        User doctor = new User("U2", "doctor@clinic.com", Role.DOCTOR);
        User caretaker = new User("U3", "teddy_bear_daycare@demo.com", Role.CARETAKER);

        system.registerUser(parent);
        system.registerUser(doctor);
        system.registerUser(caretaker);

        System.out.println();

        Child alex = new Child("C1", "Alex Smith", 7, "Autism Spectrum");
        system.registerChild(alex);

        
        system.grantAccess(parent.id, alex.id);
        system.grantAccess(doctor.id, alex.id);
        system.grantAccess(caretaker.id, alex.id);

        System.out.println("\n--- Entering Data Phase ---");

       
        system.logMilestone(parent.id, alex.id, "Maintained eye contact for 5 seconds.", Domain.SOCIAL);
        system.logMilestone(caretaker.id, alex.id, "Followed 2-step instruction.", Domain.COGNITIVE);
        system.logMilestone(doctor.id, alex.id, "Responded well to new medication.", Domain.PHYSICAL);
        system.logMilestone(doctor.id, alex.id, "Clear auditory sensory threshold improved.", Domain.SENSORY);

        
        system.logTrigger(caretaker.id, alex.id, "Refused to eat lunch.", Priority.LOW); // Low severity
        system.logTrigger(parent.id, alex.id, "Severe sensory overload in grocery store.", Priority.CRITICAL); // Highest
                                                                                                               // severity
        system.logTrigger(caretaker.id, alex.id, "Mild upset when routine changed.", Priority.MEDIUM); // Medium
                                                                                                       // severity

       
        System.out.println("\n--- Doctor Views Physical Milestones ---");
        system.viewMilestones(doctor.id, alex.id, Domain.PHYSICAL);

        System.out.println("\n--- Caretaker Views All Milestones ---");
        system.viewMilestones(caretaker.id, alex.id, null);

        
        System.out.println();
        system.processTriggers(parent.id, alex.id);

       
        System.out.println("\n--- Care Team for Alex ---");
        List<User> careTeam = system.getCareTeamForChild(alex.id);
        for (User u : careTeam) {
            System.out.println("- " + u.role + " (" + u.email + ")");
        }
    }
}

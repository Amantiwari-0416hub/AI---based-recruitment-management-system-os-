import java.util.LinkedList;
import java.util.Queue;

// Shared Resource Class representing the Interview Slot / Shared Database
class InterviewScheduler {
    private final int capacity;
    private final Queue<String> resumeQueue = new LinkedList<>();

    public InterviewScheduler(int capacity) {
        this.capacity = capacity;
    }

    // Producer: Candidate uploading resume
    public synchronized void uploadResume(String candidateName) throws InterruptedException {
        while (resumeQueue.size() == capacity) {
            System.out.println("Queue is full! Candidate " + candidateName + " waiting...");
            wait(); // Wait if the buffer is full
        }
        resumeQueue.add(candidateName);
        System.out.println("Resume uploaded by: " + candidateName);
        notifyAll(); // Notify consumer (AI screening engine)
    }

    // Consumer: AI Screening Engine processing resumes
    public synchronized void processResume() throws InterruptedException {
        while (resumeQueue.isEmpty()) {
            wait(); // Wait if no resumes are in the queue
        }
        String candidate = resumeQueue.poll();
        System.out.println("AI Screening Engine processing resume for: " + candidate);
        notifyAll(); // Notify producers
    }
}

public class RecruitmentSystemDemo {
    public static void main(String[] args) {
        InterviewScheduler system = new InterviewScheduler(5);

        // Simulating Producer (Candidate upload thread)
        Thread candidateThread = new Thread(() -> {
            try {
                system.uploadResume("Candidate_Rahul");
                Thread.sleep(1000);
                system.uploadResume("Candidate_Priya");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Simulating Consumer (AI Processing thread)
        Thread aiThread = new Thread(() -> {
            try {
                system.processResume();
                Thread.sleep(1000);
                system.processResume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        candidateThread.start();
        aiThread.start();
    }
}

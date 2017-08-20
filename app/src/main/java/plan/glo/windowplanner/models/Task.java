package plan.glo.windowplanner.models;
import java.util.Date;
import java.util.List;

public class Task implements TaskI{

   private List<JobI> jobs;
   private Date startDate;
   private Date endDate;
   private String name;
   private static int TASK_COUNT = 0;
   private int id;

   public Task(List<JobI> jobs, Date startDate, Date endDate, String name) {
       this(jobs, startDate, endDate, name, TASK_COUNT++);
   }

   Task(List<JobI> jobs, Date startDate, Date endDate, String name, int id) {
       this.jobs      = jobs;
       this.startDate = startDate;
       this.endDate   = endDate;
       this.name      = name;
       this.id        = id;
       TASK_COUNT     = Math.max(id, TASK_COUNT);
   }

   @Override
   public int getId() {
      return id;
   }

   @Override
   public String getTaskName() {
      return name;
   }

   @Override
   public Date getTaskEndDate() {
      return endDate;
   }

   @Override
   public Date getTaskStartDate() {
      return startDate;
   }

   @Override
   public List<JobI> getJobs() {
      return this.jobs;
   }

   @Override
   public void modifyTask(TaskI configObject) {
      this.jobs = configObject.getJobs() == null ? this.getJobs() : configObject.getJobs();
      this.startDate = configObject.getTaskStartDate() == null ? this.getTaskStartDate() : configObject.getTaskStartDate();
      this.endDate = configObject.getTaskEndDate() == null ? this.getTaskEndDate() : configObject.getTaskEndDate();
      this.name = configObject.getTaskName() == null ? this.getTaskName(): configObject.getTaskName();
   }

   @Override
   public JobI getFreeJob() {
      for (JobI j : this.jobs) {
         if (!j.hasEvent()) {
            return j;
         }
      }
      return null;
   }
}
